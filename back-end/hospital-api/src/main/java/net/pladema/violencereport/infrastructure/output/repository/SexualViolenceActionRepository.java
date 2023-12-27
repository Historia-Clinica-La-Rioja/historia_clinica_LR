package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.embedded.SexualViolenceActionPK;
import net.pladema.violencereport.infrastructure.output.repository.entity.SexualViolenceAction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SexualViolenceActionRepository extends JpaRepository<SexualViolenceAction, SexualViolenceActionPK> {
}
