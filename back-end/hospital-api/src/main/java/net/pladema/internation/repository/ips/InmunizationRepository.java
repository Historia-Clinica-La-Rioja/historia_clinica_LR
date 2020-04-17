package net.pladema.internation.repository.ips;

import net.pladema.internation.repository.ips.entity.Inmunization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InmunizationRepository extends JpaRepository<Inmunization, Integer> {

}
