package net.pladema.establishment.repository;

import net.pladema.establishment.domain.bed.EmergencyCareBedBo;
import net.pladema.establishment.repository.domain.BedInfoVo;
import net.pladema.establishment.repository.entity.Bed;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisodeStatus.ACTIVE;

@Repository
public interface BedRepository extends JpaRepository<Bed, Integer> {


	@Transactional(readOnly = true)
	@Query(value = " SELECT b FROM  Bed b "
			+ " JOIN Room r ON b.roomId = r.id"
			+ " JOIN Sector s ON r.sectorId = s.id " + " WHERE s.institutionId =:institutionId")
	List<Bed> getAllByInstitution(@Param("institutionId") Integer institutionId);


	@Transactional(readOnly = true)
	@Query(value = " SELECT b FROM  Bed b "
			+ " JOIN Room r ON b.roomId = r.id"
			+ " JOIN Sector s ON r.sectorId = s.id "
			+ " WHERE b.roomId = :roomId AND s.institutionId =:institutionId")
	List<Bed> getAllByRoomAndInstitution(@Param("roomId") Integer roomId,
			@Param("institutionId") Integer institutionId);


	@Transactional(readOnly = true)
	@Query(value = " SELECT b FROM  Bed b "
			+ " JOIN Room r ON b.roomId = r.id"
			+ " JOIN Sector s ON r.sectorId = s.id "
			+ "WHERE b.free = true AND b.roomId = :roomId AND s.institutionId =:institutionId")
	List<Bed> getAllFreeBedsByRoomAndInstitution(@Param("roomId") Integer roomId,
			@Param("institutionId") Integer institutionId);


	@Transactional(readOnly = true)
	@Query(value = "SELECT s.institutionId " +
			"FROM  Bed b " +
			"INNER JOIN Room r ON (b.roomId = r.id) " +
			"INNER JOIN Sector s ON (r.sectorId = s.id) " +
			"WHERE b.id = :id ")
	Integer getInstitutionId(@Param("id") Integer id);

	@Transactional(readOnly = true)
	@Query(value = "SELECT b.id FROM Bed b")
    List<Integer> getAllIds();

	@Transactional(readOnly = true)
	@Query(value = "SELECT b.id " +
			"FROM  Bed b " +
			"INNER JOIN Room r ON (b.roomId = r.id) " +
			"INNER JOIN Sector s ON (r.sectorId = s.id) " +
			"WHERE s.institutionId IN :institutionsIds")
	List<Integer> getAllIdsByInstitutionsId(@Param("institutionsIds") List<Integer> institutionsIds);

	@Transactional(readOnly = true)
	@Query(value = "SELECT b " +
			"FROM  Bed b " +
			"INNER JOIN Room r ON (b.roomId = r.id) " +
			"INNER JOIN Sector s ON (r.sectorId = s.id) " +
			"WHERE s.institutionId IN :institutionsIds")
	List<Bed> getAllIdsByInstitutions(List<Integer> allowedInstitutions);
	
	@Transactional(readOnly = true)
	@Query(value = " SELECT b FROM  Bed b "
			+ " JOIN Room r ON b.roomId = r.id"
			+ " JOIN Sector s ON r.sectorId = s.id "
			+ " JOIN ClinicalSpecialtySector css ON css.sectorId = s.id "
			+ "WHERE b.free = true "
			+ "AND s.institutionId =:institutionId "
			+ "AND css.clinicalSpecialtyId = :clinicalSpecialtyId")
	List<Bed> getFreeBedsByClinicalSpecialty(@Param("institutionId") Integer institutionId,
			@Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);
	
