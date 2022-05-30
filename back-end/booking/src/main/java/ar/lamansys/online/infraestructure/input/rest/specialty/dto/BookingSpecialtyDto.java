package ar.lamansys.online.infraestructure.input.rest.specialty.dto;

import ar.lamansys.online.domain.specialty.BookingSpecialtyBo;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookingSpecialtyDto {

    private Integer id;
    private String description;

    public BookingSpecialtyDto(BookingSpecialtyBo practice) {
        this.setId(practice.getId());
        this.setDescription(practice.getDescription());
    }
}
