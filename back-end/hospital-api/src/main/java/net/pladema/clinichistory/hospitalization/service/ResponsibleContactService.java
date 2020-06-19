package net.pladema.clinichistory.hospitalization.service;

import net.pladema.clinichistory.hospitalization.service.domain.ResponsibleContactBo;

public interface ResponsibleContactService {

    ResponsibleContactBo addResponsibleContact(ResponsibleContactBo contact, Integer internmentEpisodeId);

}
