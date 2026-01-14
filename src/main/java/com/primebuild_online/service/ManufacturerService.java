package com.primebuild_online.service;

import com.primebuild_online.model.DTO.ManufacturerDTO;
import com.primebuild_online.model.Manufacturer;

import java.util.List;

public interface ManufacturerService {

    Manufacturer saveManufacturerDTO(ManufacturerDTO manufacturerDTO);

    List<Manufacturer> getAllManufacturers();

    Manufacturer updateManufacturerReq(ManufacturerDTO manufacturerDTO, long id);

    Manufacturer getManufacturerById(long id);

    void deleteManufacturer(long id);
}
