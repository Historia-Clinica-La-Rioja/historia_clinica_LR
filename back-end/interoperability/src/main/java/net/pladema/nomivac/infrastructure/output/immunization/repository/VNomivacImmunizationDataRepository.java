package net.pladema.nomivac.infrastructure.output.immunization.repository;

import ar.lamansys.sgx.shared.scheduling.infrastructure.output.repository.synchronization.AbstractDataRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VNomivacImmunizationDataRepository extends AbstractDataRepository<VNomivacImmunizationData, Integer>, JpaRepository<VNomivacImmunizationData, Integer> {

    @Query("SELECT idata FROM VNomivacImmunizationData idata" +
    		" LEFT JOIN NomivacImmunizationSync isync ON idata.id = isync.id" +
            " WHERE isync.synchronizedDate < idata.updatedOn " +
            " OR  isync.synchronizedDate IS NULL " +
            " ORDER BY idata.updatedOn ASC")
    Slice<VNomivacImmunizationData> getDataToSynch(Pageable pageable);
}
