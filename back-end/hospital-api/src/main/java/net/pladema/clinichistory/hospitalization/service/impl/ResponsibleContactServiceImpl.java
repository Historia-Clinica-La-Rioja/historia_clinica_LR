package net.pladema.clinichistory.hospitalization.service.impl;

import net.pladema.clinichistory.hospitalization.repository.ResponsibleContactRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.ResponsibleContact;
import net.pladema.clinichistory.hospitalization.service.ResponsibleContactService;
import net.pladema.clinichistory.hospitalization.service.domain.ResponsibleContactBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ResponsibleContactServiceImpl implements ResponsibleContactService {

    private static final Logger LOG = LoggerFactory.getLogger(ResponsibleContactServiceImpl.class);

    private final ResponsibleContactRepository responsibleContactRepository;

    public ResponsibleContactServiceImpl(ResponsibleContactRepository responsibleContactRepository) {
        this.responsibleContactRepository = responsibleContactRepository;
    }

    @Override
    public ResponsibleContactBo addResponsibleContact(ResponsibleContactBo contact, Integer internmentEpisodeId) {
        if (contact != null) {
            LOG.debug("Input parameters -> contact {}, id {}", contact, internmentEpisodeId);
            ResponsibleContact rcToSave = new ResponsibleContact(contact.getFullName(), contact.getPhoneNumber(), contact.getRelationship());
            rcToSave.setInternmentEpisodeId(internmentEpisodeId);
            ResponsibleContact saved = responsibleContactRepository.save(rcToSave);
            ResponsibleContactBo result = new ResponsibleContactBo(saved);
            LOG.debug("Output -> {}", result);
            return result;
        }
        return null;
    }

}
