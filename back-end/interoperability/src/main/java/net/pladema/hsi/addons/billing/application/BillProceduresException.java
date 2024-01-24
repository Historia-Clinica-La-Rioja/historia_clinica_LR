package net.pladema.hsi.addons.billing.application;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BillProceduresException extends Exception {

	public static final String ERROR_MSG = "Error al obtener los datos de pre-facturación";
	private String code;
	private String reason;
	private String errorDetails;
    public static BillProceduresException addonsBillingApiException(String errorDetails) {
    	return new BillProceduresException("ADDONS_API_EXCEPTION", ERROR_MSG, errorDetails);
    }

	public static BillProceduresException addonsBillingApiExceptionUnparsed(String body) {
		return addonsBillingApiException("Error al parsear mensaje de error");
	}

	public static BillProceduresException addonsBillingApiTimeoutException(String errorDetails) {
		return new BillProceduresException("ADDONS_API_TIMEOUT", ERROR_MSG, errorDetails);
	}

	public static BillProceduresException addonsBillingApiNotFoundException(String errorDetails) {
		return new BillProceduresException("ADDONS_API_NOT_FOUND", ERROR_MSG, errorDetails);
	}

	public static BillProceduresException addonsBillingApiUnauthorized(String errorDetails) {
		return new BillProceduresException("ADDONS_API_UNAUTHORIZED", ERROR_MSG, errorDetails);
	}

	public static BillProceduresException addonsBillingApiUnknown(String errorDetails) {
		return new BillProceduresException("ADDONS_API_UNKNOWN", ERROR_MSG, errorDetails);
	}
}
