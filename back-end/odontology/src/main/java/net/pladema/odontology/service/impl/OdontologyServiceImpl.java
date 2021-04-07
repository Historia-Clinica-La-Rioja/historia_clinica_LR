package net.pladema.odontology.service.impl;

import net.pladema.odontology.repository.OdontologyRepository;
import net.pladema.odontology.service.OdontologyService;
import org.springframework.stereotype.Service;

@Service
public class OdontologyServiceImpl implements OdontologyService {



    private final OdontologyRepository odontologyRepository;

    public OdontologyServiceImpl(OdontologyRepository odontologyRepository) {
        this.odontologyRepository = odontologyRepository;
    }

    @Override
    public String getInfo() {
        return odontologyRepository.getInfo();
    }
}
