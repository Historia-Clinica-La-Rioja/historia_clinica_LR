package ar.lamansys.sgh.shared.infrastructure.input.service.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class BookingInstitutionExtendedDto {

	private final Integer id;
	private final String description;
	private final String sisaCode;
	private final String dependency;
	private final String address;
	private final String city;
	private final String department;
	private final List<String> clinicalSpecialtiesNames;
	private final List<String> aliases;

}
