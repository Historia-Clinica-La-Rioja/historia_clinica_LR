package net.pladema.establishment.controller.service;

import java.time.ZoneId;

public interface InstitutionExternalService {

    ZoneId getTimezone(Integer institutionId);

}
