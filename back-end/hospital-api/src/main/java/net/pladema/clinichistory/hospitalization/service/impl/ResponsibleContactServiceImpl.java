package net.pladema.clinichistory.hospitalization.service.impl;

import net.pladema.clinichistory.hospitalization.controller.dto.ResponsibleContactDto;
import net.pladema.clinichistory.hospitalization.controller.mapper.ResponsibleContactMapper;
import net.pladema.clinichistory.hospitalization.repository.ResponsibleContactRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.ResponsibleContact;
import net.pladema.clinichistory.hospitalization.service.ResponsibleContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ResponsibleContactServiceImpl implements ResponsibleContactService {

    private static final Logger LOG = LoggerFactory.getLogger(ResponsibleContactServiceImpl.class);

    private final ResponsibleContactRepository responsibleContactRepository;

    private final ResponsibleContactMapper responsibleContactMapper;

    public ResponsibleContactServiceImpl(ResponsibleContactRepository responsibleContactRepository, ResponsibleContactMapper responsibleContactMapper) {
        this.responsibleContactRepository = responsibleContactRepository;
        this.responsibleContactMapper = responsibleContactMapper;
    }

    @Override
    public ResponsibleContactDto addResponsibleContact(ResponsibleContactDto contact, Integer internmentEpisodeId) {
        if (contact != null) {
            LOG.debug("Input parameters -> contact {}, id {}", contact, internmentEpisodeId);
            ResponsibleContact rcToSave = responsibleContactMapper.toResponsibleContact(contact);
            rcToSave.setInternmentEpisodeId(internmentEpisodeId);
            ResponsibleContact saved = responsibleContactRepository.save(rcToSave);
            ResponsibleContactDto result = responsibleContactMapper.toResponsibleContactDto(saved);
            LOG.debug("Output -> {}", result);
            return result;
        }
        return null;
    }
}
