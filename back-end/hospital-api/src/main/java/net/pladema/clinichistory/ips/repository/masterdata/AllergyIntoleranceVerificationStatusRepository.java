package net.pladema.clinichistory.ips.repository.masterdata;

import net.pladema.clinichistory.ips.repository.masterdata.entity.AllergyIntoleranceVerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergyIntoleranceVerificationStatusRepository extends JpaRepository<AllergyIntoleranceVerificationStatus, String> {

}
