package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentElectronicSignatureRejectReason;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentElectronicSignatureRejectReasonRepository extends JpaRepository<DocumentElectronicSignatureRejectReason, Integer> {
}
