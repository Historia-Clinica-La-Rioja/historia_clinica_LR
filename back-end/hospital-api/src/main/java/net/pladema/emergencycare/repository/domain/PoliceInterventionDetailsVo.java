package net.pladema.emergencycare.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.repository.entity.PoliceInterventionDetails;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PoliceInterventionDetailsVo implements Serializable {

	private static final long serialVersionUID = -8118445529514102823L;

	private Integer id;

	private LocalDate callDate;

	private LocalTime callTime;

	private String plateNumber;

	private String firstName;

	private String lastName;

	public PoliceInterventionDetailsVo(PoliceInterventionDetails policeInterventionDetails){
		if (policeInterventionDetails != null) {
			this.id = policeInterventionDetails.getId();
			this.callDate = policeInterventionDetails.getCallDate();
			this.callTime = policeInterventionDetails.getCallTime();
			this.plateNumber = policeInterventionDetails.getPlateNumber();
			this.firstName = policeInterventionDetails.getFirstname();
			this.lastName = policeInterventionDetails.getLastname();
		}
	}
}
