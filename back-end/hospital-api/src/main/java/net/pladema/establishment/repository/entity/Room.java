package net.pladema.establishment.repository.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "room")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Room {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@Column(name = "room_number", nullable = false, unique = true)
	private String roomNumber;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "type", nullable = false)
	private String type;

	@Column(name = "discharge_date")
	private LocalDate dischargeDate;

	@Column(name = "sector_id", nullable = false)
	private Integer sectorId;
	
}
