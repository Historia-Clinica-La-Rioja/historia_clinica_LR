package ar.lamansys.sgh.shared.domain.medicineGroup;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MedicineGroupAuditFinancedBo {

	private Boolean financed;

	private List<String> auditRequiredDescription;

	public MedicineGroupAuditFinancedBo(Boolean financed, List<String> auditRequiredDescription) {
		this.financed = financed;
		this.auditRequiredDescription = auditRequiredDescription;
	}
}


