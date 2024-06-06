package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceReportSituationHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViolenceReportSituationHistoryRepository extends JpaRepository<ViolenceReportSituationHistory, Integer> {

	@Query("SELECT vrsh " +
			"FROM ViolenceReportSituationHistory vrsh " +
			"JOIN ViolenceReport vr ON (vr.id = vrsh.reportId) " +
			"WHERE vr.patientId = :patientId ")
	List<ViolenceReportSituationHistory> getAllByPatientId(@Param("patientId") Integer patientId);

	@Override
	@Modifying
	@Query("DELETE FROM ViolenceReportSituationHistory vrsh WHERE vrsh.id = :reportId")
	void deleteById(@Param("reportId") Integer reportId);

}
