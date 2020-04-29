package net.pladema.internation.repository.ips;

import net.pladema.internation.repository.ips.entity.ObservationVitalSign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObservationVitalSignRepository extends JpaRepository<ObservationVitalSign, Integer>, ObservationVitalSignRepositoryCustom {

}
