package net.pladema.establishment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import net.pladema.establishment.repository.domain.BedInfoVo;
import net.pladema.establishment.repository.domain.BedSummaryVo;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.repository.entity.HistoricPatientBedRelocation;

public interface BedService {

    Bed updateBedStatusOccupied(Integer id);

	Optional<Bed> freeBed(Integer bedId);
	
	List<Bed> getFreeBeds(Integer institutionId, Integer clinicalSpecialtyId);
	
	HistoricPatientBedRelocation addPatientBedRelocation(HistoricPatientBedRelocation patientBedRelocation);
	
	Optional<HistoricPatientBedRelocation> getLastPatientBedRelocation(Integer internmentEpisodeId);

	Optional<HistoricPatientBedRelocation> getBedIdByDateTime(Integer internmentEpisodeId, LocalDateTime localDateTime);

	Optional<BedInfoVo> getBedInfo(Integer bedId);
	
	List<BedSummaryVo> getBedSummary(Integer institutionId, Short[] sectorType);

	void updateBedNurse(Integer userId, Integer bedId);

	boolean isBedFreeAndAvailable(Integer bedId);
}
