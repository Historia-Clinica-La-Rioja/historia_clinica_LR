package ar.lamansys.refcounterref.infraestructure.output.repository.file;

import ar.lamansys.refcounterref.domain.file.ReferenceCounterReferenceFileBo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReferenceCounterReferenceFileRepository extends JpaRepository<ReferenceCounterReferenceFile, Integer> {

    @Transactional(readOnly = true)
    @Query(value = " SELECT rcrf " +
            "FROM ReferenceCounterReferenceFile rcrf " +
            "WHERE rcrf.id = :fileId " +
            "AND rcrf.type = :type")
    Optional<ReferenceCounterReferenceFile> findByIdAndType(@Param("fileId") Integer fileId, @Param("type") Integer type);

    @Transactional(readOnly = true)
    @Query(value = " SELECT new ar.lamansys.refcounterref.domain.file.ReferenceCounterReferenceFileBo(rcrf.referenceCounterReferenceId, " +
            "rcrf.id, rcrf.name) " +
            "FROM ReferenceCounterReferenceFile rcrf " +
            "WHERE rcrf.referenceCounterReferenceId IN (:referenceCounterReferenceIds) " +
            "AND rcrf.type = :type")
    List<ReferenceCounterReferenceFileBo> findByReferenceCounterReferenceIdsAndType(@Param("referenceCounterReferenceIds") List<Integer> referenceCounterReferenceIds,
                                                                    @Param("type") Integer type);

    @Transactional(readOnly = true)
    @Query(value = " SELECT new ar.lamansys.refcounterref.domain.file.ReferenceCounterReferenceFileBo(rcrf.referenceCounterReferenceId, " +
            "rcrf.id, rcrf.name) " +
            "FROM ReferenceCounterReferenceFile rcrf " +
            "WHERE rcrf.referenceCounterReferenceId = :referenceCounterReferenceId " +
            "AND rcrf.type = :type")
    List<ReferenceCounterReferenceFileBo> findByReferenceCounterReferenceIdAndType(@Param("referenceCounterReferenceId") Integer referenceCounterReferenceId, @Param("type") Integer type);

}
