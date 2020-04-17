package net.pladema.internation.repository.core;

import net.pladema.internation.repository.core.entity.DocumentAllergyIntolerance;
import net.pladema.internation.repository.core.entity.DocumentAllergyIntolerancePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentAllergyIntoleranceRepository extends JpaRepository<DocumentAllergyIntolerance, DocumentAllergyIntolerancePK> {

}
