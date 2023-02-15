package ar.lamansys.sgh.shared.domain.medicalcoverage;

import ar.lamansys.sgh.shared.infrastructure.input.service.EMedicalCoverageTypeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SharedPatientMedicalCoverageBo {

	private SharedMedicalCoverageBo medicalCoverage;

	private String affiliateNumber;

	public SharedPatientMedicalCoverageBo(String affiliateNumber, Integer medicalCoverageId, String name, String cuit, Short type, Integer rnos, String acronym) {
		this.affiliateNumber = affiliateNumber;
		switch (EMedicalCoverageTypeDto.map(type)){
			case PREPAGA: this.medicalCoverage =  new SharedPrivateHealthInsuranceBo(medicalCoverageId, name, cuit, type);
				break;
			case OBRASOCIAL: this.medicalCoverage = new SharedHealthInsuranceBo(medicalCoverageId, name,cuit, rnos, acronym, type);
				break;
			case ART: this.medicalCoverage = new SharedARTCoverageBo(medicalCoverageId, name,cuit, type);
		}
	}



}
