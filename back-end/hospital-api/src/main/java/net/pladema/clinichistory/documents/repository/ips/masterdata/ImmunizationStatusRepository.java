package net.pladema.clinichistory.documents.repository.ips.masterdata;

import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.InmunizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImmunizationStatusRepository extends JpaRepository<InmunizationStatus, String> {

}
