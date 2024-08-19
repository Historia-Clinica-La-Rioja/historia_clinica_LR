package ar.lamansys.sgh.clinichistory.domain.ips;

import java.util.Optional;

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
public class FhirObservationBo {

	private Integer id;
	private Integer observationGroupId;
	private String loincCode;
	private String value;
	private FhirQuantityBo quantity;

	public Optional<String> getUnitDescription() {
		var qty = this.getQuantity();
		if (qty == null) return Optional.empty();
		return Optional.ofNullable(qty.getUnit());
	}

	/**
	 * If the observation has a quantity attached
	 * the observation's value must be that quantity.
	 * Otherwise, return the value field or null if there's no value.
	 * @return
	 */
	public String getValue(){
		if (this.quantity != null && this.quantity.getValue() != null) {
			return this.quantity.getValue().toString();
		}
		return this.value;
	}

}
