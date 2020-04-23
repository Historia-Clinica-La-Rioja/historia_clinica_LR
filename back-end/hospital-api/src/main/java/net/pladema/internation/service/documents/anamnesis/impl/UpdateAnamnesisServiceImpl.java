package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.service.documents.anamnesis.UpdateAnamnesisService;
import net.pladema.internation.service.domain.Anamnesis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UpdateAnamnesisServiceImpl implements UpdateAnamnesisService {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateAnamnesisServiceImpl.class);

    public UpdateAnamnesisServiceImpl() {
    }


    @Override
    public Anamnesis updateAnanmesisDocument(Integer IntermentEpisodeId, Integer patientId, Anamnesis anamnesis) {
        LOG.debug("Input parameters -> intermentEpisodeId {}, patientId {}, anamnesis {}", anamnesis);
        return null;
    }

}
