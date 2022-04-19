package net.pladema.medicalconsultation.diary.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.functional.Overlapping;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DiaryOpeningHoursDto implements Overlapping<DiaryOpeningHoursDto> {

    @NotNull
    @Valid
    private OpeningHoursDto openingHours;

    @NotNull
    private Short medicalAttentionTypeId;

    @Nullable
    private Short overturnCount = 0;

    private Boolean externalAppointmentsAllowed;

    @Override
    public boolean overlap(DiaryOpeningHoursDto other) {
        return !(this.medicalAttentionTypeId.equals(other.getMedicalAttentionTypeId())) &&
                openingHours.overlap(other.getOpeningHours());

    }
}
