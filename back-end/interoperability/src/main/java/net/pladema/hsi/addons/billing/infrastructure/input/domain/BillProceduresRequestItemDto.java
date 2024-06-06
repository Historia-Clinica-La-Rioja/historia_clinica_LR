package net.pladema.hsi.addons.billing.infrastructure.input.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.pladema.hsi.addons.billing.domain.BillProceduresRequestItemBo;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class BillProceduresRequestItemDto {
	private String sctid;
	private String pt;

	public BillProceduresRequestItemBo toBo() {
		return new BillProceduresRequestItemBo(this.getSctid(), this.getPt());
	}
}
