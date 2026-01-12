package com.primebuild_online.service;

import com.primebuild_online.model.Build;
import com.primebuild_online.model.DTO.BuildReqDTO;

import java.util.List;

public interface BuildService {

    Build saveBuild(BuildReqDTO buildRequest);

    Build updateBuild(BuildReqDTO buildReqDTO, Long id);

    List<Build> getAllBuild();

    void deleteBuild(long id);

    Build getBuildById(long id);

}