	@Transactional(readOnly = true)
	@Query(value = " SELECT NEW net.pladema.establishment.repository.domain.BedInfoVo( "
			+ " b, r, s, pat.id, per, it.description, ie.probableDischargeDate, g.id, g.description, "
			+ " pe.id, pe.identificationNumber, us.pk.userId, "
			+ " COALESCE(status.isBlocked, false) "
			+ " ) "
			+ " FROM Bed b "
			+ " JOIN Room r ON b.roomId = r.id"
			+ " JOIN Sector s ON r.sectorId = s.id "
			+ " LEFT JOIN InternmentEpisode ie ON b.id = ie.bedId "
			+ " LEFT JOIN EmergencyCareEpisode ece ON b.id = ece.bedId "
			+ " LEFT JOIN Patient pat ON ie.patientId = pat.id OR pat.id = ece.patientId "
			+ " LEFT JOIN Person per ON pat.personId = per.id "
			+ " LEFT JOIN Gender g ON per.genderId = g.id "
			+ " LEFT JOIN IdentificationType it ON per.identificationTypeId = it.id "
			+ " LEFT JOIN UserPerson us ON us.pk.userId = b.inchargeNurseId "
			+ " LEFT JOIN Person pe ON us.pk.personId = pe.id"
			+ " LEFT JOIN AttentionPlaceStatus status ON b.statusId = status.id "
			+ " WHERE b.id = :bedId AND "
			+ " ( b.free = true OR (b.free = false AND ie.statusId = "+ ACTIVE + " OR s.sectorTypeId = 3) "
			+ " AND NOT EXISTS (SELECT pd.id FROM PatientDischarge pd where pd.internmentEpisodeId = ie.id AND pd.physicalDischargeDate IS NOT NULL) )")
	Stream<BedInfoVo> getBedInfo(@Param("bedId") Integer bedId);

	@Transactional(readOnly = true)
	@Query("SELECT CASE WHEN count(*) > 0 THEN TRUE ELSE FALSE END " +
			"FROM Bed b " +
			"WHERE b.id = :bedId AND b.free IS TRUE AND b.available IS TRUE ")
	boolean isBedFreeAndAvailable(@Param("bedId") Integer bedId);

	@Transactional(readOnly = true)
	@Query("SELECT new net.pladema.establishment.domain.bed.EmergencyCareBedBo(" +
			"	b.id," +
			"	CONCAT(r.description, ' - ', b.bedNumber), " +
			"	CASE WHEN b.free = true AND b.available = true THEN true ELSE false END, " +
			"	s.description," +
			"	r.description," +
			"	b.bedNumber," +
			"	COALESCE(status.isBlocked, false) " +
			")" +
			"FROM Bed b " +
			"JOIN Room r ON r.id = b.roomId " +
			"JOIN Sector s ON s.id = r.sectorId " +
			"LEFT JOIN AttentionPlaceStatus status ON b.statusId = status.id " +
			"WHERE r.sectorId = :sectorId")
	List<EmergencyCareBedBo> findAllEmergencyCareBedBySectorId(@Param("sectorId") Integer sectorId);

	@Transactional(readOnly = true)
	@Query("SELECT new net.pladema.establishment.domain.bed.EmergencyCareBedBo(" +
			"	b.id," +
			"	CONCAT(r.description, ' - ', b.bedNumber), " +
			"	CASE WHEN b.free = true AND b.available = true THEN true ELSE false END, " +
			"	s.description," +
			"	r.description," +
			"	b.bedNumber, " +
			"	COALESCE(status.isBlocked, false) " +
			")" +
			"FROM Bed b " +
			"JOIN Room r ON r.id = b.roomId " +
			"JOIN Sector s ON s.id = r.sectorId " +
			"LEFT JOIN AttentionPlaceStatus status ON b.statusId = status.id " +
			"WHERE b.id = :id")
	Optional<EmergencyCareBedBo> findEmergencyCareBedById(@Param("id") Integer id);

	@Transactional(readOnly = true)
	@Query(value = " SELECT b FROM Bed b "
			+ " JOIN Room r ON b.roomId = r.id"
			+ " JOIN Sector s ON r.sectorId = s.id "
			+ " WHERE b.id = :bedId AND s.institutionId =:institutionId")
	Optional<Bed> findByIdAndInstitutionId(@Param("bedId") Integer bedId, @Param("institutionId") Integer institutionId);
}
