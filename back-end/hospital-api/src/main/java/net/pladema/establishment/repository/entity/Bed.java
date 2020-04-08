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
@Table(name = "bed")
@Getter
@Setter

public class Bed {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "room_id", nullable = false)
	private String sector_id;
	
	@Column(name = "category", nullable = true)
	private String category;
	
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
		Bed other = (Bed) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Bed [id=" + id + ", sector_id=" + sector_id + ", category=" + category + "]";
	}
	
}
