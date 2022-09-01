package com.example.printifybackend.keyring;

import com.example.printifybackend.Converter;
import com.example.printifybackend.binary_obj.EntityBinaryObject;
import com.example.printifybackend.binary_obj.ServiceBinaryObject;
import com.example.printifybackend.contact_into.EntityContactInfo;
import com.example.printifybackend.contact_into.ServiceContactInfo;
import com.example.printifybackend.item.ServiceItem;
import com.example.printifybackend.keyring.dto.DtoKeyringOrder;
import com.example.printifybackend.keyring.dto.DtoItemKeyring;
import com.example.printifybackend.order.EntityOrder;
import com.example.printifybackend.order.EntityOrderItem;
import com.example.printifybackend.order.ServiceOrder;
import com.example.printifybackend.order.ServiceOrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceKeyring {

    private final ServiceContactInfo serviceContactInfo;
    private final ServiceOrder serviceOrder;
    private final ServiceBinaryObject serviceBinaryObject;
    private final ServiceItem serviceItem;
    private final ServiceOrderItem serviceOrderItem;

    @SuppressWarnings("squid:S2259") // order will not be null if keyringItem must not be null
    public void createOrder(DtoKeyringOrder keyringOrder) {
        if (keyringOrder == null || keyringOrder.contactInfo() == null || keyringOrder.keyringItem() == null) return;

        // insert contact info
        final Long contactInfoId = this.serviceContactInfo.insert(Converter.convert(keyringOrder.contactInfo(), EntityContactInfo.class));

        // insert order
        final EntityOrder order = this.createOrderFromKeyringItem(keyringOrder.keyringItem());
        order.setContactInfoId(contactInfoId);
        final Long orderId = this.serviceOrder.insert(order);

        // insert file
        final Long fileId = this.serviceBinaryObject.insert(new EntityBinaryObject(keyringOrder.keyringItem().modelFile()));

        // insert keyring item as item
        final Long itemId = this.serviceItem.saveKeyringItem(keyringOrder.keyringItem(), fileId);

        // insert order item
        this.serviceOrderItem.insert(new EntityOrderItem(itemId, orderId, keyringOrder.keyringItem().amount(), null));
    }

    private EntityOrder createOrderFromKeyringItem(DtoItemKeyring keyringItem) {
        if (keyringItem == null) return null;

        final EntityOrder order = this.serviceOrder.createNewEmptyOrder();
        order.setAdditionalInfo(keyringItem.additionalInformation());
        return order;
    }


}
