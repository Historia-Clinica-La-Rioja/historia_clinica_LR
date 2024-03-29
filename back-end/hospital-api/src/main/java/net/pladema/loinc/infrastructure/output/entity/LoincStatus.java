package net.pladema.loinc.infrastructure.output.entity;

import lombok.AllArgsConstructor;
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
@Table(name = "loinc_status")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoincStatus implements Serializable {

	@Id
	@Column(name = "id")
	private Short id;

	@Column(name = "description", nullable = false)
	private String description;

}

