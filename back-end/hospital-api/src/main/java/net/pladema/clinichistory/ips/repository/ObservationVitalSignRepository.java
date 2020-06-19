package net.pladema.clinichistory.ips.repository;

import net.pladema.clinichistory.ips.repository.entity.ObservationVitalSign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObservationVitalSignRepository extends JpaRepository<ObservationVitalSign, Integer> {

}
