package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Component;
import com.primebuild_online.repository.ComponentRepository;
import com.primebuild_online.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComponentServiceImpl implements ComponentService {

    @Autowired
    private ComponentRepository componentRepository;

    @Override
    public Component saveComponent(Component component){
        return componentRepository.save(component);
    }

    @Override
    public List<Component> getAllComponent(){
        return componentRepository.findAll();
    }

    @Override
    public Component getComponentById(long id) {
        Optional<Component> component = componentRepository.findById(id);
        if (component.isPresent()) {
            return component.get();
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public Component updateComponent(Component component, long id) {
        Component existingComponent = componentRepository.findById(id).orElseThrow(RuntimeException::new);
        existingComponent.setCompName(component.getCompName());
        componentRepository.save(existingComponent);
        return existingComponent;
    }

    @Override
    public void deleteComponent(long id){
        componentRepository.findById(id).orElseThrow(RuntimeException::new);
        componentRepository.deleteById(id);
    }
}
