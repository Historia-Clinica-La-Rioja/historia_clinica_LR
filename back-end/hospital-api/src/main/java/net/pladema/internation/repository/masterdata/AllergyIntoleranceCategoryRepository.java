package net.pladema.internation.repository.masterdata;

import net.pladema.internation.repository.masterdata.entity.AllergyIntoleranceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergyIntoleranceCategoryRepository extends JpaRepository<AllergyIntoleranceCategory, String> {

}
