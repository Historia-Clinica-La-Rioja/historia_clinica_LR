package net.pladema.establishment.controller.service;

import net.pladema.establishment.controller.dto.InstitutionDto;

import java.time.ZoneId;

public interface InstitutionExternalService {

    ZoneId getTimezone(Integer institutionId);

}
