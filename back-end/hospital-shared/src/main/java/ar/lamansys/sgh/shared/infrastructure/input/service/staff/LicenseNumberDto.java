package ar.lamansys.sgh.shared.infrastructure.input.service.staff;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LicenseNumberDto {

	private Integer id;
	private String number;
	private String type;

	public String getInfo() {
		return (type) + "-" + number;
	}
}
