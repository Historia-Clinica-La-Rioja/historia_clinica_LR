package ar.lamansys.online.infraestructure.input.rest.professional.dto;

import ar.lamansys.online.domain.professional.AvailabilityBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class AvailabilityDto {
    private final LocalDate date;
    private final List<String> slots;

    public AvailabilityDto(AvailabilityBo availabilityBo) {
        this.date = availabilityBo.getDate();
        this.slots = availabilityBo.getSlots();
    }
}
