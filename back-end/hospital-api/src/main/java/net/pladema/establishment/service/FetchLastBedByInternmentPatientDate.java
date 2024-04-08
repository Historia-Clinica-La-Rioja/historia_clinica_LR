package net.pladema.establishment.service;

import net.pladema.establishment.service.domain.InternmentPatientBedRoomBo;

import java.time.LocalDateTime;

public interface FetchLastBedByInternmentPatientDate {

	InternmentPatientBedRoomBo run(Integer internmentId, LocalDateTime requestDate);
}
