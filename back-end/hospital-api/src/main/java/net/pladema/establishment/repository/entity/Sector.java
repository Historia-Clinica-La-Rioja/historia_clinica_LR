package net.pladema.establishment.repository.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sector")
@Getter
@Setter

public class Sector {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "institution_id", nullable = false)
	private int institution_id;
	
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sector other = (Sector) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Sector [id=" + id + ", description=" + description + ", institution_id=" + institution_id + "]";
	}
}
