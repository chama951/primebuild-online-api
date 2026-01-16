package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.BuildItem;
import com.primebuild_online.repository.BuildItemRepository;
import com.primebuild_online.service.BuildItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuildItemServiceImpl implements BuildItemService {
    private final BuildItemRepository buildItemRepository;

    public BuildItemServiceImpl(BuildItemRepository buildItemRepository) {
        this.buildItemRepository = buildItemRepository;
    }

    @Override
    public void saveBuildItem(BuildItem buildItem) {
        buildItemRepository.save(buildItem);
    }


    @Override
    public void updateBuildItem(BuildItem buildItem, long id) {
        BuildItem existingBuildItem = buildItemRepository.findById(id).orElseThrow(RuntimeException::new);
        existingBuildItem.setBuildQuantity(buildItem.getBuildQuantity());
    }

    @Override
    public BuildItem findByBuildIdAndItemId(Long buildId, Long itemId) {
        Optional<BuildItem> buildItem = buildItemRepository.findByBuildIdAndItemId(buildId, itemId);
        if (buildItem.isPresent()) {
            return buildItem.get();
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteAllByBuildId(Long buildId) {
        buildItemRepository.deleteAllByBuildId(buildId);
    }

    @Override
    public void deleteById(Long id) {
        buildItemRepository.deleteById(id);
    }

    @Override
    public List<BuildItem> findAllByBuildId(Long buildId) {
       List<BuildItem> buildItemList = buildItemRepository.findAllByBuildId(buildId);
       return buildItemList;
    }


}
