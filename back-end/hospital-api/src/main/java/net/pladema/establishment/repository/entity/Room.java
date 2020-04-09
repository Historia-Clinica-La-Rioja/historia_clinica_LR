package net.pladema.establishment.repository.entity;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "room")
@Getter
@Setter
@ToString

public class Room {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Short id;
	
	@Column(name = "description", nullable = false, length = 255)
	private String description;
	
	@Column(name = "type", nullable = false, length = 255)
	private String type;
	
	@Column(name = "sector_id", nullable = false)
	private Short sectorId;
	
	@Column(name = "discharge_date", nullable = true)
	private LocalDate dischargeDate;
	
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
		Room other = (Room) obj;
		return Objects.equals(id, other.id);
	}
	
}
