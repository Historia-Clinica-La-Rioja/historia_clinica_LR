package net.pladema.establishment.repository;

import java.util.stream.Stream;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.pladema.establishment.repository.entity.HistoricPatientBedRelocation;

@Repository
public interface HistoricPatientBedRelocationRepository extends SGXAuditableEntityJPARepository<HistoricPatientBedRelocation, Integer> {

	@Query(value = " SELECT hpbr FROM  HistoricPatientBedRelocation hpbr "
			+ " WHERE hpbr.internmentEpisodeId =:internmentEpisodeId ORDER BY hpbr.relocationDate DESC ")
	Stream<HistoricPatientBedRelocation> getAllByInternmentEpisode(
			@Param("internmentEpisodeId") Integer internmentEpisodeId);

}
