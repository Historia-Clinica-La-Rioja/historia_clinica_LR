package net.pladema.person.repository;

import net.pladema.person.repository.entity.Ethnicity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EthnicityRepository extends JpaRepository<Ethnicity, Integer> {

    @Query(" SELECT e " +
            " FROM Ethnicity e " +
            " WHERE e.active = TRUE " +
            " ORDER BY e.pt ")
    List<Ethnicity> findAllActive();

    @Query(" SELECT e.id " +
            " FROM Ethnicity e " +
            " WHERE e.sctid = :sctid " +
            " AND e.pt = :pt ")
    Optional<Integer> findIdBySctidAndPt(@Param("sctid") String sctid, @Param("pt") String pt);

}
