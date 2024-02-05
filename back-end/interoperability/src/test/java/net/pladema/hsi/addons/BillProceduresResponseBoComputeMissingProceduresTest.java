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
	public void procedures_not_billed_none_missing() {
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
		 * a response for each porocedure requested
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
			request);

		Assertions.assertEquals(responseBo.getProceduresNotBilledCount(), 0);
	}

	@Test
	public void procedures_not_billed_2_missing() {

		/**
		 * Request
		 */

		BillProceduresRequestBo request = makeBillProceduresRequestBo(
				List.of(
						new BillProceduresRequestItemBo("sctid1", "pt1"),
						new BillProceduresRequestItemBo("sctid2", "pt2"),
						new BillProceduresRequestItemBo("sctid1", "pt3")
				)
		);

		/**
		 * Response
		 */
		var responseBo = new BillProceduresResponseBo(
				List.of(
						getBillProceduresResponseItemBo("pt1")
				),
				medicalCoverageTotal,
				patientTotal,
				medicalCoverageName,
				medicalCoverageCuit,
				enabled,
				request);

		Assertions.assertEquals(responseBo.getProceduresNotBilledCount(), 2);
	}

	@Test
	public void procedures_not_billed_1_missing_with_repeated_response() {

		/**
		 * Request
		 */

		BillProceduresRequestBo request = makeBillProceduresRequestBo(
				List.of(
						new BillProceduresRequestItemBo("sctid1", "pt1"),
						new BillProceduresRequestItemBo("sctid2", "pt1"),
						new BillProceduresRequestItemBo("sctid1", "pt2"),
						new BillProceduresRequestItemBo("sctid1", "pt2"),
						new BillProceduresRequestItemBo("sctid1", "pt3")
				)
		);

		/**
		 * Response
		 */

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
				request);

		Assertions.assertEquals(responseBo.getProceduresNotBilledCount(), 1);
	}

	private static BillProceduresResponseItemBo getBillProceduresResponseItemBo(String pt) {
		return new BillProceduresResponseItemBo("code", "description", pt, 123, LocalDateTime.now(), 1.0F, 2.0F, 3.0F, 4.0F, 5.0F);
	}


	private BillProceduresRequestBo makeBillProceduresRequestBo(List<BillProceduresRequestItemBo> procedures) {
		BillProceduresRequestBo request = new BillProceduresRequestBo(
				medicalCoverageCuit,
				LocalDateTime.now(),
				procedures,
				Optional.empty()
		);
		return request;
	}

}
