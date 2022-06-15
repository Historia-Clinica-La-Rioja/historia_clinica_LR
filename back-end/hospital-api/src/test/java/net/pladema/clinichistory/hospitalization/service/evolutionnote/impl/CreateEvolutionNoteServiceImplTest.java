package net.pladema.clinichistory.hospitalization.service.evolutionnote.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.FetchHospitalizationHealthConditionState;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalObservationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.UnitRepository;
import net.pladema.clinichistory.hospitalization.repository.EvolutionNoteDocumentRepository;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeStorage;
import net.pladema.clinichistory.hospitalization.repository.PatientDischargeRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.CreateEvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.EvolutionNoteValidator;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
import net.pladema.clinichistory.hospitalization.service.impl.InternmentEpisodeServiceImpl;
import net.pladema.establishment.repository.MedicalCoveragePlanRepository;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.sgx.exceptions.PermissionDeniedException;
import net.pladema.sgx.session.infrastructure.input.service.FetchLoggedUserRolesExternalService;

class CreateEvolutionNoteServiceImplTest extends UnitRepository {

    private CreateEvolutionNoteService createEvolutionNoteService;

    @Autowired
    private InternmentEpisodeRepository internmentEpisodeRepository;

    @Autowired
    private EvolutionNoteDocumentRepository evolutionNoteDocumentRepository;

    @Autowired
    private PatientDischargeRepository patientDischargeRepository;

	@Autowired
	private MedicalCoveragePlanRepository medicalCoveragePlanRepository;

	@Mock
	DateTimeProvider dateTimeProvider;

    @Mock
    private DocumentService documentService;

    @Mock
    private DocumentFactory documentFactory;

    @Mock
    private FetchHospitalizationHealthConditionState fetchHospitalizationHealthConditionState;

	@Mock
	private FetchLoggedUserRolesExternalService fetchLoggedUserRolesExternalService;

	@Mock
	private InternmentEpisodeStorage internmentEpisodeStorage;
	@Mock
	private FeatureFlagsService featureFlagsService;

	private EvolutionNoteValidator evolutionNoteValidator;

    @BeforeEach
    void setUp(){
        var internmentEpisodeService = new InternmentEpisodeServiceImpl(
                internmentEpisodeRepository,
                dateTimeProvider,
				evolutionNoteDocumentRepository,
                patientDischargeRepository,
                documentService,
                medicalCoveragePlanRepository,
                internmentEpisodeStorage, featureFlagsService);
        createEvolutionNoteService = new CreateEvolutionNoteServiceImpl(
                documentFactory,
                internmentEpisodeService,
                fetchHospitalizationHealthConditionState,
                dateTimeProvider, new EvolutionNoteValidator(fetchLoggedUserRolesExternalService, internmentEpisodeService));
    }

