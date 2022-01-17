package net.pladema.snowstorm.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SnomedGroupRepository extends JpaRepository<SnomedGroup, Integer> {

    @Query( " SELECT sg.id " +
            " FROM SnomedGroup sg " +
            " WHERE sg.ecl = :ecl ")
    Integer getIdByEcl(@Param("ecl") String ecl);

}
