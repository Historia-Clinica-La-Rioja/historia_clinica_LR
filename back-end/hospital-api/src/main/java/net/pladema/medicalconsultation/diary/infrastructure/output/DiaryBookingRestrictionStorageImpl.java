package net.pladema.medicalconsultation.diary.infrastructure.output;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.diary.application.port.output.DiaryBookingRestrictionStorage;
import net.pladema.medicalconsultation.diary.domain.enums.EDiaryBookingRestrictionType;
import net.pladema.medicalconsultation.diary.repository.DiaryBookingRestrictionRepository;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryBookingRestriction;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBookingRestrictionBo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiaryBookingRestrictionStorageImpl implements DiaryBookingRestrictionStorage {

    private final DiaryBookingRestrictionRepository repository;

    @Override
    public void save(Integer diaryId, DiaryBookingRestrictionBo restrictionBo) {
        log.debug("Input parameters -> diaryId {}, restrictionBo {} ", diaryId, restrictionBo);
        repository.save(mapToEntity(diaryId, restrictionBo));
    }

    @Override
    public Optional<DiaryBookingRestrictionBo> getByDiaryId(Integer diaryId) {
        log.debug("Input parameters -> diaryId {}", diaryId);
        var result = repository.findById(diaryId).map(this::mapToBo);
        log.debug("Output -> {}", result);
        return result;
    }

    private DiaryBookingRestrictionBo mapToBo(DiaryBookingRestriction entity) {
        return DiaryBookingRestrictionBo.builder()
                .restrictionType(EDiaryBookingRestrictionType.map(entity.getRestrictionType()))
                .days(entity.getDays())
                .build();
    }

    private DiaryBookingRestriction mapToEntity(Integer diaryId, DiaryBookingRestrictionBo restrictionBo) {
        return DiaryBookingRestriction.builder()
                .diaryId(diaryId)
                .restrictionType(restrictionBo.getRestrictionType().getId())
                .days(restrictionBo.getDays())
                .build();
    }


}
