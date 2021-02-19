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

@Entity
@Table(name = "emergency_care_state")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmergencyCareState implements Serializable {
		/**
		 *
		 */
		private static final long serialVersionUID = 288115248758489524L;

		public static final short EN_ATENCION = 1;
		public static final short EN_ESPERA = 2;
		public static final short CON_ALTA_ADMINISTRATIVA = 3;
		public static final short CON_ALTA_MEDICA = 4;

		@Id
		@Column(name = "id")
		private Short id;

		@Column(name = "description", length = 25, nullable = false)
		private String description;
}
