package net.pladema.snowstorm.services.loadCsv;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UpdateConceptsSynonymsResultBo {

	private String eclKey;

	private Integer conceptsLoaded;

	private Integer erroneousConcepts;

	private Integer missingMainConcepts;

	private List<String> errorMessages;

}
