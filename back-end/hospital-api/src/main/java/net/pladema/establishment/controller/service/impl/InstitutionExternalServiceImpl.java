package net.pladema.establishment.controller.service.impl;

import net.pladema.establishment.controller.service.InstitutionExternalService;
import net.pladema.establishment.service.InstitutionService;
import net.pladema.establishment.service.domain.InstitutionBo;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
public class InstitutionExternalServiceImpl implements InstitutionExternalService {

    private final InstitutionService institutionService;

    public InstitutionExternalServiceImpl(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @Override
    public ZoneId getTimezone(Integer institutionId) {
        InstitutionBo institutionBo = this.institutionService.get(institutionId);
        return ZoneId.of(institutionBo.getTimezone());
    }
}
