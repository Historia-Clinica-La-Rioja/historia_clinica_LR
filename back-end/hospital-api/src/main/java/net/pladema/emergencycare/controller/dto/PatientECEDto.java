package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PatientECEDto {

	Integer id;

	Integer patientMedicalCoverageId;

	Short typeId;

	PersonECEDto person;
}
