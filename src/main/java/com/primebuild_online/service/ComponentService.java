package com.primebuild_online.service;

import com.primebuild_online.model.Component;
import com.primebuild_online.model.DTO.ComponentReqDTO;

import java.util.List;

public interface ComponentService {

    Component saveComponentReq(ComponentReqDTO componentReqDTO);

    List<Component> getAllComponent();

    Component getComponentById(Long id);

    Component updateComponentReq(ComponentReqDTO componentReqDTO, Long id);

    void deleteComponent(Long id);
}
