package net.pladema.internation.repository.masterdata;

import net.pladema.internation.repository.masterdata.entity.AllergyIntoleranceClinicalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergyIntoleranceClinicalStatusRepository extends JpaRepository<AllergyIntoleranceClinicalStatus, String> {

}
