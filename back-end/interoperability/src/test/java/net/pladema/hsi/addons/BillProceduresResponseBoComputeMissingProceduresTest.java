package net.pladema.hsi.addons;

import net.pladema.hsi.addons.billing.domain.BillProceduresRequestBo;
import net.pladema.hsi.addons.billing.domain.BillProceduresRequestItemBo;
import net.pladema.hsi.addons.billing.domain.BillProceduresResponseBo;

import net.pladema.hsi.addons.billing.domain.BillProceduresResponseItemBo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BillProceduresResponseBoComputeMissingProceduresTest {
	String medicalCoverageCuit = "123";
	String medicalCoverageName = "abc";
	Float medicalCoverageTotal = 1.3F;
	Float patientTotal = 1.3F;
	boolean enabled = true;
	@Test
	public void procedures_not_billed_none_missing_encounter_doesnt_exist() {
		/**
		 * Request
		 * 2 procedures requested
		 */

		BillProceduresRequestBo request = makeBillProceduresRequestBo(
			List.of(
				new BillProceduresRequestItemBo("sctid1", "pt1"),
				new BillProceduresRequestItemBo("sctid2", "pt2")
			)
		);

		/**
		 * Response
		 * a response for each procedure requested
		 * + a response for another procedure
		 */
		var responseBo = new BillProceduresResponseBo(
			List.of(
					getBillProceduresResponseItemBo("pt1"),
					getBillProceduresResponseItemBo("pt2"),
					getBillProceduresResponseItemBo("pt3")
				),
			medicalCoverageTotal,
			patientTotal,
			medicalCoverageName,
			medicalCoverageCuit,
			enabled,
			request,
			false);

		Assertions.assertEquals(responseBo.getProceduresNotBilledCount(), 0);
	}

	@Test
	public void procedures_not_billed_none_missing_encounter_does_exist() {
		/**
		 * Request
		 * 2 procedures requested
		 */
		BillProceduresRequestBo request = makeBillProceduresRequestBo(
				List.of(
						new BillProceduresRequestItemBo("sctid1", "pt1"),
						new BillProceduresRequestItemBo("sctid2", "pt2")
				)
		);

		/**
		 * Response
		 * a response for each procedure requested
		 * + a response for another procedure
		 */
		var responseBo = new BillProceduresResponseBo(
				List.of(
						getBillProceduresResponseItemBo("pt1"),
						getBillProceduresResponseItemBo("pt2"),
						getBillProceduresResponseItemBo("pt3")
				),
				medicalCoverageTotal,
				patientTotal,
				medicalCoverageName,
				medicalCoverageCuit,
				enabled,
				request,
				true);

		Assertions.assertEquals(responseBo.getProceduresNotBilledCount(), 0);
	}

	@Test
	public void procedure_lacks_code_encounter_does_exist() {

		BillProceduresRequestBo request = makeBillProceduresRequestBo(
				List.of(
						new BillProceduresRequestItemBo("sctid1", "pt1"),
						new BillProceduresRequestItemBo("sctid2", "pt2")
				)
		);

		var responseBo = new BillProceduresResponseBo(
				List.of(
						getBillProceduresResponseItemBo("pt1"),
						getBillProceduresResponseItemBo("pt2"),
						getBillProceduresResponseItemBo("pt3"),
						getBillProceduresResponseItemBoMissingCode("pt4")
				),
				medicalCoverageTotal,
				patientTotal,
				medicalCoverageName,
				medicalCoverageCuit,
				enabled,
				request,
				true);

		Assertions.assertEquals(responseBo.getProceduresNotBilledCount(), 1);
	}

	@Test
	public void _1_non_registered_procedure_encounter_does_exist() {

		BillProceduresRequestBo request = makeBillProceduresRequestBo(
				List.of(
						new BillProceduresRequestItemBo("sctid1", "pt1"),
						new BillProceduresRequestItemBo("sctid2", "pt2")
				)
		);

		var responseBo = new BillProceduresResponseBo(
				List.of(
						getBillProceduresResponseItemBo("pt1"),
						getBillProceduresResponseItemBo("pt2"),
						getBillProceduresResponseItemBo("pt3"),
						getBillProceduresResponseItemBoNotRegistered("pt3")
				),
				medicalCoverageTotal,
				patientTotal,
				medicalCoverageName,
				medicalCoverageCuit,
				enabled,
				request,
				true);

		Assertions.assertEquals(responseBo.getProceduresNotBilledCount(), 0);

		var firstProcedure = responseBo.getProcedures().get(0);
		Assertions.assertNotEquals(firstProcedure.getCodeNomenclator(), firstProcedure.getDescription());

	}

	@Test
	public void procedures_not_billed_2_missing_encounter_doesnt_exist() {

		BillProceduresRequestBo request = makeBillProceduresRequestBo(
				List.of(
						new BillProceduresRequestItemBo("sctid1", "pt1"),
						new BillProceduresRequestItemBo("sctid2", "pt2"),
						new BillProceduresRequestItemBo("sctid1", "pt3")
				)
		);

		var responseBo = new BillProceduresResponseBo(
				List.of(
						getBillProceduresResponseItemBo("pt1")
				),
				medicalCoverageTotal,
				patientTotal,
				medicalCoverageName,
				medicalCoverageCuit,
				enabled,
				request,
				false);

		Assertions.assertEquals(responseBo.getProceduresNotBilledCount(), 2);
	}

	/**
	 * When the encounter exists in the remote server the missing
	 * procedures don't count as missing. It's assumed the remote
	 * decided to ignore them.
	 */
	@Test
	public void procedures_not_billed_2_missing_encounter_does_exist() {

		BillProceduresRequestBo request = makeBillProceduresRequestBo(
				List.of(
						new BillProceduresRequestItemBo("sctid1", "pt1"),
						new BillProceduresRequestItemBo("sctid2", "pt2"),
						new BillProceduresRequestItemBo("sctid1", "pt3")
				)
		);

		var responseBo = new BillProceduresResponseBo(
				List.of(
						getBillProceduresResponseItemBo("pt1")
				),
				medicalCoverageTotal,
				patientTotal,
				medicalCoverageName,
				medicalCoverageCuit,
				enabled,
				request,
				true);

		Assertions.assertEquals(responseBo.getProceduresNotBilledCount(), 0);
	}

	@Test
	public void procedures_not_billed_1_missing_with_repeated_response_encounter_doesnt_exist() {

		BillProceduresRequestBo request = makeBillProceduresRequestBo(
				List.of(
						new BillProceduresRequestItemBo("sctid1", "pt1"),
						new BillProceduresRequestItemBo("sctid2", "pt1"),
						new BillProceduresRequestItemBo("sctid1", "pt2"),
						new BillProceduresRequestItemBo("sctid1", "pt2"),
						new BillProceduresRequestItemBo("sctid1", "pt3")
				)
		);

		var responseBo = new BillProceduresResponseBo(
				List.of(
						getBillProceduresResponseItemBo("pt1"),
						getBillProceduresResponseItemBo("pt1"),
						getBillProceduresResponseItemBo("pt2"),
						getBillProceduresResponseItemBo("pt3")
				),
				medicalCoverageTotal,
				patientTotal,
				medicalCoverageName,
				medicalCoverageCuit,
				enabled,
				request,
				false);

		Assertions.assertEquals(responseBo.getProceduresNotBilledCount(), 1);
	}

	private static BillProceduresResponseItemBo getBillProceduresResponseItemBo(String pt) {
		return new BillProceduresResponseItemBo("code", "description", pt, 123,
		LocalDateTime.now(), 1.0F, 2.0F, 3.0F, 4.0F, 5.0F,
		BillProceduresResponseItemBo.PracticeType.PRACTICE);
	}

	private static BillProceduresResponseItemBo getBillProceduresResponseItemBoMissingCode(String pt) {
		return new BillProceduresResponseItemBo(null, "description", pt, 123,
				LocalDateTime.now(), 1.0F, 2.0F, 3.0F, 4.0F, 5.0F,
				BillProceduresResponseItemBo.PracticeType.PRACTICE);
	}

	private static BillProceduresResponseItemBo getBillProceduresResponseItemBoNotRegistered(String pt) {
		return new BillProceduresResponseItemBo(null, "description", pt, 123,
				LocalDateTime.now(), 1.0F, 2.0F, 3.0F, 4.0F, 5.0F,
				BillProceduresResponseItemBo.PracticeType.NON_REGISTERED);
	}


	private BillProceduresRequestBo makeBillProceduresRequestBo(List<BillProceduresRequestItemBo> procedures) {
		BillProceduresRequestBo request = new BillProceduresRequestBo(
				medicalCoverageCuit,
				LocalDateTime.now(),
				procedures,
				Optional.empty(),
				"sisaCode"
		);
		return request;
	}

}
