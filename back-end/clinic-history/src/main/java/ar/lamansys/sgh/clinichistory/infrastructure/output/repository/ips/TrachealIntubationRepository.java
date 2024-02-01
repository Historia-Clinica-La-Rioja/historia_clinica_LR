package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.TrachealIntubation;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.TrachealIntubationPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrachealIntubationRepository extends JpaRepository<TrachealIntubation, TrachealIntubationPK> {
}
