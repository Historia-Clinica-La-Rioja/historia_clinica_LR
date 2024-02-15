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

	public AddressBo(String street, String number, String floor, String apartment, String cityName, String stateName) {
		this.street = street;
		this.number = number;
		this.floor = floor;
		this.apartment = apartment;
		this.cityName = cityName;
		this.stateName = stateName;
	}

	/*Se usa en PDF de receta comun*/
	public String getCompleteAddress() {
		String resultThatCanHoldNulls = street + " " + number + " " + floor + " " + apartment + " " + cityName + " " + stateName;
		String resultWithoutNulls = resultThatCanHoldNulls.replace("null", "").trim();
		if (resultWithoutNulls.isEmpty())
			return null;
		return resultWithoutNulls;
	}

}
