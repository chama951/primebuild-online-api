package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Component;
import com.primebuild_online.model.Manufacturer;
import com.primebuild_online.repository.ManufacturerRepository;
import com.primebuild_online.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Override
    public Manufacturer saveManufacturer(Manufacturer manufacturer) {
        return manufacturerRepository.save(manufacturer);
    }

    @Override
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }

    @Override
    public Manufacturer updateManufacturer(Manufacturer manufacturer, long id) {
        Manufacturer existingManufacturer = manufacturerRepository.findById(id).orElseThrow(RuntimeException::new);
        existingManufacturer.setManufacturerName(manufacturer.getManufacturerName());
        manufacturerRepository.save(existingManufacturer);
        return existingManufacturer;
    }

    @Override
    public Manufacturer getManufacturerById(long id) {
        Optional<Manufacturer> Manufacturer = manufacturerRepository.findById(id);
        if (Manufacturer.isPresent()) {
            return Manufacturer.get();
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteManufacturer(long id) {
        manufacturerRepository.findById(id).orElseThrow(RuntimeException::new);
        manufacturerRepository.deleteById(id);
    }
}
