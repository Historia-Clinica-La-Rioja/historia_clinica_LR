package ar.lamansys.sgh.shared.domain.general;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PhoneBo {

	private String phonePrefix;

	private String phoneNumber;

	public String getCompletePhone() {
		if (phonePrefix != null && phoneNumber != null)
			return phonePrefix + " " + phoneNumber;
		if (phonePrefix == null && phoneNumber != null)
			return phoneNumber;
		return null;
	}

}
