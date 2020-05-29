package net.pladema.establishment.service.impl;

import net.pladema.establishment.controller.dto.BedDto;
import net.pladema.establishment.controller.mapper.BedMapper;
import net.pladema.establishment.repository.BedRepository;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.service.BedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
 
@Service
public class BedServiceImpl implements BedService {

    private static final Logger LOG = LoggerFactory.getLogger(BedServiceImpl.class);

	private static final boolean AVAILABLE = true;

    private BedRepository bedRepository;

    private BedMapper bedMapper;

    public BedServiceImpl(BedRepository bedRepository, BedMapper bedMapper) {
        this.bedRepository = bedRepository;
        this.bedMapper = bedMapper;
    }

    @Override
    public BedDto updateBedStatusOccupied(Integer id) {
        LOG.debug("Input parameters -> BedId {}", id);
        Bed bedToUpdate = bedRepository.getOne(id);
        bedToUpdate.setFree(false);
        Bed saved = bedRepository.save(bedToUpdate);
        BedDto result = bedMapper.toBedDto(saved);
        LOG.debug("Output -> {}", result);
        return result;
    }

	@Override
	public Optional<Bed> freeBed(Integer bedId) {
		Optional<Bed> bedOpt = bedRepository.findById(bedId);
		bedOpt.ifPresent(bed -> {
			bed.setAvailable(AVAILABLE);
			bed.setFree(AVAILABLE);
			bedRepository.save(bed);
		});
		return bedOpt;
	}
	
}
