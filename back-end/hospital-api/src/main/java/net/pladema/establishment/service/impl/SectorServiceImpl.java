package net.pladema.establishment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.repository.SectorRepository;
import net.pladema.establishment.repository.domain.SectorOfTypeVo;
import net.pladema.establishment.service.SectorService;
import net.pladema.establishment.service.domain.AttentionPlacesQuantityBo;
import net.pladema.establishment.service.domain.SectorBO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SectorServiceImpl implements SectorService {

    private final SectorRepository sectorRepository;

    private static final String OUTPUT = "Output -> {}";

    @Override
    public List<SectorBO> getSectorOfType(Integer institutionId, Short sectorTypeId) {
        log.debug("Input parameter ->, institutionId {}, sectorTypeId {}", institutionId, sectorTypeId);
        List<SectorOfTypeVo> sectors = sectorRepository.getSectorsOfTypeByInstitution(institutionId, sectorTypeId);
        List<SectorBO> result = sectors.stream()
                .map(this::createSectorBoInstance)
                .collect(Collectors.toList());
        log.trace(OUTPUT, result);
        return result;

    }

    @Override
    public AttentionPlacesQuantityBo quantityAttentionPlacesBySectorType(Integer institutionId, Short sectorTypeId) {
        log.debug("Input parameter ->, institutionId {}, sectorTypeId {}", institutionId, sectorTypeId);
        AttentionPlacesQuantityBo result = sectorRepository.quantityAttentionPlacesBySectorType(institutionId, sectorTypeId);
        log.trace(OUTPUT, result);
        return result;
    }

    private SectorBO createSectorBoInstance(SectorOfTypeVo sector) {
        log.debug("Input parameters -> SectorOfTypeVo {}", sector);
        SectorBO result = new SectorBO();
        result.setId(sector.getId());
        result.setDescription(sector.getDescription());
        result.setInstitutionId(sector.getInstitutionId());
        result.setSectorId(sector.getSectorId());
        result.setSectorTypeId(sector.getSectorTypeId());
        result.setSectorOrganizationId(sector.getSectorOrganizationId());
        result.setAgeGroupId(sector.getAgeGroupId());
        result.setCareTypeId(sector.getCareTypeId());
        result.setHospitalizationTypeId(sector.getHospitalizationTypeId());
        result.setInformer(sector.getInformer());
        result.setHasDoctorsOffice(sector.getHasDoctorsOffice());
        log.debug(OUTPUT, result);
        return result;
    }

	@Override
	public String getSectorName(Integer sectorId) {
		log.debug("Input parameter -> sectorId {}", sectorId);
		String result = sectorRepository.getSectorName(sectorId);
		log.trace(OUTPUT, result);
		return result;
	}
}
