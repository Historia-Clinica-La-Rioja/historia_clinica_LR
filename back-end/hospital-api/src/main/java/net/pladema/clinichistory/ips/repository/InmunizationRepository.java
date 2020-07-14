package net.pladema.clinichistory.ips.repository;

import net.pladema.clinichistory.ips.repository.entity.Inmunization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InmunizationRepository extends JpaRepository<Inmunization, Integer> {

}
