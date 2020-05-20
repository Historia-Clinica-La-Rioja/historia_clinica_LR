package net.pladema.establishment.service;

import net.pladema.establishment.controller.dto.BedDto;

public interface BedService {

    BedDto updateBedStatusOccupied(Integer id);
}
