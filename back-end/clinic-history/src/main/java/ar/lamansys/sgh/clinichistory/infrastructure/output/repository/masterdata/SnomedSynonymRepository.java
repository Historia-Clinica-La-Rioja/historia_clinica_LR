package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata;


import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.SnomedSynonym;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.SnomedSynonymPK;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SnomedSynonymRepository extends JpaRepository<SnomedSynonym, SnomedSynonymPK> {
}
