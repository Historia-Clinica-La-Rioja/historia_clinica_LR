package net.pladema.hsi.addons.billing.application.port;

import net.pladema.hsi.addons.billing.domain.BillProceduresRequestBo;
import net.pladema.hsi.addons.billing.domain.BillProceduresResponseBo;

public interface BillProceduresPort {

	public BillProceduresResponseBo getBilling(BillProceduresRequestBo request);
}
