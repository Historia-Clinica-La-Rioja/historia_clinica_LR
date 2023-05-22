package net.pladema.cipres.application.getpatientid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.cipres.application.port.CipresPatientStorage;

import net.pladema.cipres.domain.BasicDataPersonBo;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetPatientId {

	private final CipresPatientStorage cipresPatientStorage;

	public Integer run(BasicDataPersonBo basicDataPersonBo) {
		return cipresPatientStorage.getPatientId(basicDataPersonBo);
	}

}