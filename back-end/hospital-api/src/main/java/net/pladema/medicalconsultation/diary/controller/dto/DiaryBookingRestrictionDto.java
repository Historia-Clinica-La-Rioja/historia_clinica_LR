package net.pladema.medicalconsultation.diary.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.medicalconsultation.diary.domain.enums.EDiaryBookingRestrictionType;

@Getter
@Setter
@NoArgsConstructor
public class DiaryBookingRestrictionDto {

    private EDiaryBookingRestrictionType restrictionType;
    private Integer days;

}
