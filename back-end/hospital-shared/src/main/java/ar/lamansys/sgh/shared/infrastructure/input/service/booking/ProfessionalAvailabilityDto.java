package ar.lamansys.sgh.shared.infrastructure.input.service.booking;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class ProfessionalAvailabilityDto {
    private final List<DiaryAvailabilityDto> availability;
    private final BookingProfessionalDto professional;


}
