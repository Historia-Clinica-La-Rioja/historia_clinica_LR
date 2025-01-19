package net.pladema.snowstorm.services.loadCsv;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateConceptsResultBo {

	public final Integer conceptsLoaded;

	public final Integer erroneousConcepts;

	public final List<String> errorMessages;

	public final Integer missingMainConcepts;

}
