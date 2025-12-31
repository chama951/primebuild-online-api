package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.BuildItem;
import com.primebuild_online.repository.BuildItemRepository;
import com.primebuild_online.service.BuildItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildItemServiceImpl implements BuildItemService {

    @Autowired
    private BuildItemRepository buildItemRepository;

    @Override
    public void updateBuildItem(BuildItem buildItem, long id) {
        BuildItem existingBuildItem = buildItemRepository.findById(id).orElseThrow(RuntimeException::new);
        existingBuildItem.setBuildQuantity(buildItem.getBuildQuantity());
    }
}
