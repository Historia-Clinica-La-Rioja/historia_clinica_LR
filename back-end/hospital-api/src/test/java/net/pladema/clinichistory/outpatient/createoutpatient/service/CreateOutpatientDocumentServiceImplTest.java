package net.pladema.clinichistory.outpatient.createoutpatient.service;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.ips.AllergyConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthHistoryConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProblemBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.UnitRepository;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientDocumentBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.exceptions.CreateOutpatientDocumentException;
import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateOutpatientDocumentServiceImplTest extends UnitRepository {

    private CreateOutpatientDocumentService createOutpatientDocumentService;

    @Autowired
    private OutpatientConsultationRepository outpatientConsultationRepository;

    @Mock
    private DocumentFactory documentFactory;

    @Mock
    private DateTimeProvider dateTimeProvider;

    @BeforeEach
    public void setUp() {
        var updateOutpatientConsultationService = new UpdateOutpatientDocumentServiceImpl(outpatientConsultationRepository);
        createOutpatientDocumentService =
                new CreateOutpatientDocumentServiceImpl(documentFactory, updateOutpatientConsultationService, dateTimeProvider);
    }

    @Test
    void createDocumentWithInvalidInstitutionId() {
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createOutpatientDocumentService.execute(validOutpatientConsultation(null, 8))
        );
        String expectedMessage = "El id de la institución es obligatorio";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInvalidEpisodeId() {
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createOutpatientDocumentService.execute(validOutpatientConsultation(8, null))
        );
        String expectedMessage = "El id del encuentro asociado es obligatorio";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }
    @Test
    void invalidDocumentRepeatedClinicalTerms() {
        var outpatientDocumentBo = validOutpatientConsultation(8, 5);
        outpatientDocumentBo.setReasons(List.of(new ReasonBo(new SnomedBo("SCTID", "PT")), new ReasonBo(new SnomedBo("SCTID", "PT"))));
        outpatientDocumentBo.setProblems(List.of(new ProblemBo(new SnomedBo("SCTID", "PT")), new ProblemBo(new SnomedBo("SCTID", "PT"))));
        outpatientDocumentBo.setFamilyHistories(List.of(new HealthHistoryConditionBo(new SnomedBo("SCTID", "PT")), new HealthHistoryConditionBo(new SnomedBo("SCTID", "PT"))));
        outpatientDocumentBo.setAllergies(List.of(new AllergyConditionBo(new SnomedBo("SCTID", "PT")), new AllergyConditionBo(new SnomedBo("SCTID", "PT"))));
        outpatientDocumentBo.setProcedures(List.of(new ProcedureBo(new SnomedBo("SCTID", "PT")), new ProcedureBo(new SnomedBo("SCTID", "PT"))));
        CreateOutpatientDocumentException exception = Assertions.assertThrows(CreateOutpatientDocumentException.class, () ->
                createOutpatientDocumentService.execute(outpatientDocumentBo)
        );
        assertEquals(5, exception.getMessages().size());
        assertTrue(exception.getMessages().containsAll(List.of("Problemas médicos repetidos",
                "Antecedentes familiares repetidos", "Alergias repetidas", "Motivos repetidos",
                "Procedimientos repetidos")));

    }

    private OutpatientDocumentBo validOutpatientConsultation(Integer institutionId, Integer encounterId){
        var result = new OutpatientDocumentBo();
        result.setInstitutionId(institutionId);
        result.setEncounterId(encounterId);
        return result;
    }

}