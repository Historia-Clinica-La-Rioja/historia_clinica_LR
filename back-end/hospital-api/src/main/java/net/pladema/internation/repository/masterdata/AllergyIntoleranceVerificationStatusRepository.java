package net.pladema.internation.repository.masterdata;

import net.pladema.internation.repository.masterdata.entity.AllergyIntoleranceVerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergyIntoleranceVerificationStatusRepository extends JpaRepository<AllergyIntoleranceVerificationStatus, String> {

}
