package ar.lamansys.sgh.shared.infrastructure.input.service.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class BookingInstitutionExtendedDto {

	private final Integer id;
	private final String description;
	private final String sisaCode;
	private final String dependency;
	private final String address;
	private final List<String> clinicalSpecialtyName;

}
