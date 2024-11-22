package net.pladema.medicalconsultation.diary.service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.diary.domain.enums.EDiaryBookingRestrictionType;

@Getter
@Setter
@ToString
@Builder
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

    public boolean isValidatedByDays() {
        return EDiaryBookingRestrictionType.RESTRICTED_BY_DAYS_AMOUNT.equals(this.restrictionType);
    }

    public boolean isValidatedByMonth() {
        return EDiaryBookingRestrictionType.RESTRICTED_BY_CURRENT_MONTH.equals(this.restrictionType);

    }
    public boolean isUnrestricted() {
        return EDiaryBookingRestrictionType.UNRESTRICTED.equals(this.restrictionType);
    }
}
