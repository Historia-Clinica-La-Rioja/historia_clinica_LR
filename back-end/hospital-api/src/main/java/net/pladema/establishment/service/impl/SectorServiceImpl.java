package net.pladema.establishment.service.impl;

import lombok.RequiredArgsConstructor;
import net.pladema.establishment.repository.SectorRepository;
import net.pladema.establishment.repository.entity.Sector;
import net.pladema.establishment.service.SectorService;

import net.pladema.establishment.service.domain.SectorBO;

import net.pladema.medicalconsultation.diary.repository.entity.Diary;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public  class SectorServiceImpl implements SectorService {

	private final SectorRepository sectorRepository;

	private static final Logger LOG = LoggerFactory.getLogger(SectorServiceImpl.class);


	private static final String OUTPUT = "Output -> {}";

	public SectorServiceImpl(SectorRepository sectorRepository){
		this.sectorRepository = sectorRepository;
	}

	@Override
	public List<SectorBO> getSectorOfType(Integer institutionId, Short sectorTypeId){

		LOG.debug("Input parameter ->, institutionId {}, sectorTypeId {}", institutionId, sectorTypeId);
		List <Sector> sectors = sectorRepository.getSectorsOfTypeByInstitution(institutionId, sectorTypeId);
		List<SectorBO> result = sectors.stream().map(this::createSectorBoInstance).collect(Collectors.toList());
		LOG.trace(OUTPUT, result);
		return result;

	}

	private SectorBO createSectorBoInstance(Sector sector) {
		LOG.debug("Input parameters -> Sector {}", sector);
		SectorBO result = new SectorBO();
		result.setId(sector.getId());
		result.setSectorId(sector.getSectorId());
		result.setDescription(sector.getDescription());
		result.setAgeGroupId(sector.getAgeGroupId());
		result.setCareTypeId(sector.getCareTypeId());
		result.setSectorOrganizationId(sector.getSectorOrganizationId());
		result.setHospitalizationTypeId(sector.getHospitalizationTypeId());
		result.setSectorTypeId(sector.getSectorTypeId());
		result.setInstitutionId(sector.getInstitutionId());
		LOG.debug(OUTPUT, result);
		return result;
	}
}
