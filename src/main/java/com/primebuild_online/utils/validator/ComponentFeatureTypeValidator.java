package com.primebuild_online.utils.validator;

import com.primebuild_online.model.ComponentFeatureType;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ComponentFeatureTypeValidator {
    public void validate(ComponentFeatureType componentFeatureType) {

        if (componentFeatureType.getComponent() == null ||
                componentFeatureType.getComponent().getId() == null) {

            throw new PrimeBuildException(
                    "Component must be selected",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (componentFeatureType.getFeatureType() == null ||
                componentFeatureType.getFeatureType().getId() == null) {

            throw new PrimeBuildException(
                    "Feature type must be selected",
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
