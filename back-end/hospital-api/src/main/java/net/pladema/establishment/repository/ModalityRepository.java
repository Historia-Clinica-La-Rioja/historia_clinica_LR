package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.Modality;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ModalityRepository extends JpaRepository<Modality, Integer> {

	@Transactional(readOnly = true)
    @Query("SELECT m " +
            "FROM Modality AS m ")
	List<Modality> getAllModality();
}

