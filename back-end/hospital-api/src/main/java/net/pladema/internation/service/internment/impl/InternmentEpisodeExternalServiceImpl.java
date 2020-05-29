package net.pladema.internation.service.internment.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.pladema.internation.service.internment.InternmentEpisodeExternalService;
import net.pladema.internation.service.internment.InternmentEpisodeService;

@Service
public class InternmentEpisodeExternalServiceImpl implements InternmentEpisodeExternalService {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentEpisodeExternalServiceImpl.class);
	
	InternmentEpisodeService internmentEpisodeService;

	public InternmentEpisodeExternalServiceImpl(InternmentEpisodeService internmentEpisodeService) {
		super();
		this.internmentEpisodeService = internmentEpisodeService;
	}

	@Override
	public Boolean existsActiveForBedId(Integer bedId) {
        LOG.debug("Input parameters -> bedId {}", bedId);
		Boolean ret = internmentEpisodeService.existsActiveForBedId(bedId);
		LOG.debug("Output -> {}", ret);
		return ret;
	}
	

}
