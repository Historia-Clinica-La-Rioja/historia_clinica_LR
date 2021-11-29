package net.pladema.snvs.infrastructure.output.rest.report;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.snvs.infrastructure.output.repository.snvs.ManualClassificationRepository;
import net.pladema.snvs.application.ports.report.ReportPort;
import net.pladema.snvs.application.ports.report.exceptions.ReportPortException;
import net.pladema.snvs.domain.event.SnvsEventInfoBo;
import net.pladema.snvs.domain.event.exceptions.SnvsEventInfoBoException;
import net.pladema.snvs.domain.institution.InstitutionDataBo;
import net.pladema.snvs.domain.patient.PatientDataBo;
import net.pladema.snvs.domain.patient.PersonDataBo;
import net.pladema.snvs.domain.report.SnvsToReportBo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:snvs-e2e-test.properties")
@Disabled
class ReportPortImplE2ETest {

    @Autowired
    private ReportPort reportPort;

    @MockBean
    private SharedPatientPort sharedPatientPort;

    @MockBean
    private SharedInstitutionPort sharedInstitutionPort;

    @MockBean
    private ManualClassificationRepository manualClassificationRepository;

    @MockBean
    private DateTimeProvider dateTimeProvider;

    @Test
    void nullReport() {
        Exception exception = Assertions.assertThrows(ReportPortException.class, () ->
                reportPort.run(null)
        );
        assertEquals("La información a reportar es obligatoria", exception.getMessage());
    }

    @Test
    void success() throws ReportPortException, SnvsEventInfoBoException {
        var result = reportPort.run(buildMock());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(999, result.getEventId());
        Assertions.assertEquals(999, result.getGroupEventId());
        Assertions.assertEquals(999, result.getManualClassificationId());
        Assertions.assertEquals(-1, result.getPatientId());
        Assertions.assertEquals((short)400, result.getResponseCode());
        Assertions.assertEquals(LocalDate.of(2020,11,26), result.getLastUpdate());
    }

    private SnvsToReportBo buildMock() throws SnvsEventInfoBoException {
        return new SnvsToReportBo(new SnvsEventInfoBo(999,999,999,1),
                LocalDate.of(2020,11,26),
                new PatientDataBo(-1, mockPerson()),
                new InstitutionDataBo(-20, "10064902100232"));
    }

    private PersonDataBo mockPerson() {
        return new PersonDataBo("PLADEMA_NOMBRE", "PLADEMA_APELLIDO", (short)1,
                "99999999",
                null, LocalDate.of(9999,12,12),
                (short)1, null,  "pladema@mail.com",
                null);
    }
}