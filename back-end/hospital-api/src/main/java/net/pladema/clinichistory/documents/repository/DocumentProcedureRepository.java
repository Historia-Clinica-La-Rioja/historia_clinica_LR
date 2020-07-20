package net.pladema.clinichistory.documents.repository;

import net.pladema.clinichistory.documents.repository.entity.DocumentProcedure;
import net.pladema.clinichistory.documents.repository.entity.DocumentProcedurePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentProcedureRepository extends JpaRepository<DocumentProcedure, DocumentProcedurePK> {

}
