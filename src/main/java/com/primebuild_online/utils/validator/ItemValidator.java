package com.primebuild_online.utils.validator;

import com.primebuild_online.model.Item;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Component
public class ItemValidator {
    public void validate(Item item) {

        if (!StringUtils.hasText(item.getItemName())) {
            throw new PrimeBuildException(
                    "Item name must not be empty",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (item.getQuantity() == null || item.getQuantity() < 0) {
            throw new PrimeBuildException(
                    "Quantity must be zero or greater",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (item.getPrice() == null ||
                item.getPrice().compareTo(BigDecimal.ZERO) <= 0) {

            throw new PrimeBuildException(
                    "Price must be greater than zero",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (item.getDiscountPercentage() != null) {

            if (item.getDiscountPercentage().compareTo(BigDecimal.ZERO) < 0 ||
                    item.getDiscountPercentage().compareTo(BigDecimal.valueOf(100)) > 0) {

                throw new PrimeBuildException(
                        "Discount percentage must be between 0 and 100",
                        HttpStatus.BAD_REQUEST
                );
            }
        }

        if (item.getPowerConsumption() < 0) {
            throw new PrimeBuildException(
                    "Power consumption cannot be negative",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (item.getComponent() == null ||
                item.getComponent().getId() == null) {

            throw new PrimeBuildException(
                    "Component must be selected",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (item.getManufacturer() == null ||
                item.getManufacturer().getId() == null) {

            throw new PrimeBuildException(
                    "Manufacturer must be selected",
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
