package ar.lamansys.sgh.publicapi.prescription.application.port.out;

import java.util.regex.Pattern;

import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.BadPrescriptionIdFormatException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PrescriptionIdentifier {
	public final String domain;
	public final Integer prescriptionId;

	private static final String ID_DIVIDER = "-";
	public static PrescriptionIdentifier parse(String prescriptionId) throws BadPrescriptionIdFormatException {
		var parts = prescriptionId.split(ID_DIVIDER);
		assertFormatPrescriptionId(parts);
		return new PrescriptionIdentifier(parts[0], Integer.valueOf(parts[1]));
	}

	private static void assertFormatPrescriptionId(String[] parts) throws BadPrescriptionIdFormatException {
		Pattern pattern = Pattern.compile("\\d+");
		if(parts.length != 2 || !pattern.matcher(parts[0]).matches() || !pattern.matcher(parts[1]).matches()) {
			throw new BadPrescriptionIdFormatException();
		}
	}
}
