package net.pladema.establishment.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.service.InstitutionBoMapper;
import net.pladema.establishment.service.InstitutionService;
import net.pladema.establishment.service.domain.InstitutionBo;

@Service
public class InstitutionServiceImpl implements InstitutionService {

    private static final Logger LOG = LoggerFactory.getLogger(InstitutionServiceImpl.class);

    private static final String LOGGING_INPUT = "Input parameters -> institutionId {} ";

    private final InstitutionRepository institutionRepository;

    private final InstitutionBoMapper institutionBoMapper;

    public InstitutionServiceImpl(InstitutionRepository institutionRepository, InstitutionBoMapper institutionBoMapper) {
        this.institutionRepository = institutionRepository;
        this.institutionBoMapper = institutionBoMapper;
    }

    @Override
    public InstitutionBo get(Integer id) {
        LOG.debug(LOGGING_INPUT, id);
        return institutionRepository.findById(id)
                .map(institutionBoMapper::toInstitutionBo)
                .orElse(null);
    }

	@Override
	public InstitutionBo get(String sisaCode) {
		LOG.debug(LOGGING_INPUT, sisaCode);
		return institutionRepository.findBySisaCode(sisaCode)
				.map(institutionBoMapper::toInstitutionBo)
				.orElse(null);
	}
}
