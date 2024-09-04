package net.pladema.hsi.addons.billing.infrastructure.input.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.pladema.hsi.addons.billing.application.BillProceduresException;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BillProceduresExternalServiceException extends Exception{

	private BillProceduresException ex;

	public static BillProceduresExternalServiceException fromBillProceduresException(BillProceduresException ex){
		return new BillProceduresExternalServiceException(ex);
	}

	public String getCode() {
		return ex.getCode();
	}
	public String getReason() {return ex.getReason();}
	public String getErrorDetails() {return ex.getErrorDetails();}
}
