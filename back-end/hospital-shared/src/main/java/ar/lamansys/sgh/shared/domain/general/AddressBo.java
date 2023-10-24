package ar.lamansys.sgh.shared.domain.general;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddressBo {

	private String street;

	private String number;

	private String floor;

	private String apartment;

	private String postCode;

	private String cityName;

	private String stateName;
}