package net.pladema.infrastructure.output.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ServiceRequestTemplatePK implements Serializable {

	private static final long serialVersionUID = -5668370767449221182L;
	
	@Column(name = "service_request_id", nullable = false)
	private Integer serviceRequestId;

	@Column(name = "group_id", nullable = false)
	private Integer groupId;
}
