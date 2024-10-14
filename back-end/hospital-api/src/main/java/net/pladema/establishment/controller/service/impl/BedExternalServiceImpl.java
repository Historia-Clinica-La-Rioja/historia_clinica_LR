package net.pladema.establishment.controller.service.impl;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import net.pladema.establishment.controller.dto.BedDto;
import net.pladema.establishment.controller.mapper.BedMapper;
import net.pladema.establishment.controller.service.BedExternalService;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.service.BedService;

@Slf4j
@RequiredArgsConstructor
@Service
public class BedExternalServiceImpl implements BedExternalService {

    private final BedService bedService;
    private final BedMapper bedMapper;

    @Override
    public BedDto updateBedStatusOccupied(Integer id) {
        log.debug("Input parameters -> BedId {}", id);
        Bed saved = bedService.updateBedStatusOccupied(id);
        BedDto result = bedMapper.toBedDto(saved);
        log.debug("Output -> {}", result);
        return result;
    }

	@Override
	public Optional<Bed> freeBed(Integer bedId) {
		return bedService.freeBed(bedId);
	}

    @Override
    public boolean isBedFreeAndAvailable(Integer bedId) {
        return bedService.isBedFreeAndAvailable(bedId);
    }

}
