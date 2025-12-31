package com.primebuild_online.service;

import com.primebuild_online.model.Component;
import com.primebuild_online.model.Manufacturer;

import java.util.List;

public interface ManufacturerService {

    Manufacturer saveManufacturer(Manufacturer manufacturer);

    List<Manufacturer> getAllManufacturers();

    Manufacturer updateManufacturer(Manufacturer manufacturer, long id);

    Manufacturer getManufacturerById(long id);

    void deleteManufacturer(long id);
}
