package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.service.documents.anamnesis.AnamnesisService;
import net.pladema.internation.service.domain.Anamnesis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AnamnesisServiceImpl implements AnamnesisService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public AnamnesisServiceImpl() {
    }

    @Override
    public Anamnesis getAnamnesis(Integer anamnesisId) {
        LOG.debug("Input parameters {}", anamnesisId);
        return null;
    }
}
