package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.service.documents.anamnesis.UpdateAnamnesisService;
import net.pladema.internation.service.domain.Anamnesis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UpdateAnamnesisServiceImpl implements UpdateAnamnesisService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public UpdateAnamnesisServiceImpl() {
    }


    @Override
    public Anamnesis updateAnanmesisDocument(Anamnesis anamnesis) {
        LOG.debug("Input parameters {}", anamnesis);
        return null;
    }

}
