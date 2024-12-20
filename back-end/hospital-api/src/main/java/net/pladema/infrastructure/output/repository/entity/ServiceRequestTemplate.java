package net.pladema.infrastructure.output.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "service_request_template")
@Entity
public class ServiceRequestTemplate implements Serializable {

	private static final long serialVersionUID = -3445176229909020200L;

	@EmbeddedId
	private ServiceRequestTemplatePK pk;

	public ServiceRequestTemplate(Integer serviceRequestId, Integer templateId) {
		this.pk = new ServiceRequestTemplatePK(serviceRequestId, templateId);
	}
}
