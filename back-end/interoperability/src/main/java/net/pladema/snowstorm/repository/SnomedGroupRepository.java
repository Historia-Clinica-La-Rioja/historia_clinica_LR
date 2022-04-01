package net.pladema.snowstorm.repository;

import net.pladema.snowstorm.repository.entity.SnomedGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SnomedGroupRepository extends JpaRepository<SnomedGroup, Integer> {

    @Query( " SELECT sg.id " +
            " FROM SnomedGroup sg " +
            " WHERE sg.ecl = :ecl " +
			"	AND sg.description = :description " +
			" 	AND sg.groupId IS NULL ")
    Integer getBaseGroupIdByEclAndDescription(@Param("ecl") String ecl, @Param("description") String description);

}
