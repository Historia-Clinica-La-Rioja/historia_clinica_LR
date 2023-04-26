package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.Modality;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ModalityRepository extends JpaRepository<Modality, Integer> {

	@Transactional(readOnly = true)
    @Query("SELECT m " +
            "FROM Modality AS m ")
	List<Modality> getAllModality();

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT m " +
			"FROM Institution i " +
			"JOIN Sector s ON i.id = s.institutionId " +
			"JOIN Equipment e ON s.id = e.sectorId " +
			"JOIN EquipmentDiary ed ON e.id = ed.equipmentId " +
			"JOIN EquipmentAppointmentAssn eaa ON ed.id = eaa.pk.equipmentDiaryId " +
			"JOIN AppointmentOrderImage aoi ON aoi.pk.appointmentId = eaa.pk.appointmentId " +
			"JOIN Modality m ON e.modalityId = m.id " +
			"WHERE i.id = :institutionId " +
			"AND aoi.completed = true")
	List<Modality> getModalitiesByStudiesCompleted(@Param("institutionId") Integer institutionId);

}

