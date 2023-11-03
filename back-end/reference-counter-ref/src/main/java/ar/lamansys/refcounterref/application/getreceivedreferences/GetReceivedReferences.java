package ar.lamansys.refcounterref.application.getreceivedreferences;

import ar.lamansys.refcounterref.application.port.ReferenceReportStorage;
import ar.lamansys.refcounterref.domain.ReferenceReportBo;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GetReceivedReferences {

	private final ReferenceReportStorage referenceReportStorage;

	public List<ReferenceReportBo> run(Integer institutionId, LocalDate from, LocalDate to) {
		return referenceReportStorage.fetchReceivedReferencesReport(institutionId, from, to);
	}

}
