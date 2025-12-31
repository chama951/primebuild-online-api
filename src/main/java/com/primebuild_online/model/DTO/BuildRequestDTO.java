package com.primebuild_online.model.DTO;

import com.primebuild_online.model.BuildItem;
import com.primebuild_online.model.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildRequestDTO {
    private Double totalPrice;
    private LocalDateTime createdDate;
    private LocalDateTime lastModified;
    private String buildStatus;
    private List<BuildItem> buildItems;
    private List<Item> items;
}
