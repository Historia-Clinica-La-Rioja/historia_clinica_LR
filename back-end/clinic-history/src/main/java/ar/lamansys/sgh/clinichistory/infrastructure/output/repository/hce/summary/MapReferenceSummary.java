package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.summary;

import ar.lamansys.sgh.clinichistory.domain.hce.summary.ReferenceSummaryBo;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferenceCounterReference;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MapReferenceSummary {

	private SharedReferenceCounterReference sharedReferenceCounterReference;

	public ReferenceSummaryBo processReferenceSummaryBo(Object[] a) {
		List<String> clinicalSpecialties = sharedReferenceCounterReference.getReferenceClinicalSpecialtiesName((Integer) a[0]);
		return new ReferenceSummaryBo((Integer) a[0], (String) a[1], clinicalSpecialties, a[2] != null ? (String) a[2] : null, (String) a[3], (Boolean) a[4]);
	}

}
