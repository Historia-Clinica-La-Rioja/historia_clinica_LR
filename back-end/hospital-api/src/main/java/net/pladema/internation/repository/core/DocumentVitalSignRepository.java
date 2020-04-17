package net.pladema.internation.repository.core;

import net.pladema.internation.repository.core.entity.DocumentVitalSign;
import net.pladema.internation.repository.core.entity.DocumentVitalSignPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentVitalSignRepository extends JpaRepository<DocumentVitalSign, DocumentVitalSignPK> {

}
