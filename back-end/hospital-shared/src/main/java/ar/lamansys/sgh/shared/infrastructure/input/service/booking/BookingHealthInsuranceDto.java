package ar.lamansys.sgh.shared.infrastructure.input.service.booking;

import lombok.Getter;

@Getter
public class BookingHealthInsuranceDto {
    private final Integer id;
    private final String description;

	public BookingHealthInsuranceDto(Integer id, String description) {
		this.id = id;
		this.description = description;
	}
}
