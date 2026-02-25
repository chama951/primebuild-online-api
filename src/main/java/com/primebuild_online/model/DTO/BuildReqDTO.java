package com.primebuild_online.model.DTO;

import com.primebuild_online.model.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildReqDTO {
    private String buildName;
    private String buildStatus;
    private boolean published;
    private List<Item> itemList = new ArrayList<>();
}
