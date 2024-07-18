package net.pladema.imagenetwork.derivedstudies.repository;

import net.pladema.imagenetwork.derivedstudies.repository.entity.ResultStudies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ResultStudiesRepository extends JpaRepository<ResultStudies, Integer> {
	@Query(" SELECT rS " +
			"FROM ResultStudies rS " +
			"WHERE rS.idMove = :idMove")
	List<ResultStudies> findAllByIdMove(@Param("idMove") Integer idMove);

	@Transactional
	@Modifying
	@Query("DELETE FROM ResultStudies rS "+
			"WHERE rS.idMove = :idMove ")
	void deleteByIdMove(@Param("idMove") Integer idMove);
}

