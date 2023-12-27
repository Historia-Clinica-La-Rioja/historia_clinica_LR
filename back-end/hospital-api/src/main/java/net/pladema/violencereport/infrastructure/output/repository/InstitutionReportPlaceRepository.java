package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.embedded.InstitutionReportPlacePK;
import net.pladema.violencereport.infrastructure.output.repository.entity.InstitutionReportPlace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionReportPlaceRepository extends JpaRepository<InstitutionReportPlace, InstitutionReportPlacePK> {
}
