package net.pladema.medicalconsultation.diary.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.diary.domain.enums.EDiaryBookingRestrictionType;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class DiaryBookingRestrictionBo {

    private EDiaryBookingRestrictionType restrictionType;
    private Integer days;

    public static DiaryBookingRestrictionBo unrestricted() {
        return new DiaryBookingRestrictionBo(
                EDiaryBookingRestrictionType.UNRESTRICTED,
                null
        );
    }
}
