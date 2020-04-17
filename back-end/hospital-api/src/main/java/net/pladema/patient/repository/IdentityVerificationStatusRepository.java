package net.pladema.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.pladema.patient.repository.entity.IdentityVerificationStatus;

@Repository
public interface IdentityVerificationStatusRepository extends JpaRepository<IdentityVerificationStatus, Short> {
}
