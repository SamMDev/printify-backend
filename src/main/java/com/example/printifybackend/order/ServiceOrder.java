package com.example.printifybackend.order;

import com.example.printifybackend.Converter;
import com.example.printifybackend.item.DaoItem;
import com.example.printifybackend.item.DtoItem;
import com.example.printifybackend.item.ServiceItem;
import com.example.printifybackend.jdbi.JoinedEntity;
import com.example.printifybackend.jdbi.LazyCriteria;
import com.example.printifybackend.order.dto.DtoOrder;
import com.example.printifybackend.order.dto.DtoOrderDetail;
import com.example.printifybackend.order.dto.DtoRequestOrder;
import com.example.printifybackend.order.dto.DtoSingleOrderItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServiceOrder {

    private final DaoOrder daoOrder;
    private final ServiceItem serviceItem;
    @Autowired
    public ServiceOrder(DaoOrder daoOrder, ServiceItem serviceItem) {
        this.daoOrder = daoOrder;
        this.serviceItem = serviceItem;
    }

    public void saveOrder(DtoRequestOrder order) {
        EntityOrder entityOrder = Converter.convert(order, EntityOrder.class);
        if (entityOrder == null) return;
        entityOrder.setDate(LocalDateTime.now());
        entityOrder.setFinished(false);

        this.daoOrder.insert(entityOrder);
    }

    /**
     * Gets detail object by given id
     *
     * @param id    wanted id
     * @return      detail object
     */
    public DtoOrderDetail getDetailById(Long id) {
        EntityOrder order = this.daoOrder.findById(id);
        if (order == null) return null;

        DtoOrderDetail detail = Converter.convert(order, DtoOrderDetail.class);
        final List<DtoSingleOrderItem> orderItems = this.extractOrderItemsFromSerializedForm(order.getContent());
        detail.setOrderItems(orderItems);

        return detail;
    }

    public List<DtoOrder> getWithCriteria(LazyCriteria lazyCriteria) {
        return
                this.daoOrder.getOrdersWithCriteria(lazyCriteria)
                .stream()
                .map(o -> Converter.convert(o, DtoOrder.class))
                .collect(Collectors.toList());
    }

    public Long count(LinkedHashMap<String, Object> filter) {
        return this.daoOrder.totalRowCount(filter);
    }

    /**
     * Reads serialized form of orders and returns list of order items
     *
     * @param ordersSerialized  Orders serialized
     * @return                  List of order items
     */
    private List<DtoSingleOrderItem> extractOrderItemsFromSerializedForm(String ordersSerialized) {
        final List<DeserializedOrderModel> ordersDeserialized = Arrays.asList(Converter.read(ordersSerialized, DeserializedOrderModel[].class));
        return this.getOrderItemsFromSerializedModels(ordersDeserialized);
    }

    /**
     * Reads information from deserialized orders, and returns list of order items
     *
     * @param models    all the deserialized orders we want to extract from
     * @return          List of order items
     */
    private List<DtoSingleOrderItem> getOrderItemsFromSerializedModels(List<DeserializedOrderModel> models) {
        final List<String> itemUuids = models.stream().map(DeserializedOrderModel::getUuid).collect(Collectors.toList());
        List<DtoItem> items = this.serviceItem.findByUuidsWithImages(itemUuids);
        return items.stream().map(
                item -> {
                    // find deserialized order assigned to item (same uuid)
                    final DeserializedOrderModel assignedDeserialized = models.stream().filter(m -> Objects.equals(m.getUuid(), item.getUuid())).findFirst().orElse(null);
                    if (assignedDeserialized == null) return null;

                    final DtoSingleOrderItem singleOrderItem = Converter.convert(item, DtoSingleOrderItem.class);
                    singleOrderItem.setAmount(assignedDeserialized.getAmount());
                    singleOrderItem.setPrice(assignedDeserialized.getPrice());
                    return singleOrderItem;
                }
        )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Data
    public static class DeserializedOrderModel {
        @JsonProperty("id")
        private String uuid;
        private Integer amount;
        private BigDecimal price;
    }
}
