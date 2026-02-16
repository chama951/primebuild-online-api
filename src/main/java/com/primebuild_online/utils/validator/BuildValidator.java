package com.primebuild_online.utils.validator;

import com.primebuild_online.model.Build;
import com.primebuild_online.model.BuildItem;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class BuildValidator {

    public void validate(Build build) {

        if (build == null) {
            throw new PrimeBuildException(
                    "Build object must not be null",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (build.getBuildStatus() == null) {
            throw new PrimeBuildException(
                    "Build status must not be null",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (build.getBuildItemList() == null || build.getBuildItemList().isEmpty()) {
            throw new PrimeBuildException(
                    "Build must contain at least one item",
                    HttpStatus.BAD_REQUEST
            );
        }

        for (int i = 0; i < build.getBuildItemList().size(); i++) {
            BuildItem item = build.getBuildItemList().get(i);

            if (item.getItem() == null) {
                throw new PrimeBuildException(
                        "Item reference at index " + i + " must not be null",
                        HttpStatus.BAD_REQUEST
                );
            }

            if (item.getBuildQuantity() == null || item.getBuildQuantity() < 1) {
                throw new PrimeBuildException(
                        "Quantity for item at index " + i + " must be at least 1",
                        HttpStatus.BAD_REQUEST
                );
            }
        }
    }
}
