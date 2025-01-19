package net.pladema.medicalconsultation.equipmentdiary.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryService;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryBo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateEquipmentDiaryAndAppointments {

    private final HandleEquipmentDiaryOutOfBoundsAppointments handleEquipmentDiaryOutOfBoundsAppointments;
    private final EquipmentDiaryService equipmentDiaryService;

    @Transactional
    public Integer run(EquipmentDiaryBo equipmentDiaryBo) {
        log.debug("Input parameters -> equipmentDiaryBo {}", equipmentDiaryBo);

        handleEquipmentDiaryOutOfBoundsAppointments.run(equipmentDiaryBo);
        Integer result = equipmentDiaryService.updateDiary(equipmentDiaryBo);

        log.debug("Output -> result {}", result);
        return result;
    }
}
