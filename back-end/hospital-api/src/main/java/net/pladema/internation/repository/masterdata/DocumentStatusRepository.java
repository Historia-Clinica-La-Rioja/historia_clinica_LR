package net.pladema.internation.repository.masterdata;

import net.pladema.internation.repository.masterdata.entity.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentStatusRepository extends JpaRepository<DocumentStatus, String> {

}
