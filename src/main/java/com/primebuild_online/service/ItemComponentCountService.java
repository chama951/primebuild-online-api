package com.primebuild_online.service;

import com.primebuild_online.model.DTO.ItemComponentCountDTO;
import com.primebuild_online.model.ItemComponentCount;

import java.util.List;
import java.util.Optional;

public interface ItemComponentCountService {

    ItemComponentCount saveItemComponentCount(ItemComponentCountDTO itemComponentCountDTO);

    ItemComponentCount updateItemComponentCount(ItemComponentCountDTO itemComponentCountDTO, long id);

    ItemComponentCount getItemComponentCountById(long id);

    List<ItemComponentCount> getAllItemComponentCount();

    void deleteItemComponentCount(long id);

    List<ItemComponentCount> getByItem(Long itemId);

    Optional<ItemComponentCount> getByItemAndComponent(Long itemId, Long componentId);
}
