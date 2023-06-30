package net.pladema.medicalconsultation.appointment.repository;

import net.pladema.medicalconsultation.appointment.repository.entity.HistoricDeriveReport;
import net.pladema.medicalconsultation.appointment.repository.entity.HistoricDeriveReportPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricDeriveReportRepository extends JpaRepository<HistoricDeriveReport, HistoricDeriveReportPK> {
}
