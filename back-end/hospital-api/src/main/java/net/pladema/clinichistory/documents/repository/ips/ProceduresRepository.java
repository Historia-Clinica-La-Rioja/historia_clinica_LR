package net.pladema.clinichistory.documents.repository.ips;

import net.pladema.clinichistory.documents.repository.ips.entity.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProceduresRepository extends JpaRepository<Procedure, Integer> {

}
