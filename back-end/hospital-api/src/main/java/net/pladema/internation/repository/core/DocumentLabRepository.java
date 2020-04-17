package net.pladema.internation.repository.core;

import net.pladema.internation.repository.core.entity.DocumentLab;
import net.pladema.internation.repository.core.entity.DocumentLabPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentLabRepository extends JpaRepository<DocumentLab, DocumentLabPK> {

}
