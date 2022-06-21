package ar.lamansys.online.domain.professional;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class AppointmentDiaryBo {
    private final Integer id;
    private final Integer patientId;
    private final Integer diaryId;
    private final LocalDate date;
    private final LocalTime hour;
    private final Short appointmentStateId;
    private final boolean overturn;
    private final Integer patientMedicalCoverageId;
    private final String phoneNumber;
    private final Short medicalAttentionTypeId;
}
