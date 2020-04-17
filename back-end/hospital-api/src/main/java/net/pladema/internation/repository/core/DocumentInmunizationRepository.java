package net.pladema.internation.repository.core;

import net.pladema.internation.repository.core.entity.DocumentInmunization;
import net.pladema.internation.repository.core.entity.DocumentInmunizationPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentInmunizationRepository extends JpaRepository<DocumentInmunization, DocumentInmunizationPK> {

}
