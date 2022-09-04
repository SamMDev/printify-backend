package com.example.printifybackend.order;

import com.example.printifybackend.AbstractEntityService;
import com.example.printifybackend.Converter;
import com.example.printifybackend.contact_into.DtoContactInfo;
import com.example.printifybackend.contact_into.EntityContactInfo;
import com.example.printifybackend.contact_into.ServiceContactInfo;
import com.example.printifybackend.item.EntityItem;
import com.example.printifybackend.item.ItemType;
import com.example.printifybackend.item.ServiceItem;
import com.example.printifybackend.jdbi.JoinedEntity;
import com.example.printifybackend.jdbi.LazyCriteria;
import com.example.printifybackend.order.dto.request.DtoRequestOrder;
import com.example.printifybackend.order.dto.request.DtoOrderItem;
import com.example.printifybackend.order.dto.response.DtoResponseOrder;
import com.example.printifybackend.order.dto.response.DtoResponseOrderDetail;
import com.example.printifybackend.order.dto.response.DtoResponseOrderDetailOrderItemRow;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServiceOrder extends AbstractEntityService<EntityOrder, DaoOrder> {
    private final ServiceContactInfo serviceContactInfo;
    private final ServiceOrderItem serviceOrderItem;
    private final ServiceItem serviceItem;

    @Autowired
    public ServiceOrder(DaoOrder dao, ServiceContactInfo serviceContactInfo, ServiceOrderItem serviceOrderItem, ServiceItem serviceItem) {
        super(dao);
        this.serviceContactInfo = serviceContactInfo;
        this.serviceOrderItem = serviceOrderItem;
        this.serviceItem = serviceItem;
    }

    public void createOrder(DtoRequestOrder order) {
        // none of these objects can be null
        if (order == null || ObjectUtils.anyNull(order.getItems(), order.getContactInfo())) return;

        // first save contact info
        final Long contactInfoId = this.serviceContactInfo.insert(Converter.convert(order.getContactInfo(), EntityContactInfo.class));

        // save order
        final Long orderId = this.insert(this.createNewOrderFromDtoOrderItemsWithContactInfo(order.getItems(), contactInfoId));

        // save order items
        this.makeItemPairsByUuid(
                order.getItems(),
                this.serviceItem.findByUuids(order.getItems().stream().map(DtoOrderItem::getItemUuid).toList())
        )
        // save each found pair
        .forEach((itemDto, item) -> this.serviceOrderItem.insert(new EntityOrderItem(item.getId(), orderId, itemDto.getAmount(), itemDto.getPrice())));
    }

    public List<DtoResponseOrder> getOrdersByCriteria(LazyCriteria criteria) {
        return Optional.ofNullable(criteria)
                .map(this.dao::loadByCriteriaWithItems)
                // joined to DtoResponseOrder
                .map(joinedList -> joinedList.stream().map(this::joinedToDtoResponseObject).toList())
                .orElse(Collections.emptyList());
    }

    public DtoResponseOrderDetail getOrderDetail(Long orderId) {
        final LazyCriteria idCriteria = new LazyCriteria(null, null, Collections.singletonMap("id", orderId));
        return this.dao.loadFullByCriteria(idCriteria)
                .stream()
                .findFirst()
                .map(this::joinedToDtoResponseOrderDetail)
                .orElse(null);
    }

    public EntityOrder createNewEmptyOrder() {
        return EntityOrder.builder()
                .status(OrderStatus.OPEN)
                .payStatus(OrderPayStatus.NOT_PAID)
                .build();
    }

    private DtoResponseOrderDetail joinedToDtoResponseOrderDetail(JoinedEntity joined) {
        final DtoResponseOrderDetail orderDetail = Converter.convert(this.joinedToDtoResponseObject(joined), DtoResponseOrderDetail.class);
        orderDetail.setContactInfo(Converter.convert(joined.getJoinedOne(EntityContactInfo.class), DtoContactInfo.class));
        orderDetail.setOrderItems(this.getOrderItemsRowsFromJoinedEntity(joined));
        return orderDetail;
    }

    private List<DtoResponseOrderDetailOrderItemRow> getOrderItemsRowsFromJoinedEntity(JoinedEntity entity) {
        if (entity == null) return null;

        final Set<EntityOrderItem> orderItems = entity.getJoinedMany(EntityOrderItem.class);
        final Map<Long, EntityItem> items = entity.getJoinedMany(EntityItem.class).stream()
                .collect(Collectors.toMap(EntityItem::getId, item -> item));

       return orderItems.stream()
                .map(orderItem -> {
                    final EntityItem item = items.getOrDefault(orderItem.getItemId(), null);
                    return this.toOrderItemRow(orderItem, item);
                })
               .toList();
    }

    private DtoResponseOrderDetailOrderItemRow toOrderItemRow(EntityOrderItem orderItem, EntityItem item) {
        if (orderItem == null || item == null) return null;

        return DtoResponseOrderDetailOrderItemRow.builder()
                .amount(orderItem.getAmount())
                .price(orderItem.getPrice())
                .item(Converter.convert(item, DtoResponseOrderDetailOrderItemRow.DtoDetailItemRow.class))
                .build();
    }

    private DtoResponseOrder joinedToDtoResponseObject(JoinedEntity joined) {
        if (joined == null) return null;

        return Optional.ofNullable(joined.getJoinedOne(EntityOrder.class))
                .map(e -> {
                    final DtoResponseOrder result = Converter.convert(e, DtoResponseOrder.class);
                    result.setOrderType(this.determineOrderType(joined.getJoinedMany(EntityItem.class)));
                    return result;
                })
                .orElse(null);
    }

    private OrderType determineOrderType(Collection<EntityItem> itemsForOrder) {
        if (itemsForOrder == null) return null;

        return itemsForOrder.stream()
                .map(EntityItem::getType)
                .findFirst()
                .map(type -> type == ItemType.KEYRING ? OrderType.KEYRING_ORDER : OrderType.PRODUCT_ORDER)
                .orElse(null);
    }

    private EntityOrder createNewOrderFromDtoOrderItemsWithContactInfo(List<DtoOrderItem> items, Long contactInfoId) {
        final EntityOrder order = this.createNewOrderFromDtoOrderItems(items);
        order.setContactInfoId(contactInfoId);
        return order;
    }

    private EntityOrder createNewOrderFromDtoOrderItems(List<DtoOrderItem> items) {
        if (items == null || items.isEmpty()) return this.createEmptyItemsOrder();

        return EntityOrder.builder()
                .price(items.stream().map(DtoOrderItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add))
                .status(OrderStatus.OPEN)
                .payStatus(OrderPayStatus.NOT_PAID)
                .build();
    }

    private EntityOrder createEmptyItemsOrder() {
        return EntityOrder.builder()
                .status(OrderStatus.OPEN)
                .payStatus(OrderPayStatus.NOT_PAID)
                .price(BigDecimal.ZERO)
                .build();
    }

    /**
     * With given dtoOrderItems and items, make pairs by uuid
     */
    private Map<DtoOrderItem, EntityItem> makeItemPairsByUuid(List<DtoOrderItem> dtoOrderItems, List<EntityItem> items) {
        if (dtoOrderItems == null || items == null || dtoOrderItems.isEmpty() || items.isEmpty()) return Collections.emptyMap();

        final Map<DtoOrderItem, EntityItem> result = new HashMap<>();


        final Map<String, EntityItem> itemsByUuid = items.stream().collect(Collectors.toMap(
                EntityItem::getUuid,
                item -> items.stream().filter(i -> Objects.equals(i.getUuid(), item.getUuid())).findFirst().orElseThrow()));

        for (DtoOrderItem dtoOrderItem : dtoOrderItems) {
            if (!itemsByUuid.containsKey(dtoOrderItem.getItemUuid())) continue;

            result.put(dtoOrderItem, itemsByUuid.get(dtoOrderItem.getItemUuid()));
        }

        return result;
    }
}
