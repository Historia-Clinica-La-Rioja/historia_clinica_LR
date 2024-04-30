package net.pladema.medicalconsultation.equipmentdiary.service.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.diary.domain.IDiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.domain.IOpeningHoursBo;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"diaryId", "overturnCount"})
@ToString
@NoArgsConstructor
public class EquipmentDiaryOpeningHoursBo implements IDiaryOpeningHoursBo {

    private Integer diaryId;

    private EquipmentOpeningHoursBo openingHours;

    private Short medicalAttentionTypeId;

    private Short overturnCount = 0;

    private Boolean externalAppointmentsAllowed;

    @Override
    public IOpeningHoursBo getIOpeningHours() {
        return openingHours;
    }
}
