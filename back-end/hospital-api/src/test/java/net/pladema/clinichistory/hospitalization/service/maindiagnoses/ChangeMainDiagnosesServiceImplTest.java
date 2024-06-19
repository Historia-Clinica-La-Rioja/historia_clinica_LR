package net.pladema.clinichistory.hospitalization.service.maindiagnoses;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.FetchHospitalizationHealthConditionState;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentObservationsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.files.pdf.GeneratedPdfResponseService;
import java.util.Collections;
import javax.validation.ConstraintViolationException;
import net.pladema.UnitRepository;
import net.pladema.clinichistory.hospitalization.application.fetchEpisodeDocumentTypeById.FetchEpisodeDocumentTypeById;
import net.pladema.clinichistory.hospitalization.application.port.AnestheticStorage;
import net.pladema.clinichistory.hospitalization.application.port.InternmentEpisodeStorage;
import net.pladema.clinichistory.hospitalization.application.validateadministrativedischarge.ValidateAdministrativeDischarge;
import net.pladema.clinichistory.hospitalization.repository.EvolutionNoteDocumentRepository;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.hospitalization.repository.PatientDischargeRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.impl.InternmentEpisodeServiceImpl;
import net.pladema.clinichistory.hospitalization.service.maindiagnoses.domain.MainDiagnosisBo;
import net.pladema.establishment.repository.MedicalCoveragePlanRepository;
import net.pladema.establishment.service.InstitutionService;
import net.pladema.patient.service.PatientService;
import net.pladema.person.service.PersonService;
import net.pladema.staff.application.getlicensenumberbyprofessional.GetLicenseNumberByProfessional;
import net.pladema.staff.service.HealthcareProfessionalService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChangeMainDiagnosesServiceImplTest extends UnitRepository {

    private ChangeMainDiagnosesService changeMainDiagnosesService;

    @Autowired
    private InternmentEpisodeRepository internmentEpisodeRepository;

    @Autowired
    private EvolutionNoteDocumentRepository evolutionNoteDocumentRepository;

    @Autowired
    private PatientDischargeRepository patientDischargeRepository;

	@Autowired
	private MedicalCoveragePlanRepository medicalCoveragePlanRepository;

	@Mock
	private DateTimeProvider dateTimeProvider;

    @Mock
    private DocumentService documentService;

    @Mock
    private DocumentFactory documentFactory;

    @Mock
    private FetchHospitalizationHealthConditionState fetchHospitalizationHealthConditionState;

	@Mock
	private InternmentEpisodeStorage internmentEpisodeStorage;

	@Mock
	private FeatureFlagsService featureFlagsService;

	@MockBean
	private DocumentFileRepository documentFileRepository;

	@Mock
	private GeneratedPdfResponseService generatedPdfResponseService;

	@Mock
	private PatientService patientService;

	@Mock
	private PersonService personService;

	@Mock
	private InstitutionService institutionService;

	@Mock
	private FetchEpisodeDocumentTypeById fetchEpisodeDocumentTypeById;

	@Mock
	private HealthcareProfessionalService healthcareProfessionalService;

	@Mock
	private GetLicenseNumberByProfessional getLicenseNumberByProfessional;

    @Mock
    private AnestheticStorage anestheticStorage;

    @Mock
    private ValidateAdministrativeDischarge validateAdministrativeDischarge;

    @BeforeEach
    void setUp(){
        var internmentEpisodeService = new InternmentEpisodeServiceImpl(
				internmentEpisodeRepository,
				evolutionNoteDocumentRepository,
				patientDischargeRepository,
				medicalCoveragePlanRepository,
				documentService,
				internmentEpisodeStorage,
				featureFlagsService,
				generatedPdfResponseService,
				patientService,
				personService,
				institutionService,
				fetchEpisodeDocumentTypeById,
				healthcareProfessionalService,
				getLicenseNumberByProfessional,
                anestheticStorage,
                validateAdministrativeDischarge
        );
        changeMainDiagnosesService = new ChangeMainDiagnosesServiceImpl(
                documentFactory,
                internmentEpisodeService,
                fetchHospitalizationHealthConditionState,
				dateTimeProvider);
    }

    @Test
    void createDocumentWithEpisodeThatNotExists() {
        Exception exception = Assertions.assertThrows(NotFoundException.class, () ->
                changeMainDiagnosesService.execute(validMainDiagnosisBo(9, 10))
        );
        String expectedMessage = "internmentepisode.not.found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInvalidInstitutionId() {
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                changeMainDiagnosesService.execute(validMainDiagnosisBo(null, 8))
        );
        String expectedMessage = "El id de la institución es obligatorio";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInvalidEpisodeId() {
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                changeMainDiagnosesService.execute(validMainDiagnosisBo(8, null))
        );
        String expectedMessage = "El id del encuentro asociado es obligatorio";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInternmentInOtherInstitution() {
        var internmentEpisode = save(newInternmentEpisodeWithEpicrisis(null));
        Exception exception = Assertions.assertThrows(NotFoundException.class, () ->
                changeMainDiagnosesService.execute(validMainDiagnosisBo(internmentEpisode.getInstitutionId()+1, internmentEpisode.getId()))
        );
        String expectedMessage = "internmentepisode.not.found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithEpicrisis() {
		var epicrisisDoc = save (new Document(1, DocumentStatus.FINAL, DocumentType.EPICRISIS, SourceType.HOSPITALIZATION, -1, -1));
        var internmentEpisode = save(newInternmentEpisodeWithEpicrisis(epicrisisDoc.getId()));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                changeMainDiagnosesService.execute(validMainDiagnosisBo(8, internmentEpisode.getId()))
        );
        String expectedMessage = "Esta internación ya posee una epicrisis";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocument_withoutMainDiagnosis() {
        var internmentEpisode = save(newInternmentEpisodeWithEpicrisis(null));
        var mainDiagnosisBo = validMainDiagnosisBo(internmentEpisode.getInstitutionId(), internmentEpisode.getId());
        mainDiagnosisBo.setMainDiagnosis(null);
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                changeMainDiagnosesService.execute(mainDiagnosisBo)
        );
        String expectedMessage = "mainDiagnosis: {diagnosis.mandatory}";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage,expectedMessage);
    }


    private MainDiagnosisBo validMainDiagnosisBo(Integer institutionId, Integer encounterId){
        var result = new MainDiagnosisBo();
        result.setInstitutionId(institutionId);
        result.setEncounterId(encounterId);
        result.setNotes(new DocumentObservationsBo());
        result.setMainDiagnosis(new HealthConditionBo(new SnomedBo("MAIN", "MAIN")));
        result.setDiagnosis(Collections.emptyList());
        return result;
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
}