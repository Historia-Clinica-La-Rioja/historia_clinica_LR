package net.pladema.hsi.addons.billing.infrastructure.input;

import net.pladema.hsi.addons.billing.application.BillProceduresException;

import net.pladema.hsi.addons.billing.infrastructure.input.exception.BillProceduresExternalServiceException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.pladema.hsi.addons.billing.application.BillProcedures;
import net.pladema.hsi.addons.billing.infrastructure.input.domain.BillProceduresRequestDto;
import net.pladema.hsi.addons.billing.infrastructure.input.domain.BillProceduresResponseDto;

@Service
@RequiredArgsConstructor
public class BillProceduresExternalService {
	private final BillProcedures billProcedures;

	public BillProceduresResponseDto getBilledProcedures(BillProceduresRequestDto request) throws BillProceduresExternalServiceException {

		try {
			return BillProceduresResponseDto.fromBo(billProcedures.run(request.toBo()));
		} catch (BillProceduresException e) {
			throw BillProceduresExternalServiceException.fromBillProceduresException(e);
		}
	}
}
