package net.pladema.establishment.repository.entity;

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
@Table(name = "bed")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Bed {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@Column(name = "bed_number", nullable = false, unique = true)
	private String bedNumber;
	
	@Column(name = "room_id", nullable = false)
	private Integer roomId;
	
	@Column(name = "bed_category_id", nullable = false)
	private Short bedCategoryId;
	
	@Column(name = "enabled", nullable = false)
	private Boolean enabled;
	
	@Column(name = "available", nullable = false)
	private Boolean available;
	
	@Column(name = "free", nullable = false)
	private Boolean free;
	
}
