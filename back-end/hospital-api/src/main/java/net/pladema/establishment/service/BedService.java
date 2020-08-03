package net.pladema.establishment.service;

import java.util.List;
import java.util.Optional;

import net.pladema.establishment.repository.domain.BedInfoVo;
import net.pladema.establishment.repository.domain.BedSummaryVo;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.repository.entity.HistoricPatientBedRelocation;
public interface BedService {

    Bed updateBedStatusOccupied(Integer id);

	public Optional<Bed> freeBed(Integer bedId);
	
	public List<Bed> getFreeBeds(Integer institutionId, Integer clinicalSpecialtyId); 
	
	public HistoricPatientBedRelocation addPatientBedRelocation(HistoricPatientBedRelocation patientBedRelocation);
	
	public Optional<HistoricPatientBedRelocation> getLastPatientBedRelocation(Integer internmentEpisodeId);
	
	public Optional<BedInfoVo> getBedInfo(Integer bedId);
	
	public List<BedSummaryVo> getBedSummary(Integer institutionId);

}
