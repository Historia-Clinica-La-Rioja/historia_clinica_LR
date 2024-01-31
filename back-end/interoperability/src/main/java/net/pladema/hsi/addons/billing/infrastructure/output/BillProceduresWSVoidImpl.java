package net.pladema.hsi.addons.billing.infrastructure.output;

import java.util.Collections;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.pladema.hsi.addons.billing.application.port.BillProceduresPort;
import net.pladema.hsi.addons.billing.domain.BillProceduresRequestBo;
import net.pladema.hsi.addons.billing.domain.BillProceduresResponseBo;

@Slf4j
@ConditionalOnProperty(
		value="ws.addons.billing.enabled",
		havingValue = "false",
		matchIfMissing = true
)
@Service
public class BillProceduresWSVoidImpl implements BillProceduresPort {

	public BillProceduresResponseBo getBilling(BillProceduresRequestBo request) {
		return buildResponse(request);
	}

	private BillProceduresResponseBo buildResponse(BillProceduresRequestBo request) {
		return BillProceduresResponseBo.allRequestedLinesMissing(request, false);
	}
}