package net.pladema.medicalconsultation.diary.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentSearchBo;
import net.pladema.medicalconsultation.appointment.service.domain.EmptyAppointmentBo;
import net.pladema.medicalconsultation.diary.service.domain.ActiveDiaryClinicalSpecialtyBo;
import net.pladema.medicalconsultation.diary.service.domain.CompleteDiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;

public interface DiaryService {

    Integer addDiary(DiaryBo diaryToSave);

    Integer updateDiary(DiaryBo diaryToSave);

    Boolean deleteDiary(Integer diaryId);

    List<DiaryBo> getAllOverlappingDiary(Integer healthcareProfessionalId, Integer doctorsOfficeId, Integer institutionId,
                                         LocalDate newDiaryStart, LocalDate newDiaryEnd, Optional<Integer> excludeDiaryId);

    Collection<DiaryBo> getActiveDiariesBy(Integer associatedHealthcareProfessionalId, Integer healthcareProfessionalId, Integer specialtyId, Integer institutionId);

    Optional<CompleteDiaryBo> getDiary(Integer diaryId);

    Optional<DiaryBo> getDiaryByAppointment(Integer appointmentId);

    DiaryBo getDiaryById(Integer diaryId);

    Integer getDiaryIdByAppointment(Integer appointmentId);

    Boolean hasActiveDiariesInInstitution(Integer healthcareProfessionalId, Integer institutionId);

    Integer getInstitution(Integer diaryId);

    List<String> getActiveDiariesAliases(Integer institutionId);

	List<ActiveDiaryClinicalSpecialtyBo> getActiveDiariesClinicalSpecialties(Integer institutionId);

	Set<String> getActiveDiariesAliasesByClinicalSpecialty(Integer institutionId, Integer clinicalSpecialtyId);

	List<EmptyAppointmentBo> getEmptyAppointmentsBySearchCriteria(Integer institutionId, AppointmentSearchBo searchCriteria, Boolean mustFilterByModality);

    Optional<CompleteDiaryBo> getCompleteDiaryByAppointment(Integer appointmentId);

    Boolean hasPractices(Integer diaryId);
}
