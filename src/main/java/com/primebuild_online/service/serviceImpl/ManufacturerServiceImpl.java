package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.DTO.ManufacturerDTO;
import com.primebuild_online.model.Manufacturer;
import com.primebuild_online.repository.ManufacturerRepository;
import com.primebuild_online.service.ManufacturerService;
import com.primebuild_online.utils.exception.PrimeBuildException;
import com.primebuild_online.utils.validator.ManufacturerValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;
    private final ManufacturerValidator manufacturerValidator;


    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository, ManufacturerValidator manufacturerValidator) {
        this.manufacturerRepository = manufacturerRepository;
        this.manufacturerValidator = manufacturerValidator;
    }

    @Override
    public Manufacturer saveManufacturerDTO(ManufacturerDTO manufacturerDTO) {

        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setManufacturerName(manufacturerDTO.getManufacturerName());
        if (manufacturer.getId() == null &&
                manufacturerRepository.existsByManufacturerNameIgnoreCase(manufacturer.getManufacturerName())) {
            throw new PrimeBuildException(
                    "Manufacturer already exists",
                    HttpStatus.CONFLICT
            );
        }

        manufacturerValidator.validate(manufacturer);

        return manufacturerRepository.save(manufacturer);
    }

    @Override
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }

    @Override
    public Manufacturer updateManufacturerReq(ManufacturerDTO manufacturerDTO, Long id) {
        Manufacturer manufacturerInDb = manufacturerRepository.findById(id).orElseThrow(
                () -> new PrimeBuildException(
                        "Manufacturer not found",
                        HttpStatus.NOT_FOUND));

        manufacturerInDb.setManufacturerName(manufacturerDTO.getManufacturerName());
        if (manufacturerInDb.getId() != null &&
                manufacturerRepository.existsByManufacturerNameIgnoreCaseAndIdNot(
                        manufacturerInDb.getManufacturerName(),
                        manufacturerInDb.getId()
                )) {
            throw new PrimeBuildException(
                    "Manufacturer already exists",
                    HttpStatus.CONFLICT
            );
        }
        manufacturerValidator.validate(manufacturerInDb);
        return manufacturerRepository.save(manufacturerInDb);
    }

    @Override
    public Manufacturer getManufacturerById(Long id) {
        Optional<Manufacturer> Manufacturer = manufacturerRepository.findById(id);
        if (Manufacturer.isPresent()) {
            return Manufacturer.get();
        } else {
            throw new PrimeBuildException(
                    "Manufacturer not found",
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteManufacturer(Long id) {
        manufacturerRepository.findById(id).orElseThrow(RuntimeException::new);
        manufacturerRepository.deleteById(id);
    }
}
