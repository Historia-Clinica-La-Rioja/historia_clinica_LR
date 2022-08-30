package net.pladema.establishment.controller.service.impl;

import java.time.ZoneId;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.institution.InstitutionInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import net.pladema.establishment.service.InstitutionService;
import net.pladema.establishment.service.domain.InstitutionBo;

@Service
public class InstitutionExternalServiceImpl implements InstitutionExternalService, SharedInstitutionPort {

    private final InstitutionService institutionService;

    public InstitutionExternalServiceImpl(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @Override
    public ZoneId getTimezone(Integer institutionId) {
        InstitutionBo institutionBo = institutionService.get(institutionId);
        return ZoneId.of(institutionBo.getTimezone());
    }

    @Override
    public InstitutionInfoDto fetchInstitutionById(Integer id) {
        return Optional.ofNullable(institutionService.get(id))
                .map(institutionBo -> new InstitutionInfoDto(institutionBo.getId(), institutionBo.getName(), institutionBo.getSisaCode()))
                .orElse(null);
    }

	@Override
	public InstitutionInfoDto fetchInstitutionBySisaCode(String sisaCode) {
		return Optional.ofNullable(institutionService.get(sisaCode))
				.map(institutionBo -> new InstitutionInfoDto(institutionBo.getId(), institutionBo.getName(), institutionBo.getSisaCode()))
				.orElse(null);
	}

}
