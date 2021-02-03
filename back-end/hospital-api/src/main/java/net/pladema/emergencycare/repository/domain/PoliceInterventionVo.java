package net.pladema.emergencycare.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.repository.entity.PoliceIntervention;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PoliceInterventionVo implements Serializable {

	private static final long serialVersionUID = -8118445529514102823L;

	private Integer id;

	private LocalDate callDate;

	private LocalTime callTime;

	private String plateNumber;

	private String firstName;

	private String lastName;

	public PoliceInterventionVo(PoliceIntervention policeIntervention){
		if (policeIntervention!= null) {
			this.id = policeIntervention.getId();
			this.callDate = policeIntervention.getCallDate();
			this.callTime = policeIntervention.getCallTime();
			this.plateNumber = policeIntervention.getPlateNumber();
			this.firstName = policeIntervention.getFirstname();
			this.lastName = policeIntervention.getLastname();
		}
	}
}
