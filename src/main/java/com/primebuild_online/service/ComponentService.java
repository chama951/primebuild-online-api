package com.primebuild_online.service;

import com.primebuild_online.model.Component;

import java.util.List;

public interface ComponentService {

    Component saveComponent(Component component);

    List<Component> getAllComponent();

    Component getComponentById(long id);

    Component updateComponent(Component component, long id);

    void deleteComponent(long id);
}
