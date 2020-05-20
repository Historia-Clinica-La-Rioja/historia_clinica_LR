package net.pladema.establishment.controller.service;

import net.pladema.establishment.controller.dto.BedDto;

public interface BedExternalService {

    BedDto updateBedStatusOccupied(Integer id);
}
