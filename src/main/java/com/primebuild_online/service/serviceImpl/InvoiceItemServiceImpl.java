package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Invoice;
import com.primebuild_online.model.InvoiceItem;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.enumerations.InvoiceStatus;
import com.primebuild_online.repository.InvoiceItemRepository;
import com.primebuild_online.service.InvoiceItemService;
import com.primebuild_online.service.ItemService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InvoiceItemServiceImpl implements InvoiceItemService {
    private final InvoiceItemRepository invoiceItemRepository;
    private final ItemService itemService;

    public InvoiceItemServiceImpl(InvoiceItemRepository invoiceItemRepository,
                                  ItemService itemService) {
        this.invoiceItemRepository = invoiceItemRepository;
        this.itemService = itemService;
    }


    @Override
    public InvoiceItem saveInvoiceItem(Item itemToAdd, Invoice invoice) {
        Item itemInDb = itemService.getItemById(itemToAdd.getId());

        itemService.checkItemsStockQuantity(itemInDb, itemToAdd);

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setItem(itemInDb);
        invoiceItem.setInvoice(invoice);
        invoiceItem.setInvoiceQuantity(itemToAdd.getQuantity());
        invoiceItem.setUnitPrice(itemInDb.getPrice());
        invoiceItem.setDiscountSubTotal(itemService.calculateDiscountSubTotal(itemInDb, itemToAdd.getQuantity()));
        invoiceItem.setDiscountPerUnite(itemService.calculateDiscountPerUnite(itemInDb));
        invoiceItem.setSubtotal(itemService.calculateSubTotal(itemInDb, itemToAdd.getQuantity()));

        if (invoice.getInvoiceStatus().equals(InvoiceStatus.PAID)) {
            Integer quantityToReduce = invoiceItem.getInvoiceQuantity();
            itemService.reduceItemQuantity(itemInDb, quantityToReduce);
        }
        return invoiceItemRepository.save(invoiceItem);
    }


    @Override
    public void deleteInvoiceItemsByInvoiceId(Long id) {
        invoiceItemRepository.deleteAllByInvoice_Id(id);
    }

    @Override
    public void resetItemQuantity(List<InvoiceItem> invoiceItemList) {
        for (InvoiceItem invoiceItem : invoiceItemList) {
            Item item = invoiceItem.getItem();
            Integer quantityToReset = invoiceItem.getInvoiceQuantity();
            itemService.resetItemStockQuantity(item, quantityToReset);
        }
    }

}
