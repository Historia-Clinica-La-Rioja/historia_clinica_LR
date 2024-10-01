package ar.lamansys.sgh.shared.domain.medicineGroup;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MedicineGroupAuditBo {

	private String sctidMedicine;

	private String auditRequiredDescription;

	public MedicineGroupAuditBo(String sctidMedicine, String auditRequiredDescription) {
		this.sctidMedicine = sctidMedicine;
		this.auditRequiredDescription = auditRequiredDescription;
	}
}
