package com.primebuild_online.service;

import com.primebuild_online.model.DTO.ItemDataDTO;
import com.primebuild_online.model.DTO.ScrapedProduct;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.ItemData;
import com.primebuild_online.model.enumerations.Vendors;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

public interface ItemDataService {

    List<ScrapedProduct> parseProducts(Element productList);

    ScrapedProduct findMatch(List<ScrapedProduct> products, String itemName, String manufacturerName);

    BigDecimal extractPrice(String rawPrice);

    void buildAndSave(Item item, ScrapedProduct match, Vendors vendors);

    List<ItemData> getItemDataByVendorAndItem(Long id, Vendors vendor);

    void saveItemDataAtCreatUpdateItem(Long id);

    List<ItemData> getItemDataByItemId(Long id);

    void deleteItemDataByVendor(Long id, Vendors vendor);

    List<ItemData> saveItemDataByVendor(ItemDataDTO itemDataDTO);
}
