package ar.lamansys.sgh.shared.infrastructure.input.service.booking;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BookingProfessionalDto {
    private final Integer id;
    private final String name;
    private final Boolean coverage;

	public BookingProfessionalDto(Integer id, String name, Boolean coverage) {
		this.id = id;
		this.name = name;
		this.coverage = coverage;
	}
}
