package net.pladema.imagenetwork.derivedstudies.repository;

import net.pladema.imagenetwork.derivedstudies.repository.entity.MoveStudies;
import net.pladema.imagenetwork.imagequeue.domain.ImageQueueBo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MoveStudiesRepository extends JpaRepository<MoveStudies, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT mo " +
			"FROM MoveStudies AS mo " +
			"WHERE mo.status != :status " +
			"ORDER BY mo.orchestratorId ")
	List<MoveStudies> getListMoveStudies(@Param("status") String status);

	@Override
	@Transactional(readOnly = true)
	@Query("SELECT mo " +
			"FROM MoveStudies AS mo " +
			"WHERE mo.result != '200' " +
			"ORDER BY mo.orchestratorId ")
	Page<MoveStudies> findAll(Pageable pageable);

	@Transactional(readOnly = true)
	@Query("SELECT mo " +
			"FROM MoveStudies AS mo " +
			"WHERE mo.result != '200' " +
			"AND mo.imageId LIKE :imageId")
	Page<MoveStudies> findByImageId(@Param("imageId") String imageId, Pageable pageable);

	@Transactional
	@Modifying
	@Query("UPDATE MoveStudies AS mo " +
			"SET mo.status = :status " +
			"WHERE mo.id = :idMove")
	void updateStatus(@Param("idMove") Integer idMove,
						 @Param("status") String status);

	@Transactional
	@Modifying
	@Query("UPDATE MoveStudies AS mo " +
			"SET mo.sizeImage = :size " +
			"WHERE mo.id = :idMove")
	void updateSize(@Param("idMove") Integer idMove,
					  @Param("size") Integer size);

	@Transactional
	@Modifying
	@Query("UPDATE MoveStudies AS mo " +
			"SET mo.imageId = :imageId " +
			"WHERE mo.id = :idMove")
	void updateImageId(@Param("idMove") Integer idMove,
					@Param("imageId") String imageId);

	@Transactional
	@Modifying
	@Query("UPDATE MoveStudies AS mo " +
			"SET mo.status = :status, mo.result= :result " +
			"WHERE mo.id = :idMove")
	void updateStatusandResult(@Param("idMove") Integer idMove,
							   @Param("status") String status,
							   @Param("result") String result);

	@Transactional
	@Modifying
	@Query("UPDATE MoveStudies AS mo " +
			"SET mo.beginOfMove = :date " +
			"WHERE mo.id = :idMove")
	void updateBeginOfMove(@Param("idMove") Integer idMove,
							   @Param("date") Date date);

	@Transactional
	@Modifying
	@Query("UPDATE MoveStudies AS mo " +
			"SET mo.endOfMove = :date " +
			"WHERE mo.id = :idMove")
	void updateEndOfMove(@Param("idMove") Integer idMove,
						   @Param("date") Date date);

	@Transactional
	@Modifying
	@Query("UPDATE MoveStudies AS mo " +
			"SET mo.attempsNumber = :attempsNumbers " +
			"WHERE mo.id = :idMove")
	void updateAttemps(@Param("idMove") Integer idMove,
					@Param("attempsNumbers") Integer attempsNumbers);

	@Transactional(readOnly = true)
	@Query("SELECT mo " +
			"FROM MoveStudies AS mo " +
			"WHERE mo.result != '200' " +
			"ORDER BY mo.orchestratorId ")

	List<MoveStudies> listFailed();


	@Transactional(readOnly = true)
	@Query("SELECT mo.institutionId " +
			"FROM MoveStudies AS mo " +
			"WHERE mo.id = :idMove")
	Optional<Integer> findInstitutionId(@Param("idMove") Integer idMove);

	@Transactional(readOnly = true)
	@Query("SELECT mo.institutionId " +
			"FROM MoveStudies AS mo " +
			"WHERE mo.appointmentId = :appointmentId")
	Optional<Integer> getInstitutionIdByAppointmetId(@Param("appointmentId") Integer appointmentId);

	@Transactional(readOnly = true)
	@Query("SELECT mo.sizeImage " +
			"FROM MoveStudies AS mo " +
			"WHERE mo.appointmentId = :appointmentId")
	Optional<Integer> getSizeImageByAppointmentId(@Param("appointmentId") Integer appointmentId);

	@Transactional(readOnly = true)
	@Query(" SELECT new net.pladema.imagenetwork.imagequeue.domain.ImageQueueBo( " +
			"	mo.id, " +
			"	mo.appointmentId, " +
			"	a.patientId, " +
			"	e.modalityId, " +
			" 	e.id, " +
			"	aoi.orderId, " +
			"	aoi.studyId, " +
			"	aoi.transcribedOrderId, " +
			"	mo.status, " +
			"	mo.beginOfMove, " +
			"	mo.endOfMove, " +
			"	a.dateTypeId, " +
			"	a.hour, " +
			"	mo.imageId " +
			") " +
			"FROM MoveStudies mo " +
			"JOIN AppointmentOrderImage aoi ON (aoi.pk.appointmentId = mo.appointmentId) " +
			"JOIN Appointment a ON (mo.appointmentId = a.id) " +
			"JOIN EquipmentAppointmentAssn AS eaa ON (a.id = eaa.pk.appointmentId) " +
			"JOIN EquipmentDiary ed ON (ed.id = eaa.pk.equipmentDiaryId) " +
			"JOIN Equipment e ON (e.id = ed.equipmentId) " +
			"WHERE (mo.institutionId = :institutionId) " +
			"AND (mo.result IS NULL or mo.result != :resultNot) " +
			"AND (a.dateTypeId BETWEEN :from AND :to) " +
			"ORDER BY mo.id")
	List<ImageQueueBo> findImagesNotMovedByInstitutionId(
			@Param("institutionId") Integer institutionId,
			@Param("from") LocalDate from,
			@Param("to") LocalDate to,
			@Param("resultNot") String resultNot
	);

	@Transactional
	@Modifying
	@Query("UPDATE MoveStudies AS mo " +
			"SET mo.status = :status, mo.result= :result, mo.attempsNumber = :attemptsNumbers " +
			"WHERE mo.id = :idMove")
	void updateStatusAndResultAndAttemptsNumbre(@Param("idMove") Integer idMove,
							   @Param("status") String status,
							   @Param("result") String result,
							   @Param("attemptsNumbers") Integer attemptsNumbers);

	@Modifying
	@Query("UPDATE MoveStudies AS mo " +
			"SET mo.status = :updateStatus, mo.attempsNumber = :attemptsNumbers " +
			"WHERE mo.result != :result "+
			"AND mo.status = :status "+
			"AND mo.orchestratorId = :orchestratorId "+
			"AND DATE(mo.moveDate) = :date")
	void updateFailedCurrentDate(@Param("date") LocalDate date,
								 @Param("orchestratorId")Integer orchestratorId,
								 @Param("status") String status,
								 @Param("result") String result,
								 @Param("updateStatus") String updateStatus,
								 @Param("attemptsNumbers") Integer attemptsNumbers);

}
