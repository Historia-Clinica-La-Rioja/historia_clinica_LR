package net.pladema.establishment.application.port;


import net.pladema.establishment.service.domain.InternmentPatientBedRoomBo;

import java.time.LocalDateTime;

public interface HistoricPatientBedRelocationStorage {

	InternmentPatientBedRoomBo getLastBedByInternmentPatientDate(Integer internmentId, LocalDateTime requestDate);
}
