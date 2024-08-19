package net.pladema.medicalconsultation.equipmentdiary.service.domain;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.diary.domain.IDiaryBo;
import net.pladema.medicalconsultation.diary.domain.IDiaryOpeningHoursBo;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EquipmentDiaryBo implements IDiaryBo {

    protected Integer id;

    protected Integer equipmentId;

    protected LocalDate startDate;

    protected LocalDate endDate;

    protected Short appointmentDuration;

    protected boolean automaticRenewal = false;

    protected boolean includeHoliday = false;

    protected boolean active = true;

    protected List<EquipmentDiaryOpeningHoursBo> diaryOpeningHours;
    
    protected boolean deleted = false;

    public List<IDiaryOpeningHoursBo> getIDiaryOpeningHours() {
        return Collections.unmodifiableList(diaryOpeningHours);
    }

}
