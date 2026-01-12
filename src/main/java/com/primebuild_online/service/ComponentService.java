package com.primebuild_online.service;

import com.primebuild_online.model.Component;
import com.primebuild_online.model.DTO.ComponentReqDTO;

import java.util.List;

public interface ComponentService {

    Component saveComponent(ComponentReqDTO componentReqDTO);

    List<Component> getAllComponent();

    Component getComponentById(long id);

    Component updateComponentRequest(ComponentReqDTO componentReqDTO, long id);

    void deleteComponent(long id);
}
