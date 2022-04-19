package ar.lamansys.online.infraestructure.input.rest.integration.dto;

import ar.lamansys.online.domain.integration.BookingInstitutionBo;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookingInstitutionDto {

    private Integer id;
    private String description;

    public BookingInstitutionDto(BookingInstitutionBo institution) {
        this.setId(institution.getId());
        this.setDescription(institution.getDescription());
    }

}
