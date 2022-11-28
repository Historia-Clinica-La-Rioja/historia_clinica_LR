package net.pladema.snowstorm.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "snomed_group_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SnomedGroupType {

	public static final Short DEFAULT = (short) 1;
	public static final Short TEMPLATE = (short) 2;
	public static final Short SEARCH_GROUP = (short) 3;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Short id;

	@Column(name = "description", length = 20)
	private String description;
}
