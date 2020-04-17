package net.pladema.internation.repository.ips;

import net.pladema.internation.repository.ips.entity.HealthCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthConditionRepository extends JpaRepository<HealthCondition, Integer> {

}
