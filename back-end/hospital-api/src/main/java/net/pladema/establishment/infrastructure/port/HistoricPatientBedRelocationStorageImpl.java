package net.pladema.establishment.infrastructure.port;

import lombok.AllArgsConstructor;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentSummaryBo;
import net.pladema.establishment.application.port.HistoricPatientBedRelocationStorage;
import net.pladema.establishment.repository.HistoricPatientBedRelocationRepository;
import net.pladema.establishment.service.domain.InternmentPatientBedRoomBo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class HistoricPatientBedRelocationStorageImpl implements HistoricPatientBedRelocationStorage {

	private final HistoricPatientBedRelocationRepository historicPatientBedRelocationRepository;

	private final InternmentEpisodeService internmentEpisodeService;

	@Override
	public InternmentPatientBedRoomBo getLastBedByInternmentPatientDate(Integer internmentId, LocalDateTime requestDate) {
		Page<InternmentPatientBedRoomBo> result = historicPatientBedRelocationRepository.getLastBedByInternmentPatientDate(internmentId, requestDate, PageRequest.of(0, 1));
		if (!result.getContent().isEmpty())
			return new InternmentPatientBedRoomBo(result.getContent().get(0).getBed(), result.getContent().get(0).getRoom());
		InternmentSummaryBo summary = internmentEpisodeService.getIntermentSummary(internmentId).orElseThrow();
		return new InternmentPatientBedRoomBo(summary.getBedNumber(), summary.getRoomNumber());
	}
}
