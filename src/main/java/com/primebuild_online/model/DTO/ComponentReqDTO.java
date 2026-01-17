package com.primebuild_online.model.DTO;

import com.primebuild_online.model.FeatureType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComponentReqDTO {
    private Long id;
    private String componentName;
    private boolean buildComponent;
    private Integer buildPriority;
}
