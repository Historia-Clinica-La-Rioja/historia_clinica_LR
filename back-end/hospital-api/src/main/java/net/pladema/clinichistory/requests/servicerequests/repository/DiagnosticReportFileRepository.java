package net.pladema.clinichistory.requests.servicerequests.repository;

import net.pladema.clinichistory.requests.servicerequests.repository.domain.FileVo;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.DiagnosticReportFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DiagnosticReportFileRepository extends JpaRepository<DiagnosticReportFile, Integer> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE DiagnosticReportFile drf " +
            "SET drf.diagnosticReportId = :drId " +
            "WHERE drf.id IN :fileIds ")
    void updateDiagnosticReportFile(@Param("drId") Integer diagnosticReportId, @Param("fileIds") List<Integer> fileIds);

    @Transactional(readOnly = true)
    @Query("SELECT drf.path " +
            "FROM DiagnosticReportFile drf " +
            "WHERE drf.diagnosticReportId IS NULL ")
    List<String> getDanglingFiles();

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.clinichistory.requests.servicerequests.repository.domain.FileVo(drf.id, drf.name) " +
            "FROM DiagnosticReportFile drf " +
            "WHERE drf.diagnosticReportId = :drId ")
    List<FileVo> getFilesByDiagnosticReport(@Param("drId") Integer drId);
}
