package net.pladema.hsi.addons.billing.domain;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Value;
import net.pladema.hsi.addons.billing.application.BillProceduresException;

@Value
public class BillProceduresResponseBo {
	List<BillProceduresResponseItemBo> procedures;
	private Float medicalCoverageTotal;
	private Float patientTotal;
	private String medicalCoverageName;
	private String medicalCoverageCuit;
	private boolean enabled;
	private BillProceduresRequestBo request;
	@Getter(lazy = true)
	private Integer proceduresNotBilledCount = computeProceduresNotBilledCount();

	private Integer computeProceduresNotBilledCount() {
		Long missing = 0L;
		//The port only returns pts. Two requested procedures may have the same pt (but different sctid).
		//We need to find out how many of the requested procedures are missing from the billed ones.
		Map<String, Long> billedProceduresCount = countBilled(this.getProcedures());
		for (BillProceduresRequestItemBo item : request.getProcedures()) {
			var found = billedProceduresCount.getOrDefault(item.getPt(), 0L);
			if (found <= 0) {
				missing += 1;
			}
			else {
				billedProceduresCount.put(item.getPt(), found - 1);
			}
		}
		return missing.intValue();
	}

	private Map<String, Long> countBilled(List<BillProceduresResponseItemBo> billed) {
		return billed
				.stream()
				.map(x -> x.getSnomedPt())
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}

}
