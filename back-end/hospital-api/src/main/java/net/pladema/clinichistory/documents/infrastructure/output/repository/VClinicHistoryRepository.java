package net.pladema.clinichistory.documents.infrastructure.output.repository;

import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.VClinicHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VClinicHistoryRepository extends JpaRepository<VClinicHistory, Long> {
}
