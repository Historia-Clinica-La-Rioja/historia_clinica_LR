package ar.lamansys.sgh.clinichistory.application.ports.output;

import ar.lamansys.sgh.clinichistory.domain.ips.SaveMedicationStatementInstitutionalSupplyBo;

public interface MedicationStatementInstitutionalSupplyPort {

	Integer save(SaveMedicationStatementInstitutionalSupplyBo saveMedicationStatementInstitutionalSupplyBo);

}
