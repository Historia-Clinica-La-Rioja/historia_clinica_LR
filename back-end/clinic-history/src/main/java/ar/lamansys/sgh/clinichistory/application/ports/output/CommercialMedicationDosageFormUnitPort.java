package ar.lamansys.sgh.clinichistory.application.ports.output;

import java.util.List;

public interface CommercialMedicationDosageFormUnitPort {

	List<String> getAllValuesByDosageFormName(String name);

}
