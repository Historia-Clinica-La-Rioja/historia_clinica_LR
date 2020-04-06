package net.pladema.auditable.entity;

import java.time.LocalDateTime;
import java.time.Month;

public class AuditBean {

	private AuditBean() { }

	/**
	 * Genera un audit con una fecha generada a partir de un número dado.
	 * La intensión es poder generar fácilmente un Audit particular y luego poder usarlo en los asserts.
	 *
	 * @param dateSeed  un número del 1 al 12.
	 * @return
	 */
	public static Audit newAudit(int dateSeed) {
		Audit audit = new Audit();
		audit.setCreatedOn(buildDate(dateSeed));
		audit.setUpdatedOn(buildDate(dateSeed));
		return audit;
	}

	private static LocalDateTime buildDate(int dateSeed) {
		return LocalDateTime.of(
			2000 + dateSeed,  // year
			Month.of(dateSeed),		// month
			dateSeed,				// dayOfMonth
			dateSeed,				// hour
			dateSeed,				// minute
			dateSeed,				// second
			0			// nanoOfSecond
		);
	}
}