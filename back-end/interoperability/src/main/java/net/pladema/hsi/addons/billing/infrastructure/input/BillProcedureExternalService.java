package net.pladema.hsi.addons.billing.infrastructure.input;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.pladema.hsi.addons.billing.application.BillProcedures;
import net.pladema.hsi.addons.billing.infrastructure.input.domain.BillProceduresRequestDto;
import net.pladema.hsi.addons.billing.infrastructure.input.domain.BillProceduresResponseDto;

@Service
@RequiredArgsConstructor
public class BillProcedureExternalService {
	private final BillProcedures billProcedures;

	public BillProceduresResponseDto getBilledProcedures(BillProceduresRequestDto request) {

		return BillProceduresResponseDto.fromBo(billProcedures.run(request.toBo()));
	}
}
