package net.pladema.reports.infrastructure.input;

import ar.lamansys.sgh.shared.infrastructure.input.service.staff.LicenseNumberDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AnnexIIProfessionalDto {

	private String completeProfessionalName;

	private List<LicenseNumberDto> licenses;

	public String getLicensesString() {
		if (licenses == null || licenses.isEmpty())
			return "Sin información";
		return licenses.stream().map(license -> (license.getType().equals("NATIONAL") ? "MN" : "MP") + " N° " + license.getNumber()).collect(Collectors.joining(" | "));
	}

}
