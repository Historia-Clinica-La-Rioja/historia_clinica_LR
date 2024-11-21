package ar.lamansys.sgh.clinichistory.domain.isolation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class IsolationAlertAuthorBo {
	Integer id;
	String fullName;
	public String toString() {
		return getFullName();
	}
}
