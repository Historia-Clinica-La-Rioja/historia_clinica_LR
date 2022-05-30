package net.pladema.clinichistory.hospitalization.service.evolutionnote.impl;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.FetchHospitalizationHealthConditionState;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTermsValidatorUtils;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.documents.validation.AnthropometricDataValidator;
import net.pladema.clinichistory.hospitalization.service.documents.validation.EffectiveRiskFactorTimeValidator;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.CreateEvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;

import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.exceptions.PermissionDeniedException;
import net.pladema.sgx.session.infrastructure.input.service.FetchLoggedUserRolesExternalService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class CreateEvolutionNoteServiceImpl implements CreateEvolutionNoteService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateEvolutionNoteServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentFactory documentFactory;

    private final InternmentEpisodeService internmentEpisodeService;

    private final FetchHospitalizationHealthConditionState fetchHospitalizationHealthConditionState;

    private final DateTimeProvider dateTimeProvider;

	private final FetchLoggedUserRolesExternalService fetchLoggedUserRolesExternalService;

    public CreateEvolutionNoteServiceImpl(DocumentFactory documentFactory,
										  InternmentEpisodeService internmentEpisodeService,
										  FetchHospitalizationHealthConditionState fetchHospitalizationHealthConditionState,
										  DateTimeProvider dateTimeProvider,
										  FetchLoggedUserRolesExternalService fetchLoggedUserRolesExternalService) {
        this.documentFactory = documentFactory;
        this.internmentEpisodeService = internmentEpisodeService;
        this.fetchHospitalizationHealthConditionState = fetchHospitalizationHealthConditionState;
        this.dateTimeProvider = dateTimeProvider;
		this.fetchLoggedUserRolesExternalService = fetchLoggedUserRolesExternalService;
	}

    @Override
    public EvolutionNoteBo execute(EvolutionNoteBo evolutionNote) {
        LOG.debug("Input parameters -> evolutionNote {}", evolutionNote);

		assertContextValid(evolutionNote);
		validateNursePermissionToLoadProcedures(evolutionNote);

        var internmentEpisode = internmentEpisodeService
                .getInternmentEpisode(evolutionNote.getEncounterId(), evolutionNote.getInstitutionId());
        evolutionNote.setPatientId(internmentEpisode.getPatientId());

        LocalDateTime now = dateTimeProvider.nowDateTime();
        evolutionNote.setPerformedDate(now);

        assertDoesNotHaveEpicrisis(internmentEpisode);
        assertEvolutionNoteValid(evolutionNote);
        assertEffectiveRiskFactorTimeValid(evolutionNote, internmentEpisode.getEntryDate());
        assertAnthropometricData(evolutionNote);

		HealthConditionBo mainDiagnosis = fetchHospitalizationHealthConditionState.getMainDiagnosisGeneralState(internmentEpisode.getId());
		if (mainDiagnosis != null)
			assertDiagnosisValid(evolutionNote, internmentEpisode, mainDiagnosis);

		evolutionNote = verifyEvolutionNoteDiagnosis(evolutionNote, mainDiagnosis);

        evolutionNote.setId(documentFactory.run(evolutionNote, true));

        internmentEpisodeService.addEvolutionNote(internmentEpisode.getId(), evolutionNote.getId());

        LOG.debug(OUTPUT, evolutionNote);

        return evolutionNote;
    }

	private void validateNursePermissionToLoadProcedures(EvolutionNoteBo evolutionNote) {
		var institutionId = evolutionNote.getInstitutionId();

		var roles = fetchLoggedUserRolesExternalService.execute().collect(Collectors.toList());
		var isNurse = roles.stream().anyMatch(r -> r.institutionId.equals(institutionId)
				&& (r.role.equals(ERole.ENFERMERO)
				|| r.role.equals(ERole.ENFERMERO_ADULTO_MAYOR))
		);
		var isProfessionalOrSpecialist = roles.stream().anyMatch(r -> r.institutionId.equals(institutionId)
				&& (r.role.equals(ERole.ESPECIALISTA_MEDICO)
				|| r.role.equals(ERole.PROFESIONAL_DE_SALUD)
				|| r.role.equals(ERole.ESPECIALISTA_EN_ODONTOLOGIA))
		);

		if(evolutionNote.getProcedures() != null && !evolutionNote.getProcedures().isEmpty()
				&& isNurse
				&& ! isProfessionalOrSpecialist) {
			throw new PermissionDeniedException(
					String.format("Los usuarios con roles %s y %s no tienen permiso para agregar un procedimiento a una nota de evolución",
					ERole.ENFERMERO.getValue(), ERole.ENFERMERO_ADULTO_MAYOR.getValue()));
		}
	}

	private void assertContextValid(EvolutionNoteBo evolutionNote) {
		if (evolutionNote.getInstitutionId() == null)
			throw new ConstraintViolationException("El id de la institución es obligatorio", Collections.emptySet());
		if (evolutionNote.getEncounterId() == null)
			throw new ConstraintViolationException("El id del encuentro asociado es obligatorio", Collections.emptySet());
	}

    private void assertEvolutionNoteValid(EvolutionNoteBo evolutionNote) {
        evolutionNote.validateSelf();
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(evolutionNote.getDiagnosis()))
            throw new ConstraintViolationException("Diagnósticos secundarios repetidos", Collections.emptySet());
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(evolutionNote.getProcedures()))
            throw new ConstraintViolationException("Procedimientos repetidos", Collections.emptySet());
    }

    private void assertAnthropometricData(EvolutionNoteBo evolutionNoteBo) {
        var validator = new AnthropometricDataValidator();
        validator.isValid(evolutionNoteBo);
    }

    private void assertDoesNotHaveEpicrisis(InternmentEpisode internmentEpisode) {
        if(internmentEpisode.getEpicrisisDocId() != null) {
            throw new ConstraintViolationException("Esta internación ya posee una epicrisis", Collections.emptySet());
        }
    }

    private void assertDiagnosisValid(EvolutionNoteBo evolutionNote, InternmentEpisode internmentEpisode, HealthConditionBo mainDiagnosis) {
		if (evolutionNote.getMainDiagnosis() != null && (evolutionNote.getDiagnosis() == null || evolutionNote.getDiagnosis().isEmpty() || !evolutionNote.getMainDiagnosis().getSnomed().equals(mainDiagnosis.getSnomed())))
			return;
		if (evolutionNote.getDiagnosis().stream()
                .map(DiagnosisBo::getSnomed)
                .anyMatch(d -> d.equals(mainDiagnosis.getSnomed()))) {
            throw new ConstraintViolationException("Diagnostico principal duplicado en los secundarios", Collections.emptySet());
        }
    }

    private void assertEffectiveRiskFactorTimeValid(EvolutionNoteBo evolutionNote, LocalDateTime entryDate) {
        var validator = new EffectiveRiskFactorTimeValidator();
        validator.isValid(evolutionNote, entryDate);
    }

    private EvolutionNoteBo verifyEvolutionNoteDiagnosis(EvolutionNoteBo evolutionNote, HealthConditionBo mainDiagnosis) {
    	if (evolutionNote.getDiagnosis().isEmpty() && mainDiagnosis != null)
			evolutionNote.setMainDiagnosis(new DiagnosisBo(mainDiagnosis.getSnomed()));
		return evolutionNote;
    }

}
