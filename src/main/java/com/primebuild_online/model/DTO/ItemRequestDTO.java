package com.primebuild_online.model.DTO;

import com.primebuild_online.model.Component;
import com.primebuild_online.model.Feature;
import com.primebuild_online.model.Manufacturer;
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
    private Component component;
    private Manufacturer manufacturer;
    private List<Feature> featureList = new ArrayList<>();
}
