package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyCarePatientDto implements Serializable {

	Integer id;

	Integer patientMedicalCoverageId;

	String patientDescription;

	Short typeId;

	EmergencyCarePersonDto person;
}
