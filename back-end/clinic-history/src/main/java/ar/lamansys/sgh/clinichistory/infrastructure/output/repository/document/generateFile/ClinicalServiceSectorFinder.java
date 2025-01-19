package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtySectorDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.clinicalspecialtysector.SharedClinicalSpecialtySectorPort;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ClinicalServiceSectorFinder {

	private  final SharedClinicalSpecialtySectorPort sharedClinicalSpecialtySectorPort;

	public ClinicalSpecialtySectorDto getClinicalSpecialtySector(Integer id){
		return sharedClinicalSpecialtySectorPort.getClinicalSpecialtySectorById(id).orElse(null);
	}
}
