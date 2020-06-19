package net.pladema.clinichistory.ips.repository.masterdata;

import net.pladema.clinichistory.ips.repository.masterdata.entity.Snomed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnomedRepository extends JpaRepository<Snomed, String> {

}
