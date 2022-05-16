package ar.lamansys.sgh.shared.infrastructure.input.service.booking;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookingSpecialtyDto {
	private Integer id;
    private String description;

	public BookingSpecialtyDto(Integer id, String description) {
		this.id = id;
		this.description = description;
	}

}
