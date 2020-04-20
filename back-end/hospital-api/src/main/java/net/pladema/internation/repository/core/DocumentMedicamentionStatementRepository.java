package net.pladema.internation.repository.core;

import net.pladema.internation.repository.core.entity.DocumentMedicamentionStatement;
import net.pladema.internation.repository.core.entity.DocumentMedicamentionStatementPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentMedicamentionStatementRepository extends JpaRepository<DocumentMedicamentionStatement, DocumentMedicamentionStatementPK> {

}
