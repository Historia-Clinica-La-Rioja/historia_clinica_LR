package net.pladema.nomivac.infrastructure.output.immunization.repository;

import ar.lamansys.sgx.shared.scheduling.infrastructure.output.repository.synchronization.AbstractSyncRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NomivacImmunizationSyncRepository extends AbstractSyncRepository<NomivacImmunizationSync, Integer>, JpaRepository<NomivacImmunizationSync, Integer> {
    @Transactional
    @Modifying
    @Query("UPDATE NomivacImmunizationSync instsync"
            + " SET instsync.priority = instsync.priority + 1"
            + " WHERE instsync.id = :id")
    void givePriority(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE NomivacImmunizationSync instsync"
            + " SET instsync.priority = 0, instsync.synchronizedDate = null"
            + " WHERE instsync.id = :idInstitucion")
    void resend(@Param("idInstitucion") Integer idInstitucion);

    @Query("SELECT isync FROM NomivacImmunizationSync isync" +
    		" LEFT JOIN VNomivacImmunizationData idata ON idata.id = isync.id" +
            " WHERE idata.updatedOn IS NOT NULL " +
    		" AND isync.priority > 0" +
            " ORDER BY isync.priority DESC, isync.synchronizedDate ASC")
    Slice<NomivacImmunizationSync> getPriorityElement(Pageable pageable);

    @Query("SELECT isync FROM NomivacImmunizationSync isync" +
    		" LEFT JOIN VNomivacImmunizationData idata ON idata.id = isync.id" +
            " WHERE idata.updatedOn IS NULL " +
            " ORDER BY isync.synchronizedDate ASC")
    Slice<NomivacImmunizationSync> getDeletedElements(Pageable pageable);
}
