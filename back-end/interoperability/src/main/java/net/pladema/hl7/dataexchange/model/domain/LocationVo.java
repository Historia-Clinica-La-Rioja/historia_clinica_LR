package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.hl7.dataexchange.model.adaptor.FhirAddress;

@Getter
@Setter
@NoArgsConstructor
public class LocationVo {

	public LocationVo(String sisaCode, String name, String phoneNumber, Integer addressId) {
		setId(sisaCode);
		setName(name);
		setAddressId(addressId);
	}

	private String id;

	private String name;

	private Integer addressId;
	private FhirAddress fullAddress;

	public boolean hasAddress() {
		return fullAddress != null;
	}
}
