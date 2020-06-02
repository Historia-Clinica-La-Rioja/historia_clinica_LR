package net.pladema.internation.service.internment;

import net.pladema.internation.controller.internment.dto.ResponsibleContactDto;

public interface ResponsibleContactService {

    ResponsibleContactDto addResponsibleContact(ResponsibleContactDto contact, Integer internmentEpisodeId);

}
