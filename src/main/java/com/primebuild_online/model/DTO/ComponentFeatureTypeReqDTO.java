package com.primebuild_online.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComponentFeatureTypeReqDTO {
    private Long componentId;
    private Long featureTypeId;
}
