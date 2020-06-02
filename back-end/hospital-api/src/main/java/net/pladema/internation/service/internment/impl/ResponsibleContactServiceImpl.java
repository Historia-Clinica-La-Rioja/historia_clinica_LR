package net.pladema.internation.service.internment.impl;

import net.pladema.establishment.controller.dto.BedDto;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.internation.controller.internment.dto.ResponsibleContactDto;
import net.pladema.internation.controller.internment.mapper.ResponsibleContactMapper;
import net.pladema.internation.repository.internment.ResponsibleContactRepository;
import net.pladema.internation.repository.internment.domain.ResponsibleContact;
import net.pladema.internation.service.internment.ResponsibleContactService;
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
        LOG.debug("Input parameters -> contact {}, id {}", contact, internmentEpisodeId);
        ResponsibleContact RCToSave = responsibleContactMapper.toResponsibleContact(contact);
        RCToSave.setInternmentEpisodeId(internmentEpisodeId);
        ResponsibleContact saved = responsibleContactRepository.save(RCToSave);
        ResponsibleContactDto result = responsibleContactMapper.toResponsibleContactDto(saved);
        LOG.debug("Output -> {}", result);
        return result;
    }
}
