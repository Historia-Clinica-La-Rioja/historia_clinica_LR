package net.pladema.establishment.repository;

import java.time.LocalDateTime;
import java.util.stream.Stream;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.service.domain.InternmentPatientBedRoomBo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import net.pladema.establishment.repository.entity.HistoricPatientBedRelocation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HistoricPatientBedRelocationRepository extends SGXAuditableEntityJPARepository<HistoricPatientBedRelocation, Integer> {

	@Query(value = " SELECT hpbr FROM  HistoricPatientBedRelocation hpbr "
			+ " WHERE hpbr.internmentEpisodeId =:internmentEpisodeId ORDER BY hpbr.relocationDate DESC ")
	Stream<HistoricPatientBedRelocation> getAllByInternmentEpisode(
			@Param("internmentEpisodeId") Integer internmentEpisodeId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.establishment.service.domain.InternmentPatientBedRoomBo(b.bedNumber, r.description) " +
			"FROM HistoricPatientBedRelocation hpbr " +
			"JOIN InternmentEpisode ie ON hpbr.internmentEpisodeId = ie.id " +
			"JOIN Bed b ON hpbr.destinationBedId = b.id " +
			"JOIN Room r ON b.roomId = r.id " +
			"WHERE hpbr.creationable.createdOn <= :requestDate " +
			"AND ie.id = :internmentId " +
			"ORDER BY hpbr.relocationDate DESC")
	Page<InternmentPatientBedRoomBo> getLastBedByInternmentPatientDate(@Param("internmentId") Integer internmentId,
																	   @Param("requestDate") LocalDateTime requestDate,
																	   Pageable pageable);

}
