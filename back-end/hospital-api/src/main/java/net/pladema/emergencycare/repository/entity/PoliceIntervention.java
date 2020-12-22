package net.pladema.emergencycare.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
}
