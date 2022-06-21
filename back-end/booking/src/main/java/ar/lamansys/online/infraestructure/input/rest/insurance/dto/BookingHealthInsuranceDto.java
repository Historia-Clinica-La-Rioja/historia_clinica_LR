package ar.lamansys.online.infraestructure.input.rest.insurance.dto;

import ar.lamansys.online.domain.insurance.BookingHealthInsuranceBo;
import lombok.Getter;

@Getter
public class BookingHealthInsuranceDto {
    private final Integer id;
    private final String description;

    public BookingHealthInsuranceDto(BookingHealthInsuranceBo bookingHealthInsuranceBo){
        this.id = bookingHealthInsuranceBo.getId();
        this.description = bookingHealthInsuranceBo.getName();
    }
}
