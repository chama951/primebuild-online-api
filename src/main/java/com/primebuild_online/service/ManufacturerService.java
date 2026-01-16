package com.primebuild_online.service;

import com.primebuild_online.model.DTO.ManufacturerDTO;
import com.primebuild_online.model.Manufacturer;

import java.util.List;

public interface ManufacturerService {

    Manufacturer saveManufacturerDTO(ManufacturerDTO manufacturerDTO);

    List<Manufacturer> getAllManufacturers();

    Manufacturer updateManufacturerReq(ManufacturerDTO manufacturerDTO, Long id);

    Manufacturer getManufacturerById(Long id);

    void deleteManufacturer(Long id);
}
