package ar.lamansys.sgh.shared.infrastructure.input.service.booking;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class BookingProfessionalDto {
    private Integer id;
    private String name;
    private Boolean coverage;

	public BookingProfessionalDto(Integer id, String name, Boolean coverage) {
		this.id = id;
		this.name = name;
		this.coverage = coverage;
	}
}
