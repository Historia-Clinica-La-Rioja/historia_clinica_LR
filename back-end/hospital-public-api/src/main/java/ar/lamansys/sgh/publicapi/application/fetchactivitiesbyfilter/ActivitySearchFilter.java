package ar.lamansys.sgh.publicapi.application.fetchactivitiesbyfilter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@ToString
public final class ActivitySearchFilter {

	@NotNull
	private final String refsetCode;
	private final String identificationNumber;
	private final LocalDate from;
	private final LocalDate to;
	private final Boolean reprocessing;
	private final String coverageCuit;
}
