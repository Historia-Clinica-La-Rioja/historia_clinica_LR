package net.pladema.clinichistory.ips.repository;

import net.pladema.clinichistory.ips.repository.entity.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProceduresRepository extends JpaRepository<Procedure, Integer> {

}
