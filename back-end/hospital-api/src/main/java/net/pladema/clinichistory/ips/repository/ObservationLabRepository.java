package net.pladema.clinichistory.ips.repository;

import net.pladema.clinichistory.ips.repository.entity.ObservationLab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObservationLabRepository extends JpaRepository<ObservationLab, Integer>{

}
