package com.primebuild_online.model.DTO;
import com.primebuild_online.model.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemListReqDTO {
    private List<Item> items = new ArrayList<>();
}
