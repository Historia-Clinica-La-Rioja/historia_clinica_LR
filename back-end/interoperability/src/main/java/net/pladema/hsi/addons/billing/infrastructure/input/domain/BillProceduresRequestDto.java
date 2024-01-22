package net.pladema.hsi.addons.billing.infrastructure.input.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.pladema.hsi.addons.billing.domain.BillProceduresRequestBo;
import net.pladema.hsi.addons.billing.domain.BillProceduresRequestItemBo;

public class BillProceduresRequestDto {

	private final String medicalCoverageCuit;
	private final LocalDateTime date;
	private Set<BillProceduresRequestItemDto> procedures;
	public BillProceduresRequestDto(String medicalCoverageCuit, LocalDateTime date) {
		this.medicalCoverageCuit = medicalCoverageCuit;
		this.date = date;
		this.procedures = new HashSet<>();
	}

	public void addProcedure(String sctid, String pt) {
		procedures.add(new BillProceduresRequestItemDto(sctid, pt));
	}

    public BillProceduresRequestBo toBo() {
    	return new BillProceduresRequestBo(this.medicalCoverageCuit, this.date, proceduresToBo());
    }

	private List<BillProceduresRequestItemBo> proceduresToBo() {
		return this.procedures.stream().map(BillProceduresRequestItemDto::toBo).collect(Collectors.toList());
	}
}
