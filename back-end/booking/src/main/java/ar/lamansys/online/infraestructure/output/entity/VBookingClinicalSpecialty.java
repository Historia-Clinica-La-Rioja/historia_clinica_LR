package ar.lamansys.online.infraestructure.output.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Entity
@Table(name = "v_booking_clinical_specialty")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VBookingClinicalSpecialty {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String description;

	@Column(name = "clinical_specialty_type_id")
	private Short type;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof VBookingClinicalSpecialty)) return false;
		VBookingClinicalSpecialty that = (VBookingClinicalSpecialty) o;
		return getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
