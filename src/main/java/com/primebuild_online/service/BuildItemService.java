package com.primebuild_online.service;

import com.primebuild_online.model.BuildItem;

import java.util.List;
import java.util.Optional;

public interface BuildItemService{
    void saveBuildItem(BuildItem buildItem);

    void updateBuildItem(BuildItem buildItem, Long id);

    BuildItem findByBuildIdAndItemId(Long buildId, Long itemId);

    void deleteAllByBuildId(Long buildId);

    void deleteById(Long id);

    List<BuildItem> findAllByBuildId(Long buildId);
}
