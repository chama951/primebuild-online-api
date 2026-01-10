package com.primebuild_online.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeatureRequestDTO {
    private String featureName;
    private Long featureTypeId;
}
