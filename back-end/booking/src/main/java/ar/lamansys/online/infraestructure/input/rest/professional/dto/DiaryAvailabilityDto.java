package ar.lamansys.online.infraestructure.input.rest.professional.dto;

import ar.lamansys.online.domain.professional.DiaryAvailabilityBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class DiaryAvailabilityDto {
    private final BookingDiaryDto diary;
    private final AvailabilityDto slots;

    public DiaryAvailabilityDto(DiaryAvailabilityBo diaryAvailabilityBo) {
        this.diary = new BookingDiaryDto(diaryAvailabilityBo.getDiary());
        this.slots = new AvailabilityDto(diaryAvailabilityBo.getSlots());
    }
}
