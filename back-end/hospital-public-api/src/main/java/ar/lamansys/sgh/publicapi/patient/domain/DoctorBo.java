package ar.lamansys.sgh.publicapi.patient.domain;

import java.util.ArrayList;

import ar.lamansys.sgh.publicapi.reports.domain.ClinicalSpecialtyBo;
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
public class DoctorBo {
	private Integer id;
	private String licenseNumber;
	private String lastName;
	private String name;
	private String identificationNumber;
	private ArrayList<ClinicalSpecialtyBo> specialtyList;

}
