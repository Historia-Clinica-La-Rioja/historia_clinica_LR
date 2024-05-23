package net.pladema.emergencycare.triage.service.impl;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.EBodyTemperature;
import ar.lamansys.sgh.shared.infrastructure.input.service.EMuscleHypertonia;
import ar.lamansys.sgh.shared.infrastructure.input.service.EPerfusion;
import ar.lamansys.sgh.shared.infrastructure.input.service.ERespiratoryRetraction;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.emergencycare.service.EmergencyCareEpisodeStateService;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import net.pladema.emergencycare.triage.application.addtriagereasons.AddTriageReasons;
import net.pladema.emergencycare.triage.application.fetchtriagereasons.FetchTriageReasons;
import net.pladema.emergencycare.triage.repository.TriageDetailsRepository;
import net.pladema.emergencycare.triage.repository.TriageRepository;
import net.pladema.emergencycare.triage.repository.TriageRiskFactorsRepository;
import net.pladema.emergencycare.triage.repository.domain.TriageVo;
import net.pladema.emergencycare.triage.repository.entity.Triage;
import net.pladema.emergencycare.triage.repository.entity.TriageDetails;
import net.pladema.emergencycare.triage.repository.entity.TriageRiskFactors;
import net.pladema.emergencycare.triage.service.TriageService;
import net.pladema.emergencycare.triage.domain.TriageBo;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import net.pladema.establishment.repository.RoomRepository;
import net.pladema.medicalconsultation.doctorsoffice.repository.DoctorsOfficeRepository;
import net.pladema.medicalconsultation.shockroom.infrastructure.repository.ShockroomRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TriageServiceImpl implements TriageService {

    private static final Logger LOG = LoggerFactory.getLogger(TriageServiceImpl.class);

    private final TriageRepository triageRepository;

    private final TriageDetailsRepository triageDetailsRepository;

    private final TriageRiskFactorsRepository triageRiskFactorsRepository;

    private final InstitutionExternalService institutionExternalService;

    private final EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;

    private final EmergencyCareEpisodeStateService emergencyCareEpisodeStateService;

	private final DocumentFactory documentFactory;

	private final DocumentService documentService;

	private final ShockroomRepository shockroomRepository;

	private final RoomRepository roomRepository;

	private final DoctorsOfficeRepository doctorsOfficeRepository;
	private final AddTriageReasons addTriageReasons;
	private final FetchTriageReasons fetchTriageReasons;

    @Override
    public List<TriageBo> getAll(Integer institutionId, Integer episodeId) {
        LOG.debug("Input parameter -> institutionId {}, episodeId {}", institutionId, episodeId);
        List<TriageVo> triageVos = triageRepository.getAllByEpisodeId(episodeId);
        List<TriageBo> result = triageVos.stream()
                .map(TriageBo::new)
                .collect(Collectors.toList());
        result.forEach(t -> {
            setDetailsDescriptions(t);
            t.setCreatedOn(UTCIntoInstitutionLocalDateTime(institutionId, t.getCreatedOn()));
			t.setReasons(fetchTriageReasons.run(t.getTriageId()));
        });
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }

    private LocalDateTime UTCIntoInstitutionLocalDateTime(Integer institutionId, LocalDateTime date) {
        LOG.debug("Input parameters -> institutionId {}, date {}", institutionId, date);
        ZoneId institutionZoneId = institutionExternalService.getTimezone(institutionId);
        LocalDateTime result = date
                .atZone(ZoneId.of(JacksonDateFormatConfig.UTC_ZONE_ID))
                .withZoneSameInstant(institutionZoneId)
                .toLocalDateTime();
        LOG.debug("Output -> {}", result);
        return result;
    }

    private void setDetailsDescriptions(TriageBo triage) {
        LOG.debug("Input parameter -> triage {}", triage);
        if (triage.getOtherRiskFactors().getBodyTemperatureId() != null)
            triage.getOtherRiskFactors().setBodyTemperatureDescription(EBodyTemperature.getById(triage.getOtherRiskFactors().getBodyTemperatureId()).getDescription());
        if (triage.getOtherRiskFactors().getMuscleHypertoniaId() != null)
            triage.getOtherRiskFactors().setMuscleHypertoniaDescription(EMuscleHypertonia.getById(triage.getOtherRiskFactors().getMuscleHypertoniaId()).getDescription());
        if (triage.getOtherRiskFactors().getRespiratoryRetractionId() != null)
            triage.getOtherRiskFactors().setRespiratoryRetractionDescription(ERespiratoryRetraction.getById(triage.getOtherRiskFactors().getRespiratoryRetractionId()).getDescription());
        if (triage.getOtherRiskFactors().getPerfusionId() != null)
            triage.getOtherRiskFactors().setPerfusionDescription(EPerfusion.getById(triage.getOtherRiskFactors().getPerfusionId()).getDescription());
    }

    @Override
    public TriageBo createAdministrative(TriageBo triageBo, Integer institutionId) {
        return persistTriage(triageBo, institutionId, getAdministrativeConsumer());
    }

    @Override
    public TriageBo createAdultGynecological(TriageBo triageBo, Integer institutionId) {
		TriageBo result = persistTriage(triageBo, institutionId, getAdultConsumer());
		addTriageReasons(triageBo.getReasons(), result.getTriageId());
        return result;
    }

    @Override
    public TriageBo createPediatric(TriageBo triageBo, Integer institutionId) {
		TriageBo result = persistTriage(triageBo, institutionId, getPediatricConsumer());
		addTriageReasons(triageBo.getReasons(), result.getTriageId());
		return result;
    }
	@Transactional
    private TriageBo persistTriage(TriageBo triageBo, Integer institutionId, Consumer<TriageBo> consumer){
        LOG.debug("Input parameter -> triageBo {}, institutionId{}", triageBo, institutionId);
        validTriage(triageBo, institutionId);
        Triage triage = triageRepository.save(new Triage(triageBo));
        triageBo.setTriageId(triage.getId());
		triageBo.setInstitutionId(institutionId);
		triageBo.setEncounterId(triageBo.getEmergencyCareEpisodeId());
		triageBo.setMedicalCoverageId(emergencyCareEpisodeRepository.getPatientMedicalCoverageIdByEpisodeId(triageBo.getEmergencyCareEpisodeId()));
		setTriagePlace(triageBo);

        consumer.accept(triageBo);

        Integer episodeId = triageBo.getEmergencyCareEpisodeId();
        Short categoryId = triageBo.getCategoryId();
        this.setTriageCategoryId(episodeId, categoryId);

		Long documentId = documentFactory.run(triageBo, true);
		documentService.createDocumentTriage(documentId, triage.getId());

        LOG.debug("Output -> {}", triageBo);
        return triageBo;
    }

    private void validTriage(TriageBo triageBo, Integer institutionId) {
        EEmergencyCareState ems = emergencyCareEpisodeStateService.getState(triageBo.getEmergencyCareEpisodeId(), institutionId);
        Assert.isTrue(EEmergencyCareState.ESPERA.getId().equals(ems.getId()) || EEmergencyCareState.ATENCION.getId().equals(ems.getId()), "care-episode.invalid-triage");
    }

    private Consumer<TriageBo> getAdultConsumer() {
        return triageBo -> saveRiskFactors(triageBo.getTriageId(), triageBo.getRiskFactorIds());
    }

    private Consumer<TriageBo> getPediatricConsumer() {
        return triageBo -> {
            if (existDetails(triageBo)) {
				triageDetailsRepository.save(new TriageDetails(triageBo));
				setDetailsDescriptions(triageBo);
			}
			saveRiskFactors(triageBo.getTriageId(), triageBo.getRiskFactorIds());
        };
    }

    private Consumer<TriageBo> getAdministrativeConsumer() {
        return triageBo -> {};
    }


    private boolean existDetails(TriageBo triageBo) {
        LOG.debug("Input parameter -> triageBo {}", triageBo);
        boolean result =  (triageBo.getOtherRiskFactors().getBodyTemperatureId() != null) ||
                (triageBo.getOtherRiskFactors().getCryingExcessive() != null) ||
                (triageBo.getOtherRiskFactors().getMuscleHypertoniaId() != null) ||
                (triageBo.getOtherRiskFactors().getRespiratoryRetractionId() != null) ||
                (triageBo.getOtherRiskFactors().getStridor() != null) ||
                (triageBo.getOtherRiskFactors().getPerfusionId() != null);
        LOG.debug("Output -> {}", result);
        return result;
    }

    private void saveRiskFactors(Integer triageId, List<Integer> riskFactorIds) {
        LOG.debug("Input parameters -> triageId {}, riskFactorIds {}", triageId, riskFactorIds);
        riskFactorIds.forEach(id -> triageRiskFactorsRepository.save(new TriageRiskFactors(triageId, id)));
    }

    private Boolean setTriageCategoryId(Integer episodeId, Short triageCategoryId) {
        LOG.debug("Input parameters -> episodeId {}, triageCategoryId {}",
                episodeId, triageCategoryId);
        emergencyCareEpisodeRepository.updateTriageCategoryId(episodeId, triageCategoryId);
        return true;
    }

	private void setTriagePlace(TriageBo triage) {
		Integer shockRoomId = emergencyCareEpisodeRepository.getEmergencyCareEpisodeShockroomId(triage.getEmergencyCareEpisodeId());
		Integer roomId = emergencyCareEpisodeRepository.getRoomId(triage.getEmergencyCareEpisodeId());
		Integer doctorsOfficeId = emergencyCareEpisodeRepository.getEmergencyCareEpisodeDoctorsOfficeId(triage.getEmergencyCareEpisodeId());
		if (shockRoomId != null) {
			triage.setShockRoomId(shockRoomId);
			triage.setSectorId(shockroomRepository.getSectorId(shockRoomId));
		}

		if (roomId != null) {
			triage.setRoomId(roomId);
			triage.setSectorId(roomRepository.getSectorId(roomId));
		}

		if (doctorsOfficeId != null) {
			triage.setDoctorsOfficeId(doctorsOfficeId);
			triage.setSectorId(doctorsOfficeRepository.getSectorId(doctorsOfficeId));
		}
	}

	@Override
	public void addTriageReasons(List<ReasonBo> reasons, Integer triageId){
		if (reasons != null && !reasons.isEmpty())
			addTriageReasons.run(triageId, reasons);
	}
}
