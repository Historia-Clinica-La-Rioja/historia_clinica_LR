package ar.lamansys.odontology.domain;

import ar.lamansys.sgh.shared.infrastructure.input.service.events.EventTopicDto;

public interface Publisher {

	public void run(Integer patientId, EventTopicDto eventTopicDto);
}
