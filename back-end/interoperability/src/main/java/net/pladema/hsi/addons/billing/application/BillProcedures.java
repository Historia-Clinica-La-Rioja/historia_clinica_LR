package net.pladema.hsi.addons.billing.application;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.pladema.hsi.addons.billing.application.port.BillProceduresPort;
import net.pladema.hsi.addons.billing.domain.BillProceduresRequestBo;
import net.pladema.hsi.addons.billing.domain.BillProceduresResponseBo;

@Service
@RequiredArgsConstructor
public class BillProcedures {

	private final BillProceduresPort billProceduresPort;

	/**
	 * @param request
	 * @return
	 * @throws BillProceduresException
	 */
	public BillProceduresResponseBo run(BillProceduresRequestBo request) throws BillProceduresException {
		try {
			return billProceduresPort.getBilling(request);
		} catch (BillProceduresException e) {
			return failStrategy(e, request);
		}
	}

	/**
	 * If the port call fails we act as if all the requested lines
	 * were missing from the response
	 *
	 * @param e
	 * @param request
	 * @return
	 */
	private BillProceduresResponseBo failStrategy(BillProceduresException e, BillProceduresRequestBo request) {
		return BillProceduresResponseBo.allRequestedLinesMissing(request, e.isEnabled());
	}
}
