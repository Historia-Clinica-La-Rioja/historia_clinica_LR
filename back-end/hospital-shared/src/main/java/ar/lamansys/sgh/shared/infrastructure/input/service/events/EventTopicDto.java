package ar.lamansys.sgh.shared.infrastructure.input.service.events;

import lombok.Getter;

@Getter
public enum EventTopicDto {

	CONSULTA_ODONTOLOGICA_CREADA(3,"odontologia/nuevaConsulta");

	private Integer id;
	private String description;

	EventTopicDto(Integer id, String description) {
		this.id = id;
		this.description = description;
	}


}
