package net.pladema.internation.repository.core;

import net.pladema.internation.repository.core.entity.DocumentHistoric;
import net.pladema.internation.repository.core.entity.DocumentHistoricPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentHistoricRepository extends JpaRepository<DocumentHistoric, DocumentHistoricPK> {

}
