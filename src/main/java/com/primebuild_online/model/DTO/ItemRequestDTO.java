package com.primebuild_online.model.DTO;

import com.primebuild_online.model.Feature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDTO {
    private String itemName;
    private Integer quantity;
    private Double price;
    private Integer componentId;
    private Integer manufacturerId;
    private List<Feature> featureList = new ArrayList<>();
}
