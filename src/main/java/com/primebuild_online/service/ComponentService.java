package com.primebuild_online.service;

import com.primebuild_online.model.Component;
import com.primebuild_online.model.DTO.ComponentDTO;

import java.util.List;

public interface ComponentService {

    Component saveComponent(ComponentDTO componentDTO);

    List<Component> getAllComponent();

    Component getComponentById(long id);

    Component updateComponentRequest(ComponentDTO componentDTO, long id);

    void deleteComponent(long id);
}
