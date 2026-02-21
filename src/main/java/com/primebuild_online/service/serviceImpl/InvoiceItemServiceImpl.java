package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Invoice;
import com.primebuild_online.model.InvoiceItem;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.enumerations.InvoiceStatus;
import com.primebuild_online.repository.InvoiceItemRepository;
import com.primebuild_online.service.InvoiceItemService;
import com.primebuild_online.service.ItemService;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.http.HttpStatus;
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
    public InvoiceItem saveInvoiceItem(Item item, Invoice invoice) {
        Item itemInDb = itemService.getItemById(item.getId());

        if (itemInDb.getQuantity() < item.getQuantity()) {
            throw new PrimeBuildException(
                    itemInDb.getItemName()+" Insufficient Stock",
                    HttpStatus.BAD_REQUEST);
        }

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setItem(itemInDb);
        invoiceItem.setInvoice(invoice);
        invoiceItem.setInvoiceQuantity(item.getQuantity());
        invoiceItem.setUnitPrice(itemInDb.getPrice());
        invoiceItem.setDiscountSubTotal(calculateDiscountSubTotal(itemInDb, item.getQuantity()));
        invoiceItem.setDiscountPerUnite(calculateDiscountPerUnite(itemInDb));
        invoiceItem.setSubtotal(calculateSubTotal(itemInDb, item.getQuantity()));

        if (invoice.getInvoiceStatus().equals(InvoiceStatus.PAID)) {
            Integer quantityToReduce = invoiceItem.getInvoiceQuantity();
            itemService.reduceItemQuantity(itemInDb, quantityToReduce);
        }
        return invoiceItemRepository.save(invoiceItem);
    }

    private BigDecimal calculateDiscountPerUnite(Item itemInDb) {
        BigDecimal itemPrice = itemInDb.getPrice();
        BigDecimal discountPercentage = itemInDb.getDiscountPercentage();

        BigDecimal discountAmount = itemPrice
                .multiply(discountPercentage)
                .divide(BigDecimal.valueOf(100));
        return discountAmount;
    }

    @Override
    public void deleteInvoiceItemsByInvoiceId(Long id) {
        invoiceItemRepository.deleteAllByInvoice_Id(id);
    }

    @Override
    public void resetItemQuantity(List<InvoiceItem> invoiceItemList) {
        for (InvoiceItem invoiceItem : invoiceItemList) {
            Item item = invoiceItem.getItem();
            Integer quantityToAdd = invoiceItem.getInvoiceQuantity();
            itemService.resetStockQuantity(item, quantityToAdd);
        }
    }

    private void createPayment(Invoice invoice) {

    }

    private BigDecimal calculateDiscountSubTotal(Item itemInDb, int quantity) {
        BigDecimal itemPrice = itemInDb.getPrice();
        BigDecimal discountPercentage = itemInDb.getDiscountPercentage();

        BigDecimal discountAmount = itemPrice
                .multiply(discountPercentage)
                .divide(BigDecimal.valueOf(100));

        BigDecimal discountSubTotal = discountAmount
                .multiply(BigDecimal.valueOf(quantity));

        return discountSubTotal;
    }

    private BigDecimal calculateUnitePrice(Item itemInDb) {
        BigDecimal itemPrice = itemInDb.getPrice();
        BigDecimal discountPercentage = itemInDb.getDiscountPercentage();

        BigDecimal discountAmount = itemPrice
                .multiply(discountPercentage)
                .divide(BigDecimal.valueOf(100));

        BigDecimal unitePrice = itemPrice.subtract(discountAmount);
        return unitePrice;
    }

    private BigDecimal calculateSubTotal(Item itemInDb, int quantity) {
        BigDecimal itemPrice = itemInDb.getPrice();
        BigDecimal discountPercentage = itemInDb.getDiscountPercentage();

        BigDecimal discountAmount = itemPrice
                .multiply(discountPercentage)
                .divide(BigDecimal.valueOf(100));

        BigDecimal subTotal = itemPrice.subtract(discountAmount)
                .multiply(BigDecimal.valueOf(quantity));
        return subTotal;
    }
}
