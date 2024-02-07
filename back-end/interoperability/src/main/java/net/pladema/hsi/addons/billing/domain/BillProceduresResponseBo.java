package net.pladema.hsi.addons.billing.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class BillProceduresResponseBo {
	@Getter(AccessLevel.NONE)
	List<BillProceduresResponseItemBo> _procedures;
	private Float medicalCoverageTotal;
	private Float patientTotal;
	private String medicalCoverageName;
	private String medicalCoverageCuit;
	private boolean enabled;
	private BillProceduresRequestBo request;
	private Integer proceduresNotBilledCount;
	private boolean encounterExistsInRemote;

	public BillProceduresResponseBo(List<BillProceduresResponseItemBo> procedures, Float medicalCoverageTotal, Float patientTotal, String medicalCoverageName, String medicalCoverageCuit, boolean enabled, BillProceduresRequestBo request, boolean encounterExistsInRemote) {
		this.medicalCoverageTotal = medicalCoverageTotal;
		this.patientTotal = patientTotal;
		this.medicalCoverageName = medicalCoverageName;
		this.medicalCoverageCuit = medicalCoverageCuit;
		this.enabled = enabled;
		this.request = request;
		this.encounterExistsInRemote = encounterExistsInRemote;
		computeProcedures(procedures);
	}

	public List<BillProceduresResponseItemBo> getProcedures() {
		return this._procedures;
	}

	/**
	 * All the requested lines are missing from the response.
	 * This is an alternative to throwing an exception. Instead, we mock
	 * an empty response.
	 * The proceduresNotBilledCount will be equal to the number of requested
	 * procedures.
	 */
	public static BillProceduresResponseBo allRequestedLinesMissing(BillProceduresRequestBo request, boolean enabled) {
		return new BillProceduresResponseBo(Collections.emptyList(), null, null, "", request.getMedicalCoverageCuit(), enabled, request, false);
	}

	/**
	 * See https://lamansys.atlassian.net/browse/HSI-3946 to understand the implemented use cases
	 */
	private void computeProcedures(List<BillProceduresResponseItemBo> procedures) {
		if (encounterExistsInRemote) computeProceduresEncounterExistInRemote(procedures);
		else computeProceduresEncounterDoesntExistInRemote(procedures);
	}

	private void computeProceduresEncounterExistInRemote(List<BillProceduresResponseItemBo> fetchedProcedures) {
		this._procedures = new ArrayList<>();
		this.proceduresNotBilledCount = 0;
		for (BillProceduresResponseItemBo fetchedProcedure: fetchedProcedures) {
			if (fetchedProcedure.isNonRegistered()) {
				if (fetchedProcedure.withoutDescriptionNomenclator())
					this._procedures.add(fetchedProcedure.useDescriptionAsDescriptionNomenclator());
				else
					this._procedures.add(fetchedProcedure);
			}
//			else if (notBilled(procedure, this.request.getProcedures())) {
//				// do nothing
//			}
			else if (fetchedProcedure.withoutCode()) {
				proceduresNotBilledCount += 1;
			}
			else if (fetchedProcedure.withoutPrice()){
				this._procedures.add(fetchedProcedure.clearPrice());
			}
			else {
				this._procedures.add(fetchedProcedure);
			}
		}
	}

	private void computeProceduresEncounterDoesntExistInRemote(List<BillProceduresResponseItemBo> fetchedProcedures) {
		this._procedures = new ArrayList<>();
		this.proceduresNotBilledCount = 0;
		for (BillProceduresResponseItemBo fetchedProcedure: fetchedProcedures) {
			if (fetchedProcedure.isNonRegistered()) {
				if (fetchedProcedure.withoutDescriptionNomenclator())
					this._procedures.add(fetchedProcedure.useDescriptionAsDescriptionNomenclator());
				else
					this._procedures.add(fetchedProcedure);
			}
			else if (fetchedProcedure.withoutCode()) {
				proceduresNotBilledCount += 1;
			}
			else if (fetchedProcedure.withoutPrice()){
				this._procedures.add(fetchedProcedure.clearPrice());
			}
			else {
				this._procedures.add(fetchedProcedure);
			}
		}
		this.proceduresNotBilledCount += computeProceduresNotBilledCount(this._procedures, request);
	}

	/**
	 * For each requested procedure missing in the getBilling response we
	 * return an empty line. These will be filled by hand on the printed report.
	 * See https://lamansys.atlassian.net/browse/HSI-3946
	 */
	private static Integer computeProceduresNotBilledCount(List<BillProceduresResponseItemBo> computedProcedures, BillProceduresRequestBo request) {
		Long missing = 0L;
		//The port only returns pts. Two requested procedures may have the same pt (but different sctid).
		//We need to find out how many of the requested procedures are missing from the billed ones.
		Map<String, Long> billedProceduresCount = countBilled(computedProcedures);
		for (BillProceduresRequestItemBo item : request.getProcedures()) {
			var found = billedProceduresCount.getOrDefault(item.getPt(), 0L);
			if (found <= 0) {
				missing += 1;
			} else {
				billedProceduresCount.put(item.getPt(), found - 1);
			}
		}
		return missing.intValue();
	}

	private static Map<String, Long> countBilled(List<BillProceduresResponseItemBo> computedProcedures) {
		return computedProcedures.stream().map(x -> x.getDescription()).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}

}
