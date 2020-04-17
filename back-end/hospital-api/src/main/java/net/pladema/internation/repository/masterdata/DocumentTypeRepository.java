package net.pladema.internation.repository.masterdata;

import net.pladema.internation.repository.masterdata.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Short> {

}
