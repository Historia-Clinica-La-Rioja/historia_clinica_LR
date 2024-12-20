package net.pladema.medicalconsultation.diary.application;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.diary.application.port.output.DiaryBookingRestrictionStorage;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBookingRestrictionBo;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class PersistBookingRestriction {

    private final FeatureFlagsService featureFlagsService;
    private final DiaryBookingRestrictionStorage storage;

    public void run(Integer diaryId, DiaryBookingRestrictionBo bookingRestriction) {
        log.debug("Input parameters -> diaryId {}, bookingRestriction {} ", diaryId,bookingRestriction);
        if (!featureFlagsService.isOn(AppFeature.HABILITAR_RESTRICCION_CANTIDAD_DIAS_ASIG_TURNOS)){
            return;
        }
        var toSave = Objects.isNull(bookingRestriction) ? DiaryBookingRestrictionBo.unrestricted() : bookingRestriction;
        storage.save(diaryId,toSave);
    }
}
