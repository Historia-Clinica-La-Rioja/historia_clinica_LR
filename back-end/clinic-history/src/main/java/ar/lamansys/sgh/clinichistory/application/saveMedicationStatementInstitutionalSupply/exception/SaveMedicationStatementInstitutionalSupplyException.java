package ar.lamansys.sgh.clinichistory.application.saveMedicationStatementInstitutionalSupply.exception;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.ESaveMedicationStatementInstitutionSupplyException;
import lombok.Getter;

@Getter
public class SaveMedicationStatementInstitutionalSupplyException extends RuntimeException {

	private static final long serialVersionUID = -9030836598608982884L;

	private final ESaveMedicationStatementInstitutionSupplyException code;

	public SaveMedicationStatementInstitutionalSupplyException(ESaveMedicationStatementInstitutionSupplyException code, String message) {
		super(message);
		this.code = code;
	}

}
