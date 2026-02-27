package com.primebuild_online.model.DTO;

import com.primebuild_online.model.enumerations.Vendors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDataDTO {
    private Long itemId;
    private Vendors vendor;
}
