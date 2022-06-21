package ar.lamansys.online.infraestructure.input.rest.professional.dto;

import ar.lamansys.online.domain.professional.BookingProfessionalBo;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BookingProfessionalDto {
    private final Integer id;
    private final String name;
    private final Boolean coverage;

    public BookingProfessionalDto(BookingProfessionalBo bookingProfessionalBo) {
        this.id = bookingProfessionalBo.getId();
        this.name = bookingProfessionalBo.getName();
        this.coverage = bookingProfessionalBo.getCoverage();
    }

}
