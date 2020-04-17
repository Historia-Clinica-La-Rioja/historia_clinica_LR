package net.pladema.internation.repository.ips;

import net.pladema.internation.repository.ips.entity.MedicationStatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationStatmentRepository extends JpaRepository<MedicationStatment, Integer> {

}
