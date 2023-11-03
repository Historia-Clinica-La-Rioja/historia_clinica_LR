package ar.lamansys.sgh.publicapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AppointmentMedicalCoverageBo {
	private String name;
	private String cuit;
	private String plan;
}
