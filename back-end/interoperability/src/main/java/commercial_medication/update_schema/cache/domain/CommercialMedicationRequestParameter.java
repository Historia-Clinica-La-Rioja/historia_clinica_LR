package commercial_medication.update_schema.cache.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommercialMedicationRequestParameter {

	public static final Character AFFIRMATIVE_REQUEST = 'S';

	public static final Character NEGATIVE_REQUEST = 'N';

	private Long lastLogId;

	private Character requestCompleteDatabase;

	private Character requestOnlyTables;

	private Character requestAtcDetails;

	public String toXmlString() {
		if (lastLogId == null && requestCompleteDatabase == null && requestOnlyTables == null && requestAtcDetails == null)
			return null;
		String result = "<parametros>";
		if (lastLogId != null)
			result = result + String.format("<ultimologMF>%s</ultimologMF>", lastLogId);
		if (requestCompleteDatabase != null)
			result = result + String.format("<basecompleta>%s</basecompleta>", requestCompleteDatabase);
		if (requestOnlyTables != null)
			result = result + String.format("<solotablas>%s</solotablas>", requestOnlyTables);
		if (requestAtcDetails != null)
			result = result + String.format("<atcdetalle>%s</atcdetalle>", requestAtcDetails);
		return result + "</parametros>";
	}

}
