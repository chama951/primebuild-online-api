package com.primebuild_online.utils.validator;

import com.primebuild_online.model.Component;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

@org.springframework.stereotype.Component
public class ComponentValidator {

    public void validate(Component component) {
        if (!StringUtils.hasText(component.getComponentName())) {
            throw new PrimeBuildException(
                    "Component name must not be empty",
                    HttpStatus.BAD_REQUEST
            );
        }
        if (component.isBuildComponent()) {

            if (component.getBuildPriority() == null) {
                throw new PrimeBuildException(
                        "Build priority is required when component is marked as build component",
                        HttpStatus.BAD_REQUEST
                );
            }

            if (component.getBuildPriority() <= 0) {
                throw new PrimeBuildException(
                        "Build priority must be greater than 0",
                        HttpStatus.BAD_REQUEST
                );
            }
        }

        if (!component.isBuildComponent() && component.getBuildPriority() != null) {
            throw new PrimeBuildException(
                    "Build priority should be null when component is not a build component",
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
