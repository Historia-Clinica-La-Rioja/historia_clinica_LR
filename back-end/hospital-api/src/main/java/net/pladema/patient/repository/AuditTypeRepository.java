package net.pladema.patient.repository;

import net.pladema.patient.repository.entity.AuditType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditTypeRepository extends JpaRepository<AuditType, Short> {
}
