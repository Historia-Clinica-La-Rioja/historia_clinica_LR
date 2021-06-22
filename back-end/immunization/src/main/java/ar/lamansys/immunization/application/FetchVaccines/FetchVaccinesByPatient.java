package ar.lamansys.immunization.application.FetchVaccines;

import ar.lamansys.immunization.application.FetchVaccines.exceptions.FetchVaccinesByPatientException;
import ar.lamansys.immunization.application.FetchVaccines.exceptions.FetchVaccinesByPatientExceptionEnum;
import ar.lamansys.immunization.domain.patient.PatientInfoPort;
import ar.lamansys.immunization.domain.vaccine.VaccineBo;
import ar.lamansys.immunization.domain.vaccine.VaccineByFilterPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class FetchVaccinesByPatient {

    private final Logger logger;

    private final VaccineByFilterPort vaccineByFilterPort;

    private final PatientInfoPort patientInfoPort;

    public FetchVaccinesByPatient(VaccineByFilterPort vaccineByFilterPort, PatientInfoPort patientInfoPort) {
        this.vaccineByFilterPort = vaccineByFilterPort;
        this.patientInfoPort = patientInfoPort;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }


    public List<VaccineBo> run(Integer patientId, LocalDate applicationDate) {
        logger.debug("FetchVaccines to patient {}", patientId);
        assertInfo(patientId, applicationDate);

        var result = patientInfoPort.getPatientInfo(patientId)
                .map(p -> calculateLiveDays(p.getBirthday(), applicationDate))
                .map(vaccineByFilterPort::run)
                .orElseThrow(() -> new FetchVaccinesByPatientException(FetchVaccinesByPatientExceptionEnum.INVALID_PATIENT, "El paciente no existe"));
        logger.debug("Vaccines results ids -> {}", result.stream().map(VaccineBo::getId));
        logger.trace("Vaccines results -> {}", result);
        return result;
    }

    private Integer calculateLiveDays(LocalDate birthday, LocalDate applicationDate) {
        return Math.toIntExact(Duration.between(birthday.atStartOfDay(), applicationDate.atStartOfDay()).toDays());
    }

    private void assertInfo(Integer patientId, LocalDate applicationDate) {

        Objects.requireNonNull(patientId, () -> {
            throw new FetchVaccinesByPatientException(FetchVaccinesByPatientExceptionEnum.NULL_PATIENT_ID, "El identificador del paciente es obligatorio");
        });


        Objects.requireNonNull(applicationDate, () -> {
            throw new FetchVaccinesByPatientException(FetchVaccinesByPatientExceptionEnum.NULL_APPLICATION_DATE, "La fecha de aplicación es obligatoria");
        });
    }
}
