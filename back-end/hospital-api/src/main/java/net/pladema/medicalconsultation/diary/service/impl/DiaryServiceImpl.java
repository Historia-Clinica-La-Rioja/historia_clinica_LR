package net.pladema.medicalconsultation.diary.service.impl;

import net.pladema.medicalconsultation.diary.repository.DiaryRepository;
import net.pladema.medicalconsultation.diary.repository.entity.Diary;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DiaryServiceImpl implements DiaryService {

    private static final Logger LOG = LoggerFactory.getLogger(DiaryServiceImpl.class);

    private final DiaryOpeningHoursService diaryOpeningHoursService;

    private final DiaryRepository diaryRepository;

    public DiaryServiceImpl(DiaryOpeningHoursService diaryOpeningHoursService,
                            DiaryRepository diaryRepository){
        super();
        this.diaryOpeningHoursService = diaryOpeningHoursService;
        this.diaryRepository = diaryRepository;
    }


    @Override
    public DiaryBo addDiary(DiaryBo diaryToSave) {
        LOG.debug("Input parameters -> diaryToSave {}", diaryToSave);

        Diary diary = createDiaryInstance(diaryToSave);
        diary = diaryRepository.save(diary);

        Integer diaryId = diary.getId();

        diaryOpeningHoursService.load(diaryId, diaryToSave.getDiaryOpeningHours());

        diaryToSave.setId(diaryId);
        LOG.debug("Diary saved -> {}", diaryToSave);
        return diaryToSave;
    }

    private Diary createDiaryInstance(DiaryBo diaryBo){
        Diary diary = new Diary();
        diary.setHealthcareProfessionalId(diaryBo.getHealthcareProfessionalId());
        diary.setDoctorsOfficeId(diaryBo.getDoctorsOfficeId());
        diary.setStartDate(diaryBo.getStartDate());
        diary.setEndDate(diaryBo.getEndDate());
        diary.setAppointmentDuration(diaryBo.getAppointmentDuration());
        diary.setAutomaticRenewal(diaryBo.getAutomaticRenewal());
        diary.setProfessionalAsignShift(diaryBo.getProfessionalAsignShift());
        diary.setIncludeHoliday(diaryBo.getIncludeHoliday());
        diary.setActive(true);
        return diary;
    }

    /**
     *
     * @param healthcareProfessionalId ID profesional de salud
     * @param doctorsOfficeId ID consultorio
     * @param newDiaryStart nueva fecha de comienzo para agenda
     * @param newDiaryEnd nueva fecha de fin para agenda
     * @return lista con todos los ID de agendas definidas en rangos de fecha superpuestas para un
     * mismo profesional de salud y consultorio.
     */
    @Override
    public List<Integer> getAllOverlappingDiary(Integer healthcareProfessionalId,
                                                Integer doctorsOfficeId,
                                                LocalDate newDiaryStart, LocalDate newDiaryEnd) {
        LOG.debug("Input parameters -> healthcareProfessionalId {}, doctorsOfficeId {}, newDiaryStart {}, newDiaryEnd {}",
                healthcareProfessionalId, doctorsOfficeId, newDiaryStart, newDiaryEnd);
        List<Integer> diaryIds = diaryRepository
                .findAllOverlappingDiary(healthcareProfessionalId, doctorsOfficeId, newDiaryStart, newDiaryEnd);
        LOG.debug("Diary saved -> {}", diaryIds);
        return diaryIds;

    }
}
