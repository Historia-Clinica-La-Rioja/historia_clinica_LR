package ar.lamansys.online.domain.professional;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ProfessionalAvailabilityBo {
    private final List<DiaryAvailabilityBo> availability;
    private final BookingProfessionalBo professional;
}
