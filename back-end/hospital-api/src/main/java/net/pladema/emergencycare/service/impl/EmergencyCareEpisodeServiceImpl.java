package net.pladema.emergencycare.service.impl;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums.EPatientType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.domain.RoomBo;
import net.pladema.emergencycare.controller.exceptions.SaveEmergencyCareEpisodeException;
import net.pladema.emergencycare.controller.exceptions.SaveEmergencyCareEpisodeExceptionEnum;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.emergencycare.repository.PoliceInterventionRepository;
import net.pladema.emergencycare.repository.domain.EmergencyCareVo;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;
import net.pladema.emergencycare.repository.entity.EmergencyCareState;
import net.pladema.emergencycare.repository.entity.PoliceInterventionDetails;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.HistoricEmergencyEpisodeService;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.emergencycare.service.domain.EmergencyCareEpisodeInProgressBo;
import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;
import net.pladema.emergencycare.service.domain.PatientECEBo;
import net.pladema.emergencycare.service.domain.PoliceInterventionDetailsBo;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import net.pladema.emergencycare.triage.domain.TriageBo;
import net.pladema.emergencycare.triage.service.TriageService;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.patient.controller.service.PatientExternalService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class EmergencyCareEpisodeServiceImpl implements EmergencyCareEpisodeService {

    public static final String OUTPUT = "Output -> {}";
    private static final String WRONG_CARE_ID_EPISODE = "wrong-care-id-episode";
    private static final String CARE_EPISODE_NOT_FOUND = "El episodio de guardia no se encontró o no existe";
	private final Short WITHOUT_TRIAGE_LEVEL_ID = 6;

    private TriageService triageService;
    private PoliceInterventionRepository policeInterventionRepository;
    private EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;
    private InstitutionExternalService institutionExternalService;
    private HistoricEmergencyEpisodeService historicEmergencyEpisodeService;
	private PatientExternalService patientExternalService;

	@Override
	public EmergencyCareEpisodeInProgressBo getEmergencyCareEpisodeInProgressByInstitution(Integer institutionId, Integer patientId) {
		log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
		EmergencyCareEpisodeInProgressBo result = new EmergencyCareEpisodeInProgressBo(null, false);

        List<EmergencyCareEpisode> possibleEmergencyCareEpisodes = emergencyCareEpisodeRepository.getEmergencyCareEpisodeInProgressByInstitution(institutionId, patientId);

        this.logIfThereAreMoreThanOneEmergencyCareEpisodeInOneInstitution(possibleEmergencyCareEpisodes);

        possibleEmergencyCareEpisodes.stream()
                .findFirst()
                .ifPresent(emergencyCareEpisode -> {
                    result.setId(emergencyCareEpisode.getId());
                    result.setInProgress(true);
                });

        log.debug(OUTPUT, result);
		return result;
	}

    private void logIfThereAreMoreThanOneEmergencyCareEpisodeInOneInstitution(List<EmergencyCareEpisode> emergencyCareEpisodes) {
        if (!emergencyCareEpisodes.isEmpty() && emergencyCareEpisodes.size() > 1) {
            log.error("TICKET -> hsi-6584");
            String contextInformation = this.getMinimalInformationToLog(emergencyCareEpisodes);
            log.error("EmergencyCareEpisodeException -> Context information: {}", contextInformation);
        }
    }

    private String getMinimalInformationToLog(List<EmergencyCareEpisode> emergencyCareEpisodes) {
        return emergencyCareEpisodes.stream()
                .map(EmergencyCareEpisode::getMinimalInformationToLog)
                .collect(Collectors.joining(" | "));
    }

    @Override
	public EmergencyCareEpisodeInProgressBo getEmergencyCareEpisodeInProgress(Integer institutionId, Integer patientId) {
		log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
		EmergencyCareEpisodeInProgressBo result = new EmergencyCareEpisodeInProgressBo(null, false);
		List<EmergencyCareVo> resultQuery = emergencyCareEpisodeRepository.getEmergencyCareEpisodeInProgress(patientId);
		if(!resultQuery.isEmpty()) {
			result.setInProgress(true);
			resultQuery.forEach(rq -> {
				if (rq.getInstitutionId().equals(institutionId))
					result.setId(rq.getId());
			});
		}
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public EmergencyCareBo getEpisodeSummary(Integer institutionId, Integer episodeId) {
		log.debug("Input parameters -> institutionId {}, episodeId {}", institutionId, episodeId);
		EmergencyCareVo resultQuery = emergencyCareEpisodeRepository.getEpisode(episodeId).get();
		EmergencyCareBo result = new EmergencyCareBo(resultQuery);
		result.setCreatedOn(UTCIntoInstitutionLocalDateTime(institutionId, result.getCreatedOn()));
		log.debug(OUTPUT, result);
		return result;
	}

    @Override
    public EmergencyCareBo get(Integer episodeId, Integer institutionId) {
        log.debug("Input parameters -> episodeId {}, institutionId {}", episodeId, institutionId);
        EmergencyCareVo emergencyCareEpisode = emergencyCareEpisodeRepository.getEpisode(episodeId)
                .orElseThrow(() -> new NotFoundException(WRONG_CARE_ID_EPISODE, CARE_EPISODE_NOT_FOUND));
        EmergencyCareBo result = new EmergencyCareBo(emergencyCareEpisode);
		result.setRoom(emergencyCareEpisode.getRoom() != null ? new RoomBo(emergencyCareEpisode.getRoom()): null);
        result.setCreatedOn(UTCIntoInstitutionLocalDateTime(institutionId, result.getCreatedOn()));
		result.setCanBeAbsent(getCanBeAbsent(result.getId(), result.getEmergencyCareStateId()));
		log.debug(OUTPUT, result);
		return result;
    }

    @Override
    @Transactional
    public Boolean validateAndSetPatient(Integer episodeId, Integer patientId, Integer institutionId) {
        log.debug("Input parameters -> episodeId {}, patientId {}",
                episodeId, patientId);
		assertExistsActiveEpisodeByPatientIdAndInstitutionId(patientId, institutionId);
        emergencyCareEpisodeRepository.updatePatientId(episodeId, patientId);
        return true;
    }

	@Override
	public boolean haveMoreThanOneEmergencyCareEpisodeFromPatients(List<Integer> patients) {
		log.debug("Input parameters -> patientId {}", patients);
		List<EmergencyCareEpisode> ece = emergencyCareEpisodeRepository.getFromPatientsAndStatus(patients, Arrays.asList(
				EmergencyCareState.EN_ESPERA,
				EmergencyCareState.EN_ATENCION,
				EmergencyCareState.CON_ALTA_MEDICA
		));
		if (ece.isEmpty()) {
			log.debug(OUTPUT, false);
			return false;
		}
		return ece.stream().map(EmergencyCareEpisode::getInstitutionId).distinct().count() != ece.size();
	}

	@Override
    public EmergencyCareBo createAdministrative(EmergencyCareBo newEmergencyCare, Integer institutionId) {
		EmergencyCareBo emergencyCareBo = createEpisode(newEmergencyCare, institutionId, this::saveTriageAdministrative, null);
		addTriageReasons(emergencyCareBo.getTriage());
		return emergencyCareBo;
    }

    @Override
    public EmergencyCareBo createAdult(EmergencyCareBo newEmergencyCare, Integer institutionId, RiskFactorBo riskFactors) {
		EmergencyCareBo emergencyCareBo = createEpisode(newEmergencyCare, institutionId, this::saveTriageAdult, riskFactors);
		addTriageReasons(emergencyCareBo.getTriage());
        return emergencyCareBo;
    }

    @Override
    public EmergencyCareBo createPediatric(EmergencyCareBo newEmergencyCare, Integer institutionId, RiskFactorBo riskFactors) {
		EmergencyCareBo emergencyCareBo = createEpisode(newEmergencyCare, institutionId, this::saveTriagePediatric, riskFactors);
		addTriageReasons(emergencyCareBo.getTriage());
        return emergencyCareBo;
    }

	@Override
	public PatientECEBo getRelatedPatientData(Integer episodeId) {
		return new PatientECEBo(emergencyCareEpisodeRepository.getPatientDataByEpisodeId(episodeId));
	}

	@Override
	public Integer getPatientMedicalCoverageIdByEpisode(Integer emergencyCareEpisodeId) {
		log.debug("Input parameters -> emergencyCareEpisodeId {}", emergencyCareEpisodeId);
		Integer result = emergencyCareEpisodeRepository.getPatientMedicalCoverageIdByEpisodeId(emergencyCareEpisodeId);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public boolean isBedOccupiedByEmergencyEpisode(Integer bedId) {
		log.debug("Input parameters -> bedId {}", bedId);
		boolean result = emergencyCareEpisodeRepository.isBedOccupiedByEmergencyEpisode(bedId);
		log.debug(OUTPUT, result);
		return result;
	}
	
	@Override
	public Boolean hasEvolutionNote(Integer episodeId) {
		log.debug("Input parameters -> emergencyCareEpisodeId {}", episodeId);
		Boolean result = emergencyCareEpisodeRepository.episodeHasEvolutionNote(episodeId);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public Integer getEmergencyEpisodeEpisodeIdByDate(Integer institutionId, Integer patientId, LocalDateTime date) {
		Page<Integer> id = emergencyCareEpisodeRepository.getInternmentEpisodeIdByDate(institutionId, patientId, date, PageRequest.of(0, 1));
		if (!id.getContent().isEmpty()) return id.getContent().get(0);
		return null;
	}
    
    @Override
	public Optional<Integer> getRoomId(Integer emergencyCareEpisodeId) {
		log.debug("Input parameters -> emergencyCareEpisodeId {}", emergencyCareEpisodeId);
		Integer result = emergencyCareEpisodeRepository.getRoomId(emergencyCareEpisodeId);
		log.debug(OUTPUT, result);
		return Optional.ofNullable(result);
	}

	@Override
	public Optional<Integer> getDoctorsOfficeId(Integer emergencyCareEpisodeId) {
		log.debug("Input parameters -> emergencyCareEpisodeId {}", emergencyCareEpisodeId);
		Integer result = emergencyCareEpisodeRepository.getEmergencyCareEpisodeDoctorsOfficeId(emergencyCareEpisodeId);
		log.debug(OUTPUT, result);
		return Optional.ofNullable(result);
	}

	@Override
	public Optional<Integer> getShockRoomId(Integer emergencyCareEpisodeId) {
		log.debug("Input parameters -> emergencyCareEpisodeId {}", emergencyCareEpisodeId);
		Integer result = emergencyCareEpisodeRepository.getEmergencyCareEpisodeShockroomId(emergencyCareEpisodeId);
		log.debug(OUTPUT, result);
		return Optional.ofNullable(result);
	}

	private void validateUpdate(EmergencyCareEpisode persisted, EmergencyCareBo toUpdate){
        if (persisted.getEmergencyCareEntranceTypeId() != null && toUpdate.getEmergencyCareEntranceId() == null)
            throw new ValidationException("care-episode.entrance.invalid.update");
        if (persisted.getEmergencyCareTypeId() != null && toUpdate.getEmergencyCareTypeId() == null)
            throw new ValidationException("care-episode.type.invalid.update");
        if (persisted.getHasPoliceIntervention() != null && toUpdate.getHasPoliceIntervention() == null)
            throw new ValidationException("care-episode.police.intervention.invalid.update");
    }

    @Override
    public Integer updateAdministrative(EmergencyCareBo newEmergencyCare, Integer institutionId){
        EmergencyCareEpisode e = emergencyCareEpisodeRepository
                .findById(newEmergencyCare.getId())
                .orElseThrow(()->new NotFoundException("ECEpisode.not.found", "ECEpisode not found"));

            validateUpdate(e, newEmergencyCare);
            updatePatient(e, newEmergencyCare, institutionId);
            e.setEmergencyCareTypeId(newEmergencyCare.getEmergencyCareTypeId());
            e.setEmergencyCareEntranceTypeId(newEmergencyCare.getEmergencyCareEntranceId());
            e.setAmbulanceCompanyId(newEmergencyCare.getAmbulanceCompanyId());
			e.setReason(newEmergencyCare.getReason());
            if (e.getEmergencyCareStateId() != EmergencyCareState.EN_ATENCION)
                e.setDoctorsOfficeId(newEmergencyCare.getDoctorsOfficeId());

            PoliceInterventionDetailsBo policeInterventionDetailsBo = newEmergencyCare.getPoliceInterventionDetails();
            updatePoliceIntervention(policeInterventionDetailsBo, e, newEmergencyCare);
            e.setHasPoliceIntervention(newEmergencyCare.getHasPoliceIntervention());
            EmergencyCareEpisode saved  = emergencyCareEpisodeRepository.save(e);
            log.debug(OUTPUT, saved);
        return 1;
    }

	@Override
	public Optional<LocalDateTime> getCreatedOn(Integer episodeId){
		log.debug("Input parameters -> episodeId {}", episodeId);
		Optional<LocalDateTime> result = emergencyCareEpisodeRepository.findById(episodeId).map(EmergencyCareEpisode::getCreatedOn);
		log.debug("Output -> result {}", result);
		return result;
	}

	@Override
	public Boolean updatePatientDescription(Integer episodeId, String patientDescription) {
		log.debug("Input parameters -> episodeId {}, patientDescription {}", episodeId, patientDescription);
		emergencyCareEpisodeRepository.updatePatientDescription(episodeId, patientDescription);
		var result = Boolean.TRUE;
		log.debug("Output -> result {}", result);
		return result;
	}

    private void updatePatient(EmergencyCareEpisode episodePersisted, EmergencyCareBo episodeToUpdate, Integer institutionId) {
		if (episodeToUpdate.getPatient().getId() == null) {
			throw new SaveEmergencyCareEpisodeException(SaveEmergencyCareEpisodeExceptionEnum.PATIENT_ID_NULL, "Se debe especificar el id del paciente para la edicion del episodio de guardia");
		}

		if (!episodePersisted.getPatientId().equals(episodeToUpdate.getPatient().getId()))
			assertValidPatient(episodeToUpdate.getPatient(), institutionId);
		episodePersisted.setPatientId(episodeToUpdate.getPatient().getId());
		episodePersisted.setPatientMedicalCoverageId(episodeToUpdate.getPatient().getPatientMedicalCoverageId());
		episodePersisted.setPatientDescription(episodeToUpdate.getPatient().getPatientDescription());
    }

    private void updatePoliceIntervention(PoliceInterventionDetailsBo policeInterventionDetailsBo, EmergencyCareEpisode episodePersisted, EmergencyCareBo episodeToUpdate) {
        if ((episodePersisted.getHasPoliceIntervention() != null && episodePersisted.getHasPoliceIntervention()) && (episodeToUpdate.getHasPoliceIntervention() != null && !episodeToUpdate.getHasPoliceIntervention())
            && policeInterventionRepository.existsById(episodePersisted.getId()))
                policeInterventionRepository.deleteById(episodePersisted.getId());
        else {
            if (episodeToUpdate.getHasPoliceIntervention() != null && episodeToUpdate.getHasPoliceIntervention() && policeInterventionDetailsBo != null) {
                PoliceInterventionDetails policeInterventionDetails = new PoliceInterventionDetails(policeInterventionDetailsBo);
                policeInterventionDetails.setId(episodeToUpdate.getId());
                policeInterventionDetails = policeInterventionRepository.save(policeInterventionDetails);
                policeInterventionDetailsBo = new PoliceInterventionDetailsBo(policeInterventionDetails);
                log.debug(OUTPUT, policeInterventionDetailsBo);
            }
        }
    }

    private void assertValidPatient(PatientECEBo patient, Integer institutionId) {
		assertExistsActiveEpisodeByPatientIdAndInstitutionId(patient.getId(), institutionId);
		assertTemporaryPatient(patient);
    }

    private EmergencyCareBo createEpisode(EmergencyCareBo newEmergencyCare, Integer institutionId, Function<SaveTriageArgs, TriageBo> saveTriage, RiskFactorBo riskFactors) {

        log.debug("Input parameters -> newEmergencyCare {}, institutionId{}", newEmergencyCare, institutionId);
        EmergencyCareBo emergencyCareEpisodeBo = saveEmergencyCareEpisode(newEmergencyCare, newEmergencyCare.getTriage());

        PoliceInterventionDetailsBo policeInterventionDetailsBo = newEmergencyCare.getPoliceInterventionDetails();
        policeInterventionDetailsBo = (policeInterventionDetailsBo != null && (emergencyCareEpisodeBo.getHasPoliceIntervention() != null && emergencyCareEpisodeBo.getHasPoliceIntervention())  ) ? savePoliceIntervention(policeInterventionDetailsBo, emergencyCareEpisodeBo.getId()) : new PoliceInterventionDetailsBo();
        saveHistoricEmergencyEpisode(emergencyCareEpisodeBo);

		setTriageDocumentData(newEmergencyCare, riskFactors);
        TriageBo triageBo = saveTriage.apply(new SaveTriageArgs(newEmergencyCare.getTriage(), emergencyCareEpisodeBo.getId(), institutionId));


        emergencyCareEpisodeBo.setPoliceInterventionDetails(policeInterventionDetailsBo);
        emergencyCareEpisodeBo.setTriage(triageBo);

        log.debug(OUTPUT, emergencyCareEpisodeBo);
        return emergencyCareEpisodeBo;
    }

	private void setTriageDocumentData(EmergencyCareBo newEmergencyCare, RiskFactorBo riskFactors) {
		newEmergencyCare.getTriage().setRiskFactors(riskFactors);
		BasicPatientDto patient = patientExternalService.getBasicDataFromPatient(newEmergencyCare.getPatient().getId());
		if (patient.getPerson() != null)
			newEmergencyCare.getTriage().setPatientInfo(new PatientInfoBo(patient.getId(), patient.getPerson().getGender().getId(), patient.getPerson().getAge()));
		else
			newEmergencyCare.getTriage().setPatientInfo(new PatientInfoBo(patient.getId()));
	}

    private HistoricEmergencyEpisodeBo saveHistoricEmergencyEpisode(EmergencyCareBo emergencyCare) {
        log.debug("Input parameter -> emergencyCare {}", emergencyCare);
        HistoricEmergencyEpisodeBo historicEmergencyEpisodeBo = new HistoricEmergencyEpisodeBo(emergencyCare);
        historicEmergencyEpisodeBo = historicEmergencyEpisodeService.saveChange(historicEmergencyEpisodeBo);
        log.debug("Output -> {}", historicEmergencyEpisodeBo);
        return historicEmergencyEpisodeBo;
    }

    private PoliceInterventionDetailsBo savePoliceIntervention(PoliceInterventionDetailsBo policeInterventionDetailsBo, Integer emergencyCareId) {
        log.debug("Input parameter -> policeInterventionBo {}, emergencyCareId {}", policeInterventionDetailsBo, emergencyCareId);
        PoliceInterventionDetails policeInterventionDetails = new PoliceInterventionDetails(policeInterventionDetailsBo);
        policeInterventionDetails.setId(emergencyCareId);
        policeInterventionDetails = policeInterventionRepository.save(policeInterventionDetails);
        PoliceInterventionDetailsBo result = new PoliceInterventionDetailsBo(policeInterventionDetails);
        log.debug(OUTPUT, result);
        return result;
    }

    private EmergencyCareBo saveEmergencyCareEpisode(EmergencyCareBo emergencyCareBo, TriageBo triageBo) {
        log.debug("Input parameters -> emergencyCareBo {}, triageBo {}", emergencyCareBo, triageBo);
        assertValidPatient(emergencyCareBo.getPatient(), emergencyCareBo.getInstitutionId());
        EmergencyCareEpisode emergencyCareEpisode = new EmergencyCareEpisode(emergencyCareBo, triageBo);
        emergencyCareEpisode = emergencyCareEpisodeRepository.save(emergencyCareEpisode);
        EmergencyCareBo result = new EmergencyCareBo(emergencyCareEpisode);
        log.debug(OUTPUT, result);
        return result;
    }

    private TriageBo saveTriageAdult(SaveTriageArgs args) {
        log.debug("Input parameters -> triageBo {}, emergencyCareEpisodeId {}, institutionId {}", args.triageBo, args.emergencyCareEpisodeId, args.institutionId);
		if (!args.triageBo.getCategoryId().equals(WITHOUT_TRIAGE_LEVEL_ID)) {
			args.triageBo.setEmergencyCareEpisodeId(args.emergencyCareEpisodeId);
			TriageBo result = triageService.createAdultGynecological(args.triageBo, args.institutionId);
			log.debug(OUTPUT, result);
			return result;
		}
		return null;
    }

    private TriageBo saveTriageAdministrative(SaveTriageArgs args) {
        log.debug("Input parameters -> triageBo {}, emergencyCareEpisodeId {}, institutionId {}", args.triageBo, args.emergencyCareEpisodeId, args.institutionId);
		if (!args.triageBo.getCategoryId().equals(WITHOUT_TRIAGE_LEVEL_ID)) {
			args.triageBo.setEmergencyCareEpisodeId(args.emergencyCareEpisodeId);
			TriageBo result = triageService.createAdministrative(args.triageBo, args.institutionId);
			log.debug(OUTPUT, result);
			return result;
		}
		return null;
    }

    private TriageBo saveTriagePediatric(SaveTriageArgs args) {
        log.debug("Input parameters -> triageBo {}, emergencyCareEpisodeId {}, institutionId {}", args.triageBo, args.emergencyCareEpisodeId, args.institutionId);
		if (!args.triageBo.getCategoryId().equals(WITHOUT_TRIAGE_LEVEL_ID)) {
			args.triageBo.setEmergencyCareEpisodeId(args.emergencyCareEpisodeId);
			TriageBo result = triageService.createPediatric(args.triageBo, args.institutionId);
			log.debug(OUTPUT, result);
			return result;
		}
		return null;
    }

    private LocalDateTime UTCIntoInstitutionLocalDateTime(Integer institutionId, LocalDateTime date) {
        log.debug("Input parameters -> institutionId {}, date {}", institutionId, date);
        ZoneId institutionZoneId = institutionExternalService.getTimezone(institutionId);
        LocalDateTime result = date
                .atZone(ZoneId.of(JacksonDateFormatConfig.UTC_ZONE_ID))
                .withZoneSameInstant(institutionZoneId)
                .toLocalDateTime();
        log.debug(OUTPUT, result);
        return result;
    }

	private void addTriageReasons(TriageBo triageBo){
		if (triageBo != null)
			triageService.addTriageReasons(triageBo.getReasons(), triageBo.getTriageId());
	}

    class SaveTriageArgs {
        final TriageBo triageBo;
        final Integer emergencyCareEpisodeId;
        final Integer institutionId;

        SaveTriageArgs(TriageBo triageBo, Integer emergencyCareEpisodeId, Integer institutionId) {
            this.triageBo = triageBo;
            this.emergencyCareEpisodeId = emergencyCareEpisodeId;
            this.institutionId = institutionId;
        }
    }

	private void assertExistsActiveEpisodeByPatientIdAndInstitutionId(Integer patientId, Integer institutionId) {
		boolean violatesConstraint = emergencyCareEpisodeRepository.existsActiveEpisodeByPatientIdAndInstitutionId(patientId,institutionId);
		if (violatesConstraint)
			throw new ValidationException("care-episode.patient.invalid");
	}

	private void assertTemporaryPatient(PatientECEBo patient) {
		boolean isATemporaryPatient = patientExternalService.getBasicDataFromPatient(patient.getId()).getTypeId().equals(EPatientType.EMERGENCY_CARE_TEMPORARY.getId());
		boolean hasNotPatientDescription = patient.getPatientDescription() == null || patient.getPatientDescription().isEmpty();
		if (isATemporaryPatient && hasNotPatientDescription) {
			throw new SaveEmergencyCareEpisodeException(SaveEmergencyCareEpisodeExceptionEnum.PATIENT_DESCRIPTION, "No se puede editar un episodio de guardia con paciente temporal sin una descripcion identificatoria del paciente");
		}
	}

	private Boolean getCanBeAbsent(Integer episodeId, Short emergencyCareStateId){
		EEmergencyCareState fromState = EEmergencyCareState.getById(emergencyCareStateId);
		return EEmergencyCareState.validTransition(fromState ,EEmergencyCareState.AUSENTE) &&
				!emergencyCareEpisodeRepository.episodeHasEvolutionNote(episodeId);
	}
}
