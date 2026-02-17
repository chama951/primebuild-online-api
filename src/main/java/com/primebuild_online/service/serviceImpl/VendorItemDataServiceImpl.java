package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.DTO.ScrapedProduct;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.ItemData;
import com.primebuild_online.model.enumerations.Vendors;
import com.primebuild_online.repository.ItemDataRepository;
import com.primebuild_online.service.ItemDataHistoryService;
import com.primebuild_online.service.ItemDataService;
import com.primebuild_online.service.VendorItemDataService;
import com.primebuild_online.service.ItemService;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class VendorItemDataServiceImpl implements VendorItemDataService {
    private final ItemService itemService;
    private final ItemDataHistoryService itemDataHistoryService;
    private final ItemDataService itemDataService;

    public VendorItemDataServiceImpl(ItemDataRepository itemDataRepository,
                                     ItemService itemService,
                                     ItemDataHistoryService itemDataHistoryService, ItemDataService itemDataService) {
        this.itemService = itemService;
        this.itemDataHistoryService = itemDataHistoryService;
        this.itemDataService = itemDataService;
    }

    private static final String BASE_URL =
            "https://www.nanotek.lk/category/processor?page=";

    private static final int MAX_PAGES = 5;


    @Override
    public ItemData nanotekItemData(Long itemId) {

        Item item = itemService.getItemById(itemId);

        ItemData itemData = new ItemData();
        if (item == null || item.getItemName() == null) {
            throw new PrimeBuildException(
                    "Item or item name cannot be null",
                    HttpStatus.NOT_FOUND);
        }

        try {

            for (int page = 1; page <= MAX_PAGES; page++) {

                Document doc = Jsoup.connect(BASE_URL + page)
                        .userAgent("Mozilla/5.0")
                        .timeout(10000)
                        .get();

                //Most Important Part
                Element productList =
                        doc.selectFirst("ul.ty-catPage-product-list");

                if (productList == null)
                    continue;

                List<ScrapedProduct> products =
                        itemDataService.parseProducts(productList);

                if (products.isEmpty())
                    break;

                ScrapedProduct match =
                        itemDataService.findMatch(products, item.getItemName());

                if (match != null) {
                    itemData = itemDataService.buildAndSave(item, match, Vendors.NANOTEK);

                    itemDataHistoryService.
                            saveItemDataHistory(
                                    item,
                                    itemDataService.extractPrice(match.getPrice()),
                                    Vendors.NANOTEK);
                    return itemData;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return itemData;
    }


}
