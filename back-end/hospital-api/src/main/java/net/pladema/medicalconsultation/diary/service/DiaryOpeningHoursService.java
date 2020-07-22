package net.pladema.medicalconsultation.diary.service;

import net.pladema.medicalconsultation.diary.service.domain.OccupationBo;

import java.time.LocalDate;
import java.util.List;

public interface DiaryOpeningHoursService {

    List<OccupationBo> findAllWeeklyDoctorsOfficeOccupation(Integer doctorOfficeId, LocalDate startDate, LocalDate endDate);
}
