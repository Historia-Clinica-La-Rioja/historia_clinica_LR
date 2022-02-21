package ar.lamansys.sgh.shared.infrastructure.input.service.events;

import lombok.Getter;

@Getter
public enum EventTopicDto {

	ALTA_MEDICA_GUARDIA(2,"guardia/altaMedica"),
	CONSULTA_ODONTOLOGICA_CREADA(3,"odontologia/nuevaConsulta");

	private Integer id;
	private String description;

	EventTopicDto(Integer id, String description) {
		this.id = id;
		this.description = description;
	}


}
