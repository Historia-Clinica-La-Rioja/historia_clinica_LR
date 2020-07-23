package net.pladema.clinichistory.hospitalization.controller.externalservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;

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

	@Override
	public Integer relocatePatientBed(Integer internmentEpisodeId, Integer destinationBedId) {
		LOG.debug("Input parameters -> destinationBedId {}", destinationBedId);
		Integer oldBed = internmentEpisodeService.updateInternmentEpisodeBed(internmentEpisodeId, destinationBedId);
		LOG.debug("Bed relocated");
		return oldBed;
	}

}
