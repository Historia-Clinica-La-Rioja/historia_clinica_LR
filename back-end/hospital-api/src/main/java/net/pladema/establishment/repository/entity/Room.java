package net.pladema.establishment.repository.entity;

import java.time.LocalDateTime;
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
@Table(name = "room")
@Getter
@Setter

public class Room {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "type", nullable = false)
	private String type;
	
	@Column(name = "sector_id", nullable = false)
	private Integer sector_id;
	
	@Column(name = "discharge_date", nullable = true)
	private LocalDateTime discharge_date;
	
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

	@Override
	public String toString() {
		return "Room [id=" + id + ", description=" + description + ", type=" + type + ", sector_id=" + sector_id
				+ ", discharge_date=" + discharge_date + "]";
	}
	
	
}
