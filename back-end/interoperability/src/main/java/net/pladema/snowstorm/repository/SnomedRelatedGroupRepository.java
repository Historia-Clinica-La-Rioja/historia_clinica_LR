package net.pladema.snowstorm.repository;

import net.pladema.snowstorm.repository.entity.SnomedRelatedGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SnomedRelatedGroupRepository extends JpaRepository<SnomedRelatedGroup, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.snowstorm.repository.entity.SnomedRelatedGroup(srg.id, srg.snomedId, srg.groupId, srg.orden, srg.lastUpdate) " +
			"FROM SnomedRelatedGroup srg " +
			"WHERE srg.groupId = :groupId " +
			"AND srg.snomedId = :snomedId ")
	Optional<SnomedRelatedGroup> getByGroupIdAndSnomedId(@Param("groupId") Integer groupId, @Param("snomedId") Integer snomedId);

}
