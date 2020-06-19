package net.pladema.clinichistory.hospitalization.service;

import net.pladema.clinichistory.hospitalization.controller.dto.ResponsibleContactDto;

public interface ResponsibleContactService {

    ResponsibleContactDto addResponsibleContact(ResponsibleContactDto contact, Integer internmentEpisodeId);

}
