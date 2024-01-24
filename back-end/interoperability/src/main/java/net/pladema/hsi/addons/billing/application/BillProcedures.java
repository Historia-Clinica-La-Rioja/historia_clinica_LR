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

	public BillProceduresResponseBo run(BillProceduresRequestBo request) throws BillProceduresException {
		return billProceduresPort.getBilling(request);
	}
}
