package net.pladema.emergencycare.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeReasonRepository;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.emergencycare.repository.PoliceInterventionRepository;
import net.pladema.emergencycare.repository.domain.EmergencyCareVo;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisodeReason;
import net.pladema.emergencycare.repository.entity.EmergencyCareState;
import net.pladema.emergencycare.repository.entity.PoliceInterventionDetails;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.HistoricEmergencyEpisodeService;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.emergencycare.service.domain.EmergencyCareEpisodeInProgressBo;
import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;
import net.pladema.emergencycare.service.domain.PatientECEBo;
import net.pladema.emergencycare.service.domain.PoliceInterventionDetailsBo;
import net.pladema.emergencycare.triage.service.TriageService;
import net.pladema.emergencycare.triage.service.domain.TriageBo;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EmergencyCareEpisodeServiceImpl implements EmergencyCareEpisodeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private static final String WRONG_CARE_ID_EPISODE = "wrong-care-id-episode";

    private static final String CARE_EPISODE_NOT_FOUND = "El episodio de guardia no se encontró o no existe";

    private final TriageService triageService;

    private final PoliceInterventionRepository policeInterventionRepository;

    private final EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;

    private final EmergencyCareEpisodeReasonRepository emergencyCareEpisodeReasonRepository;

    private final InstitutionExternalService institutionExternalService;

    private final HistoricEmergencyEpisodeService historicEmergencyEpisodeService;

    public EmergencyCareEpisodeServiceImpl(TriageService triageService,
                                           PoliceInterventionRepository policeInterventionRepository,
                                           EmergencyCareEpisodeRepository emergencyCareEpisodeRepository,
                                           EmergencyCareEpisodeReasonRepository emergencyCareEpisodeReasonRepository,
                                           InstitutionExternalService institutionExternalService,
                                           HistoricEmergencyEpisodeService historicEmergencyEpisodeService){
        super();
        this.triageService = triageService;
        this.policeInterventionRepository = policeInterventionRepository;
        this.emergencyCareEpisodeRepository = emergencyCareEpisodeRepository;
        this.emergencyCareEpisodeReasonRepository = emergencyCareEpisodeReasonRepository;
        this.institutionExternalService = institutionExternalService;
        this.historicEmergencyEpisodeService = historicEmergencyEpisodeService;
    }

    @Override
    public List<EmergencyCareBo> getAll(Integer institutionId) {
        LOG.debug("Input parameters -> institutionId {}", institutionId);
        List<EmergencyCareVo> resultQuery = emergencyCareEpisodeRepository.getAll(institutionId);
        List<EmergencyCareBo> result = resultQuery.stream().map(EmergencyCareBo::new)
                .sorted(Comparator.comparing(EmergencyCareBo::getEmergencyCareStateId).thenComparing(EmergencyCareBo::getTriageCategoryId).thenComparing(EmergencyCareBo::getCreatedOn))
                        .collect(Collectors.toList());
        result.forEach(ec -> ec.setCreatedOn(UTCIntoInstitutionLocalDateTime(institutionId, ec.getCreatedOn())));
        LOG.debug(OUTPUT, result);
        return result;
    }

	@Override
	public EmergencyCareEpisodeInProgressBo emergencyCareEpisodeInProgress(Integer institutionId, Integer patientId) {
		LOG.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
		EmergencyCareEpisodeInProgressBo result = new EmergencyCareEpisodeInProgressBo(null, false);
		Optional<Integer> resultQuery = emergencyCareEpisodeRepository.emergencyCareEpisodeInProgress(institutionId, patientId);
		resultQuery.ifPresent(id -> {
			result.setId(id);
			result.setInProgress(true);
		});
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public EmergencyCareBo getEpisodeSummary(Integer institutionId, Integer episodeId) {
		LOG.debug("Input parameters -> institutionId {}, episodeId {}", institutionId, episodeId);
		EmergencyCareVo resultQuery = emergencyCareEpisodeRepository.getEpisode(episodeId, institutionId).get();
		EmergencyCareBo result = new EmergencyCareBo(resultQuery);
		result.setCreatedOn(UTCIntoInstitutionLocalDateTime(institutionId, result.getCreatedOn()));
		LOG.debug(OUTPUT, result);
		return result;
	}

    @Override
    public EmergencyCareBo get(Integer episodeId, Integer institutionId) {
        LOG.debug("Input parameters -> episodeId {}, institutionId {}", episodeId, institutionId);
        EmergencyCareVo emergencyCareEpisode = emergencyCareEpisodeRepository.getEpisode(episodeId, institutionId)
                .orElseThrow(() -> new NotFoundException(WRONG_CARE_ID_EPISODE, CARE_EPISODE_NOT_FOUND));
        EmergencyCareBo result = new EmergencyCareBo(emergencyCareEpisode);

        //Motivos de consulta
        List<ReasonBo> reasons = emergencyCareEpisodeReasonRepository.findByEpisodeId(episodeId)
                .stream()
                .map(ReasonBo::new)
                .collect(Collectors.toList());
        result.setReasons(reasons);

        result.setCreatedOn(UTCIntoInstitutionLocalDateTime(institutionId, result.getCreatedOn()));
		LOG.debug(OUTPUT, result);
		return result;
    }

    @Override
    public Boolean validateAndSetPatient(Integer episodeId, Integer patientId, Integer institutionId) {
        LOG.debug("Input parameters -> episodeId {}, patientId {}",
                episodeId, patientId);
        assertPatientValid(patientId, institutionId);
        emergencyCareEpisodeRepository.updatePatientId(episodeId, patientId);
        return true;
    }

    @Override
    public EmergencyCareBo createAdministrative(EmergencyCareBo newEmergencyCare, Integer institutionId) {
        return createEpisode(newEmergencyCare, institutionId, this::saveTriageAdministrative);
    }

    @Override
    public EmergencyCareBo createAdult(EmergencyCareBo newEmergencyCare, Integer institutionId) {
        return createEpisode(newEmergencyCare, institutionId, this::saveTriageAdult);
    }

    @Override
    public EmergencyCareBo createPediatric(EmergencyCareBo newEmergencyCare, Integer institutionId) {
        return createEpisode(newEmergencyCare, institutionId, this::saveTriagePediatric);
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
            if (e.getEmergencyCareStateId() != EmergencyCareState.EN_ATENCION)
                e.setDoctorsOfficeId(newEmergencyCare.getDoctorsOfficeId());

            PoliceInterventionDetailsBo policeInterventionDetailsBo = newEmergencyCare.getPoliceInterventionDetails();
            updatePoliceIntervention(policeInterventionDetailsBo, e, newEmergencyCare);
            e.setHasPoliceIntervention(newEmergencyCare.getHasPoliceIntervention());
            updateReasons(newEmergencyCare.getReasons(), e.getId());
            EmergencyCareEpisode saved  = emergencyCareEpisodeRepository.save(e);
            LOG.debug(OUTPUT, saved);;
        return 1;
    }

    private void updatePatient(EmergencyCareEpisode episodePersisted, EmergencyCareBo episodeToUpdate, Integer institutionId){
        if (episodeToUpdate.getPatient() != null) {
            if (episodePersisted.getPatientId() == null || (episodePersisted.getPatientId() != null && !episodePersisted.getPatientId().equals(episodeToUpdate.getPatient().getId())))
                assertPatientValid(episodeToUpdate.getPatient().getId(), institutionId);
            episodePersisted.setPatientId(episodeToUpdate.getPatient().getId());
            episodePersisted.setPatientMedicalCoverageId(episodeToUpdate.getPatient().getPatientMedicalCoverageId());
        }
        else{
            episodePersisted.setPatientId(null);
            episodePersisted.setPatientMedicalCoverageId(null);
        }

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
                LOG.debug(OUTPUT, policeInterventionDetailsBo);
            }
        }
    }

    private void updateReasons(List<ReasonBo> reasons, Integer emergencyCareEpisodeId) {
        emergencyCareEpisodeReasonRepository.deleteByEpisodeId(emergencyCareEpisodeId);
        if (reasons.size() != 0) {
            LOG.debug("Input parameters -> reasons {}, emergencyCareEpisodeId {}", reasons, emergencyCareEpisodeId);
            reasons.forEach(reason -> {
                EmergencyCareEpisodeReason emergencyCareEpisodeReason = new EmergencyCareEpisodeReason(emergencyCareEpisodeId, reason.getSctid());
                emergencyCareEpisodeReasonRepository.save(emergencyCareEpisodeReason);
            });
            LOG.debug(OUTPUT, reasons);
        }
    }

    private void validPatient(PatientECEBo patient, Integer institutionId) {
        if (patient != null)
            assertPatientValid(patient.getId(), institutionId);
    }

    private void assertPatientValid(Integer patientId, Integer institutionId) {
        boolean violatesConstraint = emergencyCareEpisodeRepository.existsActiveEpisodeByPatientIdAndInstitutionId(patientId,institutionId);
        if (violatesConstraint)
            throw new ValidationException("care-episode.patient.invalid");
    }

    private EmergencyCareBo createEpisode(EmergencyCareBo newEmergencyCare, Integer institutionId, Function<SaveTriageArgs, TriageBo> saveTriage) {

        LOG.debug("Input parameters -> newEmergencyCare {}, institutionId{}", newEmergencyCare, institutionId);
        EmergencyCareBo emergencyCareEpisodeBo = saveEmergencyCareEpisode(newEmergencyCare, newEmergencyCare.getTriage());

        PoliceInterventionDetailsBo policeInterventionDetailsBo = newEmergencyCare.getPoliceInterventionDetails();
        policeInterventionDetailsBo = (policeInterventionDetailsBo != null && (emergencyCareEpisodeBo.getHasPoliceIntervention() != null && emergencyCareEpisodeBo.getHasPoliceIntervention())  ) ? savePoliceIntervention(policeInterventionDetailsBo, emergencyCareEpisodeBo.getId()) : new PoliceInterventionDetailsBo();
        saveHistoricEmergencyEpisode(emergencyCareEpisodeBo);

        TriageBo triageBo = saveTriage.apply(new SaveTriageArgs(newEmergencyCare.getTriage(), emergencyCareEpisodeBo.getId(), institutionId));

        List<ReasonBo> reasons = saveReasons(newEmergencyCare.getReasons(), emergencyCareEpisodeBo.getId());

        emergencyCareEpisodeBo.setPoliceInterventionDetails(policeInterventionDetailsBo);
        emergencyCareEpisodeBo.setTriage(triageBo);
        emergencyCareEpisodeBo.setReasons(reasons);

        LOG.debug(OUTPUT, emergencyCareEpisodeBo);
        return emergencyCareEpisodeBo;
    }

    private HistoricEmergencyEpisodeBo saveHistoricEmergencyEpisode(EmergencyCareBo emergencyCare) {
        LOG.debug("Input parameter -> emergencyCare {}", emergencyCare);
        HistoricEmergencyEpisodeBo historicEmergencyEpisodeBo = new HistoricEmergencyEpisodeBo(emergencyCare);
        historicEmergencyEpisodeBo = historicEmergencyEpisodeService.saveChange(historicEmergencyEpisodeBo);
        LOG.debug("Output -> {}", historicEmergencyEpisodeBo);
        return historicEmergencyEpisodeBo;
    }

    private PoliceInterventionDetailsBo savePoliceIntervention(PoliceInterventionDetailsBo policeInterventionDetailsBo, Integer emergencyCareId) {
        LOG.debug("Input parameter -> policeInterventionBo {}, emergencyCareId {}", policeInterventionDetailsBo, emergencyCareId);
        PoliceInterventionDetails policeInterventionDetails = new PoliceInterventionDetails(policeInterventionDetailsBo);
        policeInterventionDetails.setId(emergencyCareId);
        policeInterventionDetails = policeInterventionRepository.save(policeInterventionDetails);
        PoliceInterventionDetailsBo result = new PoliceInterventionDetailsBo(policeInterventionDetails);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private EmergencyCareBo saveEmergencyCareEpisode(EmergencyCareBo emergencyCareBo, TriageBo triageBo) {
        LOG.debug("Input parameters -> emergencyCareBo {}, triageBo {}", emergencyCareBo, triageBo);
        validPatient(emergencyCareBo.getPatient(), emergencyCareBo.getInstitutionId());
        EmergencyCareEpisode emergencyCareEpisode = new EmergencyCareEpisode(emergencyCareBo, triageBo);
        emergencyCareEpisode = emergencyCareEpisodeRepository.save(emergencyCareEpisode);
        EmergencyCareBo result = new EmergencyCareBo(emergencyCareEpisode);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private TriageBo saveTriageAdult(SaveTriageArgs args) {
        LOG.debug("Input parameters -> triageBo {}, emergencyCareEpisodeId {}, institutionId {}", args.triageBo, args.emergencyCareEpisodeId, args.institutionId);
        args.triageBo.setEmergencyCareEpisodeId(args.emergencyCareEpisodeId);
        TriageBo result = triageService.createAdultGynecological(args.triageBo, args.institutionId);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private TriageBo saveTriageAdministrative(SaveTriageArgs args) {
        LOG.debug("Input parameters -> triageBo {}, emergencyCareEpisodeId {}, institutionId {}", args.triageBo, args.emergencyCareEpisodeId, args.institutionId);
        args.triageBo.setEmergencyCareEpisodeId(args.emergencyCareEpisodeId);
        TriageBo result = triageService.createAdministrative(args.triageBo, args.institutionId);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private TriageBo saveTriagePediatric(SaveTriageArgs args) {
        LOG.debug("Input parameters -> triageBo {}, emergencyCareEpisodeId {}, institutionId {}", args.triageBo, args.emergencyCareEpisodeId, args.institutionId);
        args.triageBo.setEmergencyCareEpisodeId(args.emergencyCareEpisodeId);
        TriageBo result = triageService.createPediatric(args.triageBo, args.institutionId);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private List<ReasonBo> saveReasons(List<ReasonBo> reasons, Integer emergencyCareEpisodeId) {
        LOG.debug("Input parameters -> reasons {}, emergencyCareEpisodeId {}", reasons, emergencyCareEpisodeId);
        reasons.forEach(reason -> {
            EmergencyCareEpisodeReason emergencyCareEpisodeReason = new EmergencyCareEpisodeReason(emergencyCareEpisodeId, reason.getSctid());
            emergencyCareEpisodeReasonRepository.save(emergencyCareEpisodeReason);
        });
        LOG.debug(OUTPUT, reasons);
        return reasons;
    }

    private LocalDateTime UTCIntoInstitutionLocalDateTime(Integer institutionId, LocalDateTime date) {
        LOG.debug("Input parameters -> institutionId {}, date {}", institutionId, date);
        ZoneId institutionZoneId = institutionExternalService.getTimezone(institutionId);
        LocalDateTime result = date
                .atZone(ZoneId.of(JacksonDateFormatConfig.UTC_ZONE_ID))
                .withZoneSameInstant(institutionZoneId)
                .toLocalDateTime();
        LOG.debug(OUTPUT, result);
        return result;
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
}
