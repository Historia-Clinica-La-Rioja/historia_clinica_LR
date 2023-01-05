package net.pladema.establishment.repository.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "modality")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Modality {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "acronym", nullable = false, length = 15)
	private String acronym;

	@Column(name = "description", nullable = false, length = 50)
	private String description;
}
