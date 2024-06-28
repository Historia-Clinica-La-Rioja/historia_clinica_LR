package net.pladema.establishment.controller.service;

import java.util.Optional;

import net.pladema.establishment.controller.dto.BedDto;
import net.pladema.establishment.repository.entity.Bed;

public interface BedExternalService {

	BedDto updateBedStatusOccupied(Integer id);
    Optional<Bed> freeBed(Integer bedId);
    boolean isBedFreeAndAvailable(Integer bedId);
    
}
