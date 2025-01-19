package net.pladema.establishment.service.impl;

import lombok.AllArgsConstructor;
import net.pladema.establishment.application.port.HistoricPatientBedRelocationStorage;
import net.pladema.establishment.service.FetchLastBedByInternmentPatientDate;

import net.pladema.establishment.service.domain.InternmentPatientBedRoomBo;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class FetchLastBedByInternmentPatientDateImpl implements FetchLastBedByInternmentPatientDate {

	private final HistoricPatientBedRelocationStorage historicPatientBedRelocationStorage;

	@Override
	public InternmentPatientBedRoomBo run(Integer internmentId, LocalDateTime requestDate) {
		return historicPatientBedRelocationStorage.getLastBedByInternmentPatientDate(internmentId, requestDate);
	}
}
