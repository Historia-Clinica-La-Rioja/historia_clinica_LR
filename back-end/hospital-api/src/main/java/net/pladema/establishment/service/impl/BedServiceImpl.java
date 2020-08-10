package net.pladema.establishment.service.impl;

import net.pladema.clinichistory.hospitalization.controller.externalservice.InternmentEpisodeExternalService;
import net.pladema.establishment.repository.BedRepository;
import net.pladema.establishment.repository.HistoricPatientBedRelocationRepository;
import net.pladema.establishment.repository.domain.BedInfoVo;
import net.pladema.establishment.repository.domain.BedSummaryVo;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.repository.entity.HistoricPatientBedRelocation;
import net.pladema.establishment.service.BedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BedServiceImpl implements BedService {

	private static final Logger LOG = LoggerFactory.getLogger(BedServiceImpl.class);

	private static final boolean AVAILABLE = true;
	public static final String OUTPUT = "Output -> {}";

	private BedRepository bedRepository;

	private HistoricPatientBedRelocationRepository historicPatientBedRelocationRepository;

	private InternmentEpisodeExternalService internmentEpisodeExtService;

	public BedServiceImpl(BedRepository bedRepository, HistoricPatientBedRelocationRepository historicPatientBedRelocationRepository,
			InternmentEpisodeExternalService internmentEpisodeExtService) {
		this.bedRepository = bedRepository;
		this.historicPatientBedRelocationRepository = historicPatientBedRelocationRepository;
		this.internmentEpisodeExtService = internmentEpisodeExtService;
	}

	@Override
	public Bed updateBedStatusOccupied(Integer id) {
		LOG.debug("Input parameters -> BedId {}", id);
		Bed bedToUpdate = bedRepository.getOne(id);
		bedToUpdate.setFree(false);
		Bed result = bedRepository.save(bedToUpdate);
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public Optional<Bed> freeBed(Integer bedId) {
		Optional<Bed> bedOpt = bedRepository.findById(bedId);
		bedOpt.ifPresent(bed -> {
			bed.setAvailable(AVAILABLE);
			bed.setFree(AVAILABLE);
			bedRepository.save(bed);
		});
		return bedOpt;
	}

	@Override
	public List<Bed> getFreeBeds(Integer institutionId, Integer clinicalSpecialtyId) {
		LOG.debug("BedService::getFreeBedsByClinicalSpecialty-> input parameters -> institutionId {} , BedId {}",
				institutionId, clinicalSpecialtyId);
		List<Bed> result = bedRepository.getFreeBedsByClinicalSpecialty(institutionId, clinicalSpecialtyId);
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public HistoricPatientBedRelocation addPatientBedRelocation(HistoricPatientBedRelocation patientBedRelocation) {
		LOG.debug("BedService::addPatientBedRelocation-> input parameters -> PatientBedRelocation{} ", patientBedRelocation);
		if (patientBedRelocation.getOriginBedId() != null) {
			internmentEpisodeExtService.relocatePatientBed(patientBedRelocation.getInternmentEpisodeId(),
					patientBedRelocation.getDestinationBedId());
			if (patientBedRelocation.isOriginBedFree()) {
				freeBed(patientBedRelocation.getOriginBedId());
			}
		}
		updateBedStatusOccupied(patientBedRelocation.getDestinationBedId());
		HistoricPatientBedRelocation result = historicPatientBedRelocationRepository.save(patientBedRelocation);
		LOG.debug(OUTPUT, result);

		return result;
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<HistoricPatientBedRelocation> getLastPatientBedRelocation(Integer internmentEpisodeId) {
		LOG.debug("BedService::getLastPatientBedRelocation-> input parameters -> internmentEpisodeId{}", internmentEpisodeId);
		Optional<HistoricPatientBedRelocation> result = historicPatientBedRelocationRepository.getAllByInternmentEpisode(internmentEpisodeId).findFirst();
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<BedInfoVo> getBedInfo(Integer bedId) {
		LOG.debug("input parameters -> bedId {}", bedId);
		Optional<BedInfoVo> result = bedRepository.getBedInfo(bedId).findFirst();
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<BedSummaryVo> getBedSummary(Integer institutionId) {
		LOG.debug("input parameters -> institutionId {}", institutionId);
		List<BedSummaryVo> result = bedRepository.getAllBedsSummary(institutionId);
		LOG.debug(OUTPUT, result);
		return result;
	}

}
