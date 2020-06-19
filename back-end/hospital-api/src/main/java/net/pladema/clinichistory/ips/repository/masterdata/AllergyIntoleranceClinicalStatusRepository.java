package net.pladema.clinichistory.ips.repository.masterdata;

import net.pladema.clinichistory.ips.repository.masterdata.entity.AllergyIntoleranceClinicalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergyIntoleranceClinicalStatusRepository extends JpaRepository<AllergyIntoleranceClinicalStatus, String> {

}