    @Test
    void createDocumentWithEpisodeThatNotExists() {
        Exception exception = Assertions.assertThrows(NotFoundException.class, () ->
                createEvolutionNoteService.execute(validEvolutionNote(9, 14))
        );
        String expectedMessage = "internmentepisode.not.found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInvalidInstitutionId() {
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(validEvolutionNote(null, 8))
        );
        String expectedMessage = "El id de la institución es obligatorio";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInvalidEpisodeId() {
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(validEvolutionNote(8, null))
        );
        String expectedMessage = "El id del encuentro asociado es obligatorio";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInternmentInOtherInstitution() {
        var internmentEpisode = save(newInternmentEpisodeWithEpicrisis(null));
        Exception exception = Assertions.assertThrows(NotFoundException.class, () ->
                createEvolutionNoteService.execute(validEvolutionNote(internmentEpisode.getInstitutionId()+1, internmentEpisode.getId()))
        );
        String expectedMessage = "internmentepisode.not.found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithEpicrisis() {
		var epicrisisDoc = save (new Document(1, DocumentStatus.FINAL, DocumentType.EPICRISIS, SourceType.HOSPITALIZATION));
		var internmentEpisode = save(newInternmentEpisodeWithEpicrisis(epicrisisDoc.getId()));
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
		        createEvolutionNoteService.execute(validEvolutionNote(8, internmentEpisode.getId()))
		);
        String expectedMessage = "Esta internación ya posee una epicrisis";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocument_withMainDiagnosisDuplicated() {
        var internmentEpisode = save(newInternmentEpisodeWithEpicrisis(null));
        var evolutionNote  = validEvolutionNote(internmentEpisode.getInstitutionId(), internmentEpisode.getId());
        evolutionNote.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo("SECONDARY", "SECONDARY")), new DiagnosisBo(new SnomedBo("MAIN", "MAIN"))));

        when(fetchHospitalizationHealthConditionState.getMainDiagnosisGeneralState(any())).thenReturn(new HealthConditionBo(new SnomedBo("MAIN", "MAIN")));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(evolutionNote)
        );
        String expectedMessage = "Diagnostico principal duplicado en los secundarios";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createDocumentWithInvalidDiagnosis() {
        var internmentEpisode = newInternmentEpisodeWithEpicrisis(null);
        internmentEpisode.setEntryDate(LocalDateTime.of(2020,10,10,00,00,00));
        internmentEpisode = save(internmentEpisode);

        var evolutionNote = validEvolutionNote(internmentEpisode.getInstitutionId(), internmentEpisode.getId());

        evolutionNote.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo("", ""))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(evolutionNote)
        );


        evolutionNote.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo(null, null))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(evolutionNote)
        );

        evolutionNote.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo("REPEATED", "REPEATED")),
                new DiagnosisBo(new SnomedBo("REPEATED", "REPEATED"))));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(evolutionNote)
        );
        String expectedMessage = "Diagnósticos secundarios repetidos";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInvalidImmunizations() {
        var internmentEpisode = newInternmentEpisodeWithEpicrisis(null);
        internmentEpisode.setEntryDate(LocalDateTime.of(2020,10,10,00,00,00));
        internmentEpisode = save(internmentEpisode);

        var evolutionNote = validEvolutionNote(internmentEpisode.getInstitutionId(), internmentEpisode.getId());

        evolutionNote.setImmunizations(List.of(new ImmunizationBo(new SnomedBo("", ""))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(evolutionNote)
        );

        evolutionNote.setImmunizations(List.of(new ImmunizationBo(new SnomedBo(null, null))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(evolutionNote)
        );
    }

    @Test
    void createDocumentWithInvalidProcedures() {
        var internmentEpisode = newInternmentEpisodeWithEpicrisis(null);
        internmentEpisode.setEntryDate(LocalDateTime.of(2020,10,10,00,00,00));
        internmentEpisode = save(internmentEpisode);

        var evolutionNote = validEvolutionNote(internmentEpisode.getInstitutionId(), internmentEpisode.getId());

        evolutionNote.setProcedures(List.of(new ProcedureBo(new SnomedBo(null, null))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(evolutionNote)
        );

        evolutionNote.setProcedures(List.of(new ProcedureBo(new SnomedBo("REPEATED", "REPEATED")),
                new ProcedureBo(new SnomedBo("REPEATED", "REPEATED"))));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(evolutionNote)
        );
        Assertions.assertTrue(exception.getMessage().contains("Procedimientos repetidos"));
    }

    @Test
    void createDocumentWithInvalidAnthropometricData() {
        var internmentEpisode = newInternmentEpisodeWithEpicrisis(null);
        internmentEpisode.setEntryDate(LocalDateTime.of(2020,10,10,00,00,00));
        internmentEpisode = save(internmentEpisode);

        var evolutionNote = validEvolutionNote(internmentEpisode.getInstitutionId(), internmentEpisode.getId());

        LocalDateTime localDateTime = LocalDateTime.of(
                LocalDate.of(2020, 10,29),
                LocalTime.of(11,20));

        evolutionNote.setAnthropometricData(newAnthropometricData("10001", localDateTime));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(evolutionNote)
        );
        Assertions.assertTrue(exception.getMessage().contains("peso: La medición debe estar entre 0.0 y 1000.0"));

        evolutionNote.setAnthropometricData(newAnthropometricData("-50", null));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(evolutionNote)
        );
        Assertions.assertTrue(exception.getMessage().contains("peso: La medición debe estar entre 0.0 y 1000.0"));
    }

    @Test
    void createDocumentWithInvalidRiskFactor() {
        var internmentEpisode = newInternmentEpisodeWithEpicrisis(null);
        internmentEpisode.setEntryDate(LocalDateTime.of(2020,10,10,00,00,00));
        internmentEpisode = save(internmentEpisode);

        var evolutionNote = validEvolutionNote(internmentEpisode.getInstitutionId(), internmentEpisode.getId());
        LocalDateTime localDateTime = LocalDateTime.of(
                LocalDate.of(2020, 10,29),
                LocalTime.of(11,20));
        evolutionNote.setRiskFactors(newRiskFactors(null, localDateTime));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(evolutionNote)
        );
        Assertions.assertTrue(exception.getMessage().contains("riskFactors.bloodOxygenSaturation.value: {value.mandatory}"));

        evolutionNote.setRiskFactors(newRiskFactors("Value", LocalDateTime.of(2020,9,9,1,5,6)));
        exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(evolutionNote)
        );
        Assertions.assertTrue(exception.getMessage().contains("Saturación de oxigeno: La fecha de medición debe ser posterior a la fecha de internación"));
    }

	@Test
	void createEvolutionNoteWithProceduresWithRoleNurse(){
		var evolutionNote = new EvolutionNoteBo();
		evolutionNote.setInstitutionId(1);
		evolutionNote.setEncounterId(1);

		var roleNurse = new RoleAssignment(ERole.ENFERMERO, evolutionNote.getInstitutionId());
		var roleList = new ArrayList<RoleAssignment>();
		roleList.add(roleNurse);

		var procedure = new ProcedureBo(new ProcedureSummaryBo(1, new SnomedBo(new Snomed("1", "1", "1", "1")), LocalDate.now(), 1));
		var procedureList = new ArrayList<ProcedureBo>();
		procedureList.add(procedure);
		evolutionNote.setProcedures(procedureList);

		Mockito.when(fetchLoggedUserRolesExternalService.execute()).thenReturn(
			roleList.stream());
		var exception = Assertions.assertThrows(PermissionDeniedException.class, () ->
				createEvolutionNoteService.execute(evolutionNote)
		);
		Assertions.assertTrue(exception.getMessage().contains(
				String.format("Los usuarios con roles %s y %s no tienen permiso para agregar un procedimiento a una nota de evolución",
						ERole.ENFERMERO.getValue(), ERole.ENFERMERO_ADULTO_MAYOR.getValue())));
	}

	@Test
	void createEvolutionNoteWithProceduresWithRoleOlderAdultNurse(){
		var evolutionNote = new EvolutionNoteBo();
		evolutionNote.setInstitutionId(1);
		evolutionNote.setEncounterId(1);

		var roleOlderAdultNurse = new RoleAssignment(ERole.ENFERMERO_ADULTO_MAYOR, evolutionNote.getInstitutionId());
		var roleList = new ArrayList<RoleAssignment>();
		roleList.add(roleOlderAdultNurse);

		var procedure = new ProcedureBo(new ProcedureSummaryBo(1, new SnomedBo(new Snomed("1", "1", "1", "1")), LocalDate.now(), 1));
		var procedureList = new ArrayList<ProcedureBo>();
		procedureList.add(procedure);
		evolutionNote.setProcedures(procedureList);

		Mockito.when(fetchLoggedUserRolesExternalService.execute()).thenReturn(
				roleList.stream());
		var exception = Assertions.assertThrows(PermissionDeniedException.class, () ->
				createEvolutionNoteService.execute(evolutionNote)
		);
		Assertions.assertTrue(exception.getMessage().contains(
				String.format("Los usuarios con roles %s y %s no tienen permiso para agregar un procedimiento a una nota de evolución",
						ERole.ENFERMERO.getValue(), ERole.ENFERMERO_ADULTO_MAYOR.getValue())));
	}

    private InternmentEpisode newInternmentEpisodeWithEpicrisis(Long epicrisisId) {
        InternmentEpisode internmentEpisode = new InternmentEpisode();
        internmentEpisode.setPatientId(1);
        internmentEpisode.setBedId(1);
        internmentEpisode.setStatusId((short) 1);
        internmentEpisode.setEpicrisisDocId(epicrisisId);
        internmentEpisode.setInstitutionId(8);
        return internmentEpisode;
    }

    private EvolutionNoteBo validEvolutionNote(Integer institutionId, Integer encounterId){
        var result = new EvolutionNoteBo();
        result.setInstitutionId(institutionId);
        result.setEncounterId(encounterId);
        result.setMainDiagnosis(new HealthConditionBo(new SnomedBo("MAIN", "MAIN")));
        result.setDiagnosis(Lists.emptyList());
        result.setImmunizations(Lists.emptyList());
        result.setAllergies(Lists.emptyList());
        return result;
    }

    private RiskFactorBo newRiskFactors(String value, LocalDateTime time) {
        var vs = new RiskFactorBo();
        vs.setBloodOxygenSaturation(new ClinicalObservationBo(null, value, time));
        return vs;
    }

    private AnthropometricDataBo newAnthropometricData(String value, LocalDateTime time) {
        var adb = new AnthropometricDataBo();
        adb.setBloodType(new ClinicalObservationBo(null, value, time));
        adb.setWeight(new ClinicalObservationBo(null, value, time));
        return adb;
    }

}