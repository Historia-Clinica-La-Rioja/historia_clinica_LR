package ar.lamansys.immunization.application.FetchVaccines;

import ar.lamansys.immunization.application.FetchVaccines.exceptions.FetchVaccinesByPatientException;
import ar.lamansys.immunization.domain.patient.PatientInfoBO;
import ar.lamansys.immunization.domain.patient.PatientInfoPort;
import ar.lamansys.immunization.domain.vaccine.VaccineBo;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.VaccineByFilterMockPortImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchVaccinesByPatientTest {

    private FetchVaccinesByPatient fetchVaccinesByPatient;

    @Mock
    private PatientInfoPort patientInfoPort;

    @BeforeEach
    public void setUp() {
        fetchVaccinesByPatient = new FetchVaccinesByPatient(new VaccineByFilterMockPortImpl(), patientInfoPort);
    }

    @Test
    void patientNeedVaccines() {
        when(patientInfoPort.getPatientInfo(any()))
                .thenReturn(Optional.of(new PatientInfoBO(1, LocalDate.of(1991,5,4))));
        List<VaccineBo> result = fetchVaccinesByPatient.run(1, LocalDate.of(2021,6,25));


        Assertions.assertEquals(result.size(), 3);
    }

    @Test
    void patientDontNeedVaccines() {
        when(patientInfoPort.getPatientInfo(any()))
                .thenReturn(Optional.of(new PatientInfoBO(1, LocalDate.of(2021,6,4))));
        List<VaccineBo> result = fetchVaccinesByPatient.run(1, LocalDate.of(2021,6,25));


        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void invalidInputData() {
        Exception exception = Assertions.assertThrows(FetchVaccinesByPatientException.class, () ->
                fetchVaccinesByPatient.run(null, null)
        );
        assertEquals(exception.getMessage(),"El identificador del paciente es obligatorio");

        exception = Assertions.assertThrows(FetchVaccinesByPatientException.class, () ->
                fetchVaccinesByPatient.run(4, null)
        );
        assertEquals(exception.getMessage(),"La fecha de aplicación es obligatoria");


        when(patientInfoPort.getPatientInfo(any()))
                .thenReturn(Optional.empty());


        exception = Assertions.assertThrows(FetchVaccinesByPatientException.class, () ->
                fetchVaccinesByPatient.run(4, LocalDate.of(2020,6,6))
        );
        assertEquals(exception.getMessage(),"El paciente no existe");
    }
}