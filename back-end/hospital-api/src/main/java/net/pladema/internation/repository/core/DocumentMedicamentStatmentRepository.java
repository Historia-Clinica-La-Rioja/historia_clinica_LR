package net.pladema.internation.repository.core;

import net.pladema.internation.repository.core.entity.DocumentMedicamentStatment;
import net.pladema.internation.repository.core.entity.DocumentMedicamentStatmentPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentMedicamentStatmentRepository extends JpaRepository<DocumentMedicamentStatment, DocumentMedicamentStatmentPK> {

}
