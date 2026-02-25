package com.primebuild_online.service;

import com.primebuild_online.model.Build;
import com.primebuild_online.model.DTO.BuildReqDTO;
import com.primebuild_online.model.Item;

import java.util.List;

public interface BuildService {

    Build saveBuildReq(BuildReqDTO buildReqDTO);

    Build updateBuildReq(BuildReqDTO buildReqDTO, Long id);

    Build createBuildItems(List<Item> itemList, Build build);

    List<Build> getAllBuild();

    void deleteBuild(Long id);

    Build getBuildById(Long id);

    List<Build> getAllBuildsByCurrentUser();

    List<Build> getStaffMadeBuilds();

    List<Build> getUserDraftBuild();

    void updateBuildAtItemPriceChange(Item itemInDb);
}
