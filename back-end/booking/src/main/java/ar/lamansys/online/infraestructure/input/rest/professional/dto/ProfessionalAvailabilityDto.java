package ar.lamansys.online.infraestructure.input.rest.professional.dto;

import ar.lamansys.online.domain.professional.ProfessionalAvailabilityBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class ProfessionalAvailabilityDto {
    private final List<DiaryAvailabilityDto> availability;
    private final BookingProfessionalDto professional;

    public ProfessionalAvailabilityDto(ProfessionalAvailabilityBo professionalAvailabilityBo) {
        this.availability = new ArrayList<>();
        professionalAvailabilityBo.getAvailability().forEach(availabilityBo -> availability.add(new DiaryAvailabilityDto(availabilityBo)));
        professional = new BookingProfessionalDto(professionalAvailabilityBo.getProfessional());
    }
}
