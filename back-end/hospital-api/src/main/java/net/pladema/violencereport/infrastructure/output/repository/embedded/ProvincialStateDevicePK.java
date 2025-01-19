package net.pladema.violencereport.infrastructure.output.repository.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProvincialStateDevicePK implements Serializable {

	private static final long serialVersionUID = -3270925889436386667L;

	@Column(name = "report_id", nullable = false)
	private Integer reportId;

	@Column(name = "provincial_state_device_id", nullable = false)
	private Short provincialStateDeviceId;
}
