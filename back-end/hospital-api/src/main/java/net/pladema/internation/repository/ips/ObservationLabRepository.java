package net.pladema.internation.repository.ips;

import net.pladema.internation.repository.ips.entity.ObservationLab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObservationLabRepository extends JpaRepository<ObservationLab, Integer>{

}
