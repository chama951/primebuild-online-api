package com.primebuild_online.utils.validator;

import com.primebuild_online.model.Manufacturer;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ManufacturerValidator {

    public void validate(Manufacturer manufacturer) {

        if (!StringUtils.hasText(manufacturer.getManufacturerName())) {
            throw new PrimeBuildException(
                    "Manufacturer name must not be empty",
                    HttpStatus.BAD_REQUEST
            );
        }

    }
}