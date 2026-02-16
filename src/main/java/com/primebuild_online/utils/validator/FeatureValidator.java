package com.primebuild_online.utils.validator;

import com.primebuild_online.model.Feature;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class FeatureValidator {
    public void validate(Feature feature) {

        if (!StringUtils.hasText(feature.getFeatureName())) {
            throw new PrimeBuildException(
                    "Feature name must not be empty",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (feature.getFeatureType() == null ||
                feature.getFeatureType().getId() == null) {

            throw new PrimeBuildException(
                    "Feature type must be selected",
                    HttpStatus.BAD_REQUEST
            );
        }

    }
}
