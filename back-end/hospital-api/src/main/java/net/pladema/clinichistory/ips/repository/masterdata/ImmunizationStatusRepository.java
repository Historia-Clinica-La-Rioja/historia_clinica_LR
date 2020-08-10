package net.pladema.clinichistory.ips.repository.masterdata;

import net.pladema.clinichistory.ips.repository.masterdata.entity.InmunizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImmunizationStatusRepository extends JpaRepository<InmunizationStatus, String> {

}
