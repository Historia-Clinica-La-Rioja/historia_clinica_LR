package net.pladema.establishment.repository;

import net.pladema.establishment.repository.domain.BedInfoVo;
import net.pladema.establishment.repository.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
			+ "  b, r, s, pat.id, per, it.description, ie.probableDischargeDate ) "
			+ " FROM Bed b "
			+ " JOIN Room r ON b.roomId = r.id"
			+ " JOIN Sector s ON r.sectorId = s.id "
			+ " LEFT JOIN InternmentEpisode ie ON b.id = ie.bedId"
			+ " LEFT JOIN Patient pat ON ie.patientId = pat.id "
			+ " LEFT JOIN Person per ON pat.personId = per.id "
			+ " LEFT JOIN IdentificationType it ON per.identificationTypeId = it.id "
			+ " WHERE b.id =:bedId AND "
			+ " ( b.free = true OR (b.free = false AND ie.statusId = "+ ACTIVE + ") "
			+ " AND NOT EXISTS (SELECT pd.id FROM PatientDischarge pd where pd.internmentEpisodeId = ie.id AND pd.physicalDischargeDate IS NOT NULL) )")
	Stream<BedInfoVo> getBedInfo(@Param("bedId") Integer bedId);
	
}
