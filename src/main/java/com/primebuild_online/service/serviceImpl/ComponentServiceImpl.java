package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Component;
import com.primebuild_online.model.ComponentFeatureType;
import com.primebuild_online.model.DTO.ComponentReqDTO;
import com.primebuild_online.model.FeatureType;
import com.primebuild_online.repository.ComponentRepository;
import com.primebuild_online.service.ComponentFeatureTypeService;
import com.primebuild_online.service.ComponentService;
import com.primebuild_online.service.FeatureTypeService;
import com.primebuild_online.service.ItemService;
import com.primebuild_online.utils.exception.PrimeBuildException;
import com.primebuild_online.utils.validator.ComponentValidator;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComponentServiceImpl implements ComponentService {
    private final ComponentRepository componentRepository;
    private final ComponentValidator componentValidator;
    private final ItemService itemService;


    public ComponentServiceImpl(
            ComponentRepository componentRepository,
            ComponentValidator componentValidator,
            @Lazy ItemService itemService) {
        this.componentRepository = componentRepository;
        this.componentValidator = componentValidator;
        this.itemService = itemService;
    }

    @Override
    public Component saveComponentReq(ComponentReqDTO componentReqDTO) {
        Component newComponent = new Component();
        return componentRepository.save(setComponentValues(componentReqDTO, newComponent));
    }

    private Component setComponentValues(ComponentReqDTO componentReqDTO, Component component) {
        component.setComponentName(componentReqDTO.getComponentName());
        if (component.getId() == null &&
                componentRepository.existsByComponentNameIgnoreCase(
                        component.getComponentName())) {

            throw new PrimeBuildException(
                    "Component already exists",
                    HttpStatus.CONFLICT
            );
        }
        component.setBuildComponent(componentReqDTO.isBuildComponent());
        component.setBuildPriority(componentReqDTO.getBuildPriority());
        if (!componentReqDTO.isBuildComponent()) {
            component.setBuildPriority(null);
        }
        component.setPowerSource(componentReqDTO.isPowerSource());
        componentValidator.validate(component);
        return component;
    }

    @Override
    public List<Component> getAllComponent() {
        return componentRepository.findAll();
    }

    @Override
    public Component getComponentById(Long id) {
        Optional<Component> component = componentRepository.findById(id);
        if (component.isPresent()) {
            return component.get();
        } else {
            throw new PrimeBuildException(
                    "Component not found",
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Component updateComponentReq(ComponentReqDTO componentReqDTO, Long id) {
        Component componentInDb = componentRepository.findById(id).orElseThrow(
                () -> new PrimeBuildException(
                        "Component not found",
                        HttpStatus.NOT_FOUND));
        componentInDb = setComponentValues(componentReqDTO, componentInDb);
        return componentRepository.save(componentInDb);
    }

    @Override
    public void deleteComponent(Long id) {
        if (itemService.existsItemByComponent(id)) {
            throw new PrimeBuildException(
                    "Component cannot be deleted while found in Items",
                    HttpStatus.CONFLICT);
        }
        componentRepository.deleteById(id);
    }

    @Override
    public List<Component> getIsBuildComponentList() {
        return componentRepository.findBuildComponentsOrderedByPriority();
    }
}
