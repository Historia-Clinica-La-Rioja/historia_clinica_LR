package net.pladema.medicalconsultation.diary.domain;

import java.time.LocalDate;
import java.util.List;

public interface IDiaryBo {

    LocalDate getStartDate();

    LocalDate getEndDate();

    List<IDiaryOpeningHoursBo> getIDiaryOpeningHours();

    Integer getId();
}
