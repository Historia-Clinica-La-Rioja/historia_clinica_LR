package net.pladema.establishment.service.impl;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.security.UserInfo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.controller.externalservice.InternmentEpisodeExternalService;
import net.pladema.establishment.repository.BedRepository;
import net.pladema.establishment.repository.BedSummaryRepository;
import net.pladema.establishment.repository.HistoricInchargeNurseBedRepository;
import net.pladema.establishment.repository.HistoricPatientBedRelocationRepository;
import net.pladema.establishment.repository.domain.BedInfoVo;
import net.pladema.establishment.repository.domain.BedSummaryVo;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.repository.entity.HistoricInchargeNurseBed;
import net.pladema.establishment.repository.entity.HistoricPatientBedRelocation;
import net.pladema.establishment.service.BedService;
import net.pladema.person.service.PersonService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BedServiceImpl implements BedService {

	private static final boolean AVAILABLE = true;
	public static final String OUTPUT = "Output -> {}";

	private final BedRepository bedRepository;
	private final BedSummaryRepository bedSummaryRepository;
	private final HistoricPatientBedRelocationRepository historicPatientBedRelocationRepository;
	private final InternmentEpisodeExternalService internmentEpisodeExtService;
	private final PersonService personService;
	private final HistoricInchargeNurseBedRepository historicInchargeNurseBedRepository;
	private final LocalDateMapper localDateMapper;

	@Override
	public Bed updateBedStatusOccupied(Integer id) {
		log.debug("Input parameters -> BedId {}", id);
		return bedRepository.findById(id).map(bedToUpdate -> {
			bedToUpdate.setFree(false);
			Bed result = bedRepository.save(bedToUpdate);
			log.debug(OUTPUT, result);
			return result;
		}).get();
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
		log.debug("BedService::getFreeBedsByClinicalSpecialty-> input parameters -> institutionId {} , BedId {}",
				institutionId, clinicalSpecialtyId);
		List<Bed> result = bedRepository.getFreeBedsByClinicalSpecialty(institutionId, clinicalSpecialtyId);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	@Transactional
	public HistoricPatientBedRelocation addPatientBedRelocation(HistoricPatientBedRelocation patientBedRelocation) {
		log.debug("BedService::addPatientBedRelocation-> input parameters -> PatientBedRelocation{} ", patientBedRelocation);
		if (patientBedRelocation.getOriginBedId() != null) {
			internmentEpisodeExtService.relocatePatientBed(patientBedRelocation.getInternmentEpisodeId(),
					patientBedRelocation.getDestinationBedId());
			if (patientBedRelocation.isOriginBedFree()) {
				freeBed(patientBedRelocation.getOriginBedId());
			}
		}
		updateBedStatusOccupied(patientBedRelocation.getDestinationBedId());
		HistoricPatientBedRelocation result = historicPatientBedRelocationRepository.save(patientBedRelocation);
		log.debug(OUTPUT, result);

		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<HistoricPatientBedRelocation> getLastPatientBedRelocation(Integer internmentEpisodeId) {
		log.debug("BedService::getLastPatientBedRelocation-> input parameters -> internmentEpisodeId{}", internmentEpisodeId);
		Optional<HistoricPatientBedRelocation> result = historicPatientBedRelocationRepository.getAllByInternmentEpisode(internmentEpisodeId).findFirst();
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<HistoricPatientBedRelocation> getBedIdByDateTime(Integer internmentEpisodeId, LocalDateTime requestDateTime) {
		log.debug("Input parameters -> internmentEpisodeId {} requestDateTime {}", internmentEpisodeId, requestDateTime);
		var patientBedRelocations = historicPatientBedRelocationRepository.getAllByInternmentEpisode(internmentEpisodeId)
				.collect(Collectors.toList());

		if (patientBedRelocations.isEmpty()) {
			log.debug(OUTPUT, "No bed relocation yet");
			return Optional.empty();
		}

		var result = patientBedRelocations.stream()
				.filter(historicPatientBedRelocation -> requestDateTime.isAfter(historicPatientBedRelocation.getRelocationDate()))
				.findFirst()
				.or(() -> {
                    Integer firstBedId = patientBedRelocations.get(patientBedRelocations.size() - 1).getOriginBedId();
					return Optional.of(new HistoricPatientBedRelocation(null, firstBedId));
				});

		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<BedInfoVo> getBedInfo(Integer bedId) {
		log.debug("input parameters -> bedId {}", bedId);
		Optional<BedInfoVo> result = bedRepository.getBedInfo(bedId).findFirst();
		if (result.isPresent() && result.get().getBedNurse() != null) {
			BedInfoVo r = result.get();
			r.getBedNurse().setFullName(personService.getCompletePersonNameById(r.getBedNurse().getPersonId()));
		}
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<BedSummaryVo> getBedSummary(Integer institutionId, Short[] sectorsType) {
		log.debug("input parameters -> institutionId {}, sectorsType {}", institutionId, sectorsType);
		List<BedSummaryVo> result = bedSummaryRepository.execute(institutionId, sectorsType);
		log.trace(OUTPUT, result);
		log.debug("Result size {}", result.size());
		return result;
	}

	@Override
	public void updateBedNurse(Integer userId, Integer bedId) {
		log.debug("input parameters -> userId {}, bedId {}", userId, bedId);
		Bed bed = bedRepository.getById(bedId);
		bed.setInchargeNurseId(userId);
		bedRepository.save(bed);
		updatePreviousHistoricInchargeNurseBed(bedId);
		historicInchargeNurseBedRepository.save(
				new HistoricInchargeNurseBed(
						userId,
						bedId,
						UserInfo.getCurrentAuditor()
				)
		);
	}

	private void updatePreviousHistoricInchargeNurseBed(Integer bedId) {
		List<HistoricInchargeNurseBed> historic = historicInchargeNurseBedRepository.getLatestHistoricInchargeNurseBedByBedId(bedId, PageRequest.of(0, 1));
		if (!historic.isEmpty()) {
			historic.get(0).setUntilDate(LocalDateTime.now());
			historicInchargeNurseBedRepository.save(historic.get(0));
		}
	}
}
