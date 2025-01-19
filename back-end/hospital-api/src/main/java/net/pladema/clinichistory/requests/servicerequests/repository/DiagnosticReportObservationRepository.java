package net.pladema.clinichistory.requests.servicerequests.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.pladema.clinichistory.requests.servicerequests.repository.entity.DiagnosticReportObservation;

@Repository
public interface DiagnosticReportObservationRepository extends JpaRepository<DiagnosticReportObservation, Integer> {

    List<DiagnosticReportObservation> findByDiagnosticReportObservationGroupId(Integer groupId);

}
