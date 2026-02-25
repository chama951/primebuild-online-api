package com.primebuild_online.utils.validator;

import com.primebuild_online.model.FeatureType;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class FeatureTypeValidator {

    public void validate(FeatureType featureType) {

        if (!StringUtils.hasText(featureType.getFeatureTypeName())) {
            throw new PrimeBuildException(
                    "Feature type name must not be empty",
                    HttpStatus.BAD_REQUEST
            );
        }

    }
}
