package net.pladema.clinichistory.outpatient.createoutpatient.service;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientDocumentBo;
import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(showSql = false)
class CreateOutpatientDocumentServiceImplTest {

    private CreateOutpatientDocumentService createOutpatientDocumentService;

    @Autowired
    private OutpatientConsultationRepository outpatientConsultationRepository;

    @Mock
    private DocumentFactory documentFactory;

    @BeforeEach
    public void setUp() {
        var updateOutpatientConsultationService = new UpdateOutpatientDocumentServiceImpl(outpatientConsultationRepository);
        createOutpatientDocumentService =
                new CreateOutpatientDocumentServiceImpl(documentFactory, updateOutpatientConsultationService);
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

    private OutpatientDocumentBo validOutpatientConsultation(Integer institutionId, Integer encounterId){
        var result = new OutpatientDocumentBo();
        result.setInstitutionId(institutionId);
        result.setEncounterId(encounterId);
        return result;
    }

}