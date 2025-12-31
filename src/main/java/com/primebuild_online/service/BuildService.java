package com.primebuild_online.service;

import com.primebuild_online.model.Build;
import com.primebuild_online.model.DTO.BuildRequestDTO;
import com.primebuild_online.model.DTO.ItemListDTO;
import com.primebuild_online.model.Item;

import java.util.List;

public interface BuildService {

    Build saveBuild(BuildRequestDTO buildRequest);

    Build updateBuild(BuildRequestDTO buildRequestDTO, long id);

    List<Build> getAllBuild();

    void deleteBuild(long id);

    Build getBuildById(long id);

    Build addNewItems(ItemListDTO items, long id);
}
