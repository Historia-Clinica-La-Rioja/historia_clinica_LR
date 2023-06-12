package net.pladema.establishment.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.pladema.address.controller.service.domain.AddressBo;
import net.pladema.address.service.AddressService;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.service.InstitutionBoMapper;
import net.pladema.establishment.service.InstitutionService;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;
import net.pladema.establishment.service.domain.InstitutionBo;

@Service
public class InstitutionServiceImpl implements InstitutionService {

    private static final Logger LOG = LoggerFactory.getLogger(InstitutionServiceImpl.class);

    private static final String LOGGING_INPUT = "Input parameters -> institutionId {} ";

    private final InstitutionRepository institutionRepository;

    private final InstitutionBoMapper institutionBoMapper;

	private final AddressService addressService;

    public InstitutionServiceImpl(InstitutionRepository institutionRepository, InstitutionBoMapper institutionBoMapper, AddressService addressService) {
        this.institutionRepository = institutionRepository;
        this.institutionBoMapper = institutionBoMapper;
		this.addressService = addressService;
    }

	@Override
	public List<InstitutionBasicInfoBo> getInstitutionsByImageSectors(){
		return institutionRepository.getByDiagnosisImagesSectors();
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

	@Override
	public AddressBo getAddress(Integer institutionId) {
		return addressService.getAddressByInstitution(institutionId);
	}
}
