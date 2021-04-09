package net.pladema.emergencycare.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.service.domain.PoliceInterventionDetailsBo;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "police_intervention_details")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PoliceInterventionDetails implements Serializable {
		/**
		 *
		 */
		private static final long serialVersionUID = -8562878895269889422L;

		@Id
		@Column(name = "emergency_care_episode_id")
		private Integer id;

		@Column(name = "call_date")
		private LocalDate callDate;

		@Column(name = "call_time")
		private LocalTime callTime;

		@Column(name = "plate_number", length = 15)
		private String plateNumber;

		@Column(name = "firstname", length = 20)
		private String firstname;

		@Column(name = "lastname", length = 20)
		private String lastname;

		public PoliceInterventionDetails(PoliceInterventionDetailsBo policeInterventionDetailsBo){
			this.id = policeInterventionDetailsBo.getId();
			this.callDate = policeInterventionDetailsBo.getCallDate();
			this.callTime = policeInterventionDetailsBo.getCallTime();
			this.plateNumber = policeInterventionDetailsBo.getPlateNumber();
			this.firstname = policeInterventionDetailsBo.getFirstName();
			this.lastname = policeInterventionDetailsBo.getLastName();
		}
}
