package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.embedded.ViolenceReportSnomedPK;
import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViolenceTypeRepository extends JpaRepository<ViolenceType, ViolenceReportSnomedPK> {
}
