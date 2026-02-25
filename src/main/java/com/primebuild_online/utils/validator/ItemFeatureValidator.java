package com.primebuild_online.utils.validator;

import com.primebuild_online.model.ItemFeature;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ItemFeatureValidator {
    public void validate(ItemFeature itemFeature) {

        if (itemFeature == null) {
            throw new PrimeBuildException(
                    "ItemFeature must not be null",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (itemFeature.getItem() == null || itemFeature.getItem().getId() == null) {
            throw new PrimeBuildException(
                    "Item must not be null",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (itemFeature.getFeature() == null || itemFeature.getFeature().getId() == null) {
            throw new PrimeBuildException(
                    "Feature must not be null",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (itemFeature.getSlotCount() == null) {
            throw new PrimeBuildException(
                    "Slot count must not be null",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (itemFeature.getSlotCount() <= 0) {
            throw new PrimeBuildException(
                    "Slot count must be greater than 0",
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
