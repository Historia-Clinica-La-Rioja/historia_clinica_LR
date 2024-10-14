package ar.lamansys.sgh.clinichistory.application.anestheticreport.validations;

import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureDescriptionBo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProcedureDescriptionValidator {

    private final String ANESTHESIA = "anestesia";
    private final String SURGERY = "cirugía";

    public void assertStartEndDatesTimes(ProcedureDescriptionBo procedureDescription) {
        this.assertStartBeforeEnd(ANESTHESIA, procedureDescription.getAnesthesiaStartDate(), procedureDescription.getAnesthesiaStartTime(), procedureDescription.getAnesthesiaEndDate(), procedureDescription.getAnesthesiaEndTime());
        this.assertStartBeforeEnd(SURGERY, procedureDescription.getSurgeryStartDate(), procedureDescription.getSurgeryStartTime(), procedureDescription.getSurgeryEndDate(), procedureDescription.getSurgeryEndTime());
    }

    private void assertStartBeforeEnd(String concept, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {

        if (startDate == null || startTime == null || endDate == null || endTime == null)
            return;

        LocalDateTime start = LocalDateTime.of(startDate, startTime);
        LocalDateTime end = LocalDateTime.of(endDate, endTime);
        if (!start.isBefore(end))
            throw new ConstraintViolationException(String.format("La fecha de inicio de la %s, debe ser anterior a su fecha de finalización", concept), Collections.emptySet());
    }
}
