package ar.lamansys.sgh.shared.infrastructure.input.service.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class DiaryAvailabilityDto {
    private final BookingDiaryDto diary;
    private final AvailabilityDto slots;

}
