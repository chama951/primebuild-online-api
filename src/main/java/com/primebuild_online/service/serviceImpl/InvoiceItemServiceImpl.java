package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Invoice;
import com.primebuild_online.model.InvoiceItem;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.enumerations.InvoiceStatus;
import com.primebuild_online.repository.InvoiceItemRepository;
import com.primebuild_online.service.InvoiceItemService;
import com.primebuild_online.service.ItemService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InvoiceItemServiceImpl implements InvoiceItemService {
    private final InvoiceItemRepository invoiceItemRepository;
    private final ItemService itemService;

    public InvoiceItemServiceImpl(InvoiceItemRepository invoiceItemRepository,
                                  @Lazy ItemService itemService) {
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

    @Override
    public void reduceItemQuantity(List<InvoiceItem> invoiceItemList) {
        for (InvoiceItem invoiceItem : invoiceItemList) {
            Item item = invoiceItem.getItem();
            Integer quantityToReduce = invoiceItem.getInvoiceQuantity();
            itemService.reduceItemQuantity(item, quantityToReduce);
        }
    }

    @Override
    public boolean existsInvoiceByItem(Long id) {
        return invoiceItemRepository.existsByItem_Id(id);
    }

    @Override
    public BigDecimal calculateDiscountAmount(List<InvoiceItem> invoiceItems) {
        BigDecimal discountAmount = BigDecimal.ZERO;
        for (InvoiceItem invoiceItem : invoiceItems) {
            discountAmount = discountAmount.add(invoiceItem.getDiscountSubTotal());
        }
        return discountAmount;
    }

    @Override
    public BigDecimal calculateTotalAmount(List<InvoiceItem> invoiceItems) {
        BigDecimal TotalAmount = BigDecimal.ZERO;
        for (InvoiceItem invoiceItem : invoiceItems) {
            TotalAmount = TotalAmount.add(invoiceItem.getSubtotal());
        }
        return TotalAmount;
    }

    @Override
    public void updateInvoiceItemAtPriceChange(List<InvoiceItem> invoiceItems) {
        for (InvoiceItem invoiceItem : invoiceItems) {

            invoiceItem.setUnitPrice(invoiceItem.getItem().getPrice());
            invoiceItem.setDiscountSubTotal(itemService.calculateDiscountSubTotal(invoiceItem.getItem(), invoiceItem.getInvoiceQuantity()));
            invoiceItem.setDiscountPerUnite(itemService.calculateDiscountPerUnite(invoiceItem.getItem()));

            invoiceItem.setSubtotal(itemService.calculateSubTotal(invoiceItem.getItem(), invoiceItem.getInvoiceQuantity()));
            invoiceItemRepository.save(invoiceItem);
        }
    }

}
