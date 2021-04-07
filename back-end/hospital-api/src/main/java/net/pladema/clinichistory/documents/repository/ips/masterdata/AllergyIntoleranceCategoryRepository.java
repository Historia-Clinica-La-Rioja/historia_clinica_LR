package net.pladema.clinichistory.documents.repository.ips.masterdata;

import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.AllergyIntoleranceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergyIntoleranceCategoryRepository extends JpaRepository<AllergyIntoleranceCategory, Short> {

}
