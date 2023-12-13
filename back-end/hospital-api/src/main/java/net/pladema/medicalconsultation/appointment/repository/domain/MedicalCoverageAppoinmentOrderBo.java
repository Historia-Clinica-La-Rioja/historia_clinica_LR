package net.pladema.medicalconsultation.appointment.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalCoverageAppoinmentOrderBo{

	private String medicalCoverage;

	private String medicalCoverageAcronym;

}
