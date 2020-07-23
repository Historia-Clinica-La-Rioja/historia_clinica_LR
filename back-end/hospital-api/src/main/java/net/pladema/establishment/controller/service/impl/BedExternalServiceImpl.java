package net.pladema.establishment.controller.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.pladema.establishment.controller.dto.BedDto;
import net.pladema.establishment.controller.mapper.BedMapper;
import net.pladema.establishment.controller.service.BedExternalService;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.service.BedService;

@Service
public class BedExternalServiceImpl implements BedExternalService {

    private static final Logger LOG = LoggerFactory.getLogger(BedExternalServiceImpl.class);

    private BedService bedService;
    
    private BedMapper bedMapper;

    public BedExternalServiceImpl(BedService bedService, BedMapper bedMapper) {
        this.bedService = bedService;
        this.bedMapper = bedMapper;
    }

    @Override
    public BedDto updateBedStatusOccupied(Integer id) {
        LOG.debug("Input parameters -> BedId {}", id);
        Bed saved = bedService.updateBedStatusOccupied(id);
        BedDto result = bedMapper.toBedDto(saved);
        LOG.debug("Output -> {}", result);
        return result;
    }
    

	@Override
	public Optional<Bed> freeBed(Integer bedId) {
		return bedService.freeBed(bedId);
	}
	
    
}
