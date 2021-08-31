package net.pladema.establishment.controller.service.impl;

import ar.lamansys.sgh.shared.infrastructure.input.service.institution.InstitutionInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import net.pladema.establishment.service.InstitutionService;
import net.pladema.establishment.service.domain.InstitutionBo;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
public class InstitutionExternalServiceImpl implements InstitutionExternalService, SharedInstitutionPort {

    private final InstitutionService institutionService;

    public InstitutionExternalServiceImpl(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @Override
    public ZoneId getTimezone(Integer institutionId) {
        InstitutionBo institutionBo = this.institutionService.get(institutionId);
        return ZoneId.of(institutionBo.getTimezone());
    }

    @Override
    public InstitutionInfoDto fetchInstitutionById(Integer id) {
        InstitutionBo institutionBo = this.institutionService.get(id);
        return map(institutionBo);
    }

    private InstitutionInfoDto map(InstitutionBo institutionBo) {
        InstitutionInfoDto result = new InstitutionInfoDto();
        result.setId(institutionBo.getId());
        result.setName(institutionBo.getName());
        return result;
    }
}
