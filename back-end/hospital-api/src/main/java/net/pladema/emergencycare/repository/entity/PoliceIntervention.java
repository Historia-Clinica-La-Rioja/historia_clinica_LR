package net.pladema.emergencycare.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.service.domain.PoliceInterventionBo;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "police_intervention")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PoliceIntervention implements Serializable {
		/**
		 *
		 */
		private static final long serialVersionUID = -8562878895269889422L;

		@Id
		@Column(name = "id")
		@GeneratedValue(strategy = GenerationType.IDENTITY)
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

		public PoliceIntervention(PoliceInterventionBo policeInterventionBo){
			this.callDate = policeInterventionBo.getDateCall();
			this.callTime = policeInterventionBo.getTimeCall();
			this.plateNumber = policeInterventionBo.getPlateNumber();
			this.firstname = policeInterventionBo.getFirstName();
			this.lastname = policeInterventionBo.getLastName();
		}
}
