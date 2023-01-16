package net.pladema.patient.controller.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.person.controller.dto.BasicPersonalDataDto;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class PatientToMergeDto {

	private List<Integer> oldPatientsIds;

	private Integer activePatientId;

	private BasicPersonalDataDto activePerson;
}
