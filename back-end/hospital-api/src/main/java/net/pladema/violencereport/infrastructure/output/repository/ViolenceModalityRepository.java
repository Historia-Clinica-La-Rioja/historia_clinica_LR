package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.embedded.ViolenceReportSnomedPK;
import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceModality;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViolenceModalityRepository extends JpaRepository<ViolenceModality, ViolenceReportSnomedPK> {
}
