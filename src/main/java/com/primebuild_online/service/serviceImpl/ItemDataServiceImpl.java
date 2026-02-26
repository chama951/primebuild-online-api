package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.DTO.ScrapedProduct;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.ItemData;
import com.primebuild_online.model.enumerations.Vendors;
import com.primebuild_online.repository.ItemDataRepository;
import com.primebuild_online.service.ItemDataService;
import com.primebuild_online.service.VendorItemDataService;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ItemDataServiceImpl implements ItemDataService {
    private final ItemDataRepository itemDataRepository;
    private final VendorItemDataService vendorItemDataService;

    public ItemDataServiceImpl(ItemDataRepository itemDataRepository,
                               @Lazy VendorItemDataService vendorItemDataService) {
        this.itemDataRepository = itemDataRepository;
        this.vendorItemDataService = vendorItemDataService;
    }

    @Override
    public List<ScrapedProduct> parseProducts(Element productList) {

        List<ScrapedProduct> products = new ArrayList<>();

        Elements items =
                productList.select("li.ty-catPage-productListItem");

        for (Element item : items) {

            String name = null;
            String price = null;
            String url = null;

            Element title =
                    item.selectFirst(".ty-productBlock-title h1");
            if (title != null)
                name = title.text();

            Element priceEl =
                    item.selectFirst(".ty-productBlock-price-retail");
            if (priceEl != null)
                price = priceEl.text();

            Element link =
                    item.selectFirst("a");
            if (link != null)
                url = link.attr("href");

            if (name != null && price != null) {
                products.add(new ScrapedProduct(name, price, url));
            }
        }

        return products;

    }

    @Override
    public ScrapedProduct findMatch(
            List<ScrapedProduct> products,
            String searchTerm,
            String manufacturerName
    ) {

        String normalizedSearch = normalize(searchTerm);
        String searchModel = extractModel(normalizedSearch);

        ScrapedProduct exactMatch = null;
        ScrapedProduct fallbackMatch = null;

        for (ScrapedProduct product : products) {

            if (product.getName() == null)
                continue;

            String normalizedProduct =
                    normalize(product.getName());

            String productModel =
                    extractModel(normalizedProduct);

            if (searchModel != null && productModel != null) {

                if (productModel.equals(searchModel)) {
                    exactMatch = product;
                    break;
                }

                if (productModel.startsWith(searchModel)) {
                    fallbackMatch = product;
                }
            }
        }

        if (exactMatch != null)
            return exactMatch;

        return fallbackMatch;
    }

    private String normalize(String text) {

        return text.toLowerCase()
                .replaceAll("[^a-z0-9 ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private String extractModel(String text) {

        Pattern pattern =
                Pattern.compile("\\b\\d{4,5}[a-z]?\\b");

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group();
        }

        return null;
    }

    @Override
    public BigDecimal extractPrice(String rawPrice) {

        if (rawPrice == null)
            return BigDecimal.ZERO;

        String cleaned =
                rawPrice.replaceAll("[^0-9.]", "");

        if (cleaned.isEmpty())
            return BigDecimal.ZERO;

        return new BigDecimal(cleaned);
    }

    @Override
    public void buildAndSave(
            Item item,
            ScrapedProduct product,
            Vendors vendor
    ) {

        BigDecimal vendorPrice = extractPrice(product.getPrice());

        Optional<ItemData> existingOpt =
                itemDataRepository.findByVendorAndItemAndVendorPrice(
                        vendor,
                        item,
                        new BigDecimal(product.getPrice().replace(",", ""))
                );

        ItemData itemData;

        BigDecimal percentage = BigDecimal.ZERO;

        if (item.getPrice() != null && item.getPrice().compareTo(BigDecimal.ZERO) != 0) {

            percentage = vendorPrice
                    .subtract(item.getPrice())
                    .divide(item.getPrice(), 6, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        if (existingOpt.isPresent()) {
            itemData = existingOpt.get();
            itemData.setVendorPrice(vendorPrice);
            itemData.setOurPrice(item.getPrice());
            itemData.setDiscountPercentage(percentage);
            itemData.setRecordedAt(LocalDateTime.now());
        } else {
            itemData = new ItemData();
            itemData.setItem(item);
            itemData.setVendor(vendor);
            itemData.setVendorPrice(vendorPrice);
            itemData.setOurPrice(item.getPrice());
            itemData.setDiscountPercentage(percentage);
            itemData.setRecordedAt(LocalDateTime.now());
        }
        itemDataRepository.save(itemData);
    }


    @Override
    public List<ItemData> nanotekItemData(Long id, Vendors vendor) {
        vendorItemDataService.nanotekItemData(id);
        return itemDataRepository.findByVendorAndItem_id(
                vendor,
                id
        );
    }

    @Override
    public void saveItemData(Long id) {
        vendorItemDataService.nanotekItemData(id);
    }

    @Override
    public List<ItemData> getItemDataByItemId(Long id) {
        return itemDataRepository.findAllByItem_Id(id);
    }

}
