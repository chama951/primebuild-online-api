package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.DTO.ManufacturerDTO;
import com.primebuild_online.model.Manufacturer;
import com.primebuild_online.repository.ManufacturerRepository;
import com.primebuild_online.service.ManufacturerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public Manufacturer saveManufacturerDTO(ManufacturerDTO manufacturerDTO) {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setManufacturerName(manufacturerDTO.getManufacturerName());
        return manufacturerRepository.save(manufacturer);
    }

    @Override
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }

    @Override
    public Manufacturer updateManufacturerReq(ManufacturerDTO manufacturerDTO, Long id) {
        Manufacturer manufacturerInDb = manufacturerRepository.findById(id).orElseThrow(RuntimeException::new);
        manufacturerInDb.setManufacturerName(manufacturerDTO.getManufacturerName());
        manufacturerRepository.save(manufacturerInDb);
        return manufacturerInDb;
    }

    @Override
    public Manufacturer getManufacturerById(Long id) {
        Optional<Manufacturer> Manufacturer = manufacturerRepository.findById(id);
        if (Manufacturer.isPresent()) {
            return Manufacturer.get();
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteManufacturer(Long id) {
        manufacturerRepository.findById(id).orElseThrow(RuntimeException::new);
        manufacturerRepository.deleteById(id);
    }
}
