package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SnomedRepository extends JpaRepository<Snomed, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT s.id " +
            "FROM Snomed s " +
            "WHERE s.sctid = :sctid " +
            "AND s.pt = :pt ")
    Optional<Integer> findIdBySctidAndPt(@Param("sctid") String sctid, @Param("pt") String pt);

    @Transactional(readOnly = true)
    @Query("SELECT MAX(s.id) " +
            "FROM Snomed s " +
            "WHERE s.sctid = :sctid ")
    Optional<Integer> findLatestIdBySctid(@Param("sctid") String sctid);
}
