package net.pladema.hsi.addons.billing.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.Value;

@Value
public class BillProceduresRequestBo {
	String medicalCoverageCuit;
	LocalDateTime date;
	List<BillProceduresRequestItemBo> procedures;
	Optional<Integer> encounterId;
	String sisaCode;
}
