package net.pladema.snowstorm.repository;

import net.pladema.snowstorm.repository.domain.SnomedTemplateSearchVo;
import net.pladema.snowstorm.repository.entity.SnomedGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SnomedGroupRepository extends JpaRepository<SnomedGroup, Integer> {

    @Query( " SELECT sg.id " +
            " FROM SnomedGroup sg " +
            " WHERE sg.ecl = :ecl " +
			"	AND sg.description = :description " +
			" 	AND sg.groupId IS NULL ")
    Integer getBaseGroupIdByEclAndDescription(@Param("ecl") String ecl, @Param("description") String description);

	@Query( " SELECT sg.id " +
			" FROM SnomedGroup sg " +
			" WHERE sg.description = :description ")
	Integer getIdByDescription(@Param("description") String description);

	@Query( " SELECT NEW net.pladema.snowstorm.repository.domain.SnomedTemplateSearchVo(sg.id, sg.description, s.id, s.sctid, s.pt) " +
			" FROM SnomedGroup sg " +
			" JOIN SnomedGroup baseGroup ON (sg.groupId = baseGroup.id) " +
			" JOIN SnomedRelatedGroup srg ON (sg.id = srg.groupId) " +
			" JOIN Snomed s ON (srg.snomedId = s.id) " +
			" WHERE sg.template = TRUE " +
			" 	AND fts(sg.description, :searchTerm ) = TRUE " +
			" 	AND baseGroup.ecl = :baseGroupEcl " +
			" 	AND baseGroup.description = :baseGroupDescription " +
			" 	AND baseGroup.groupId IS NULL " +
			" 	AND (sg.institutionId IS NULL OR sg.institutionId = :institutionId) " +
			" 	AND (sg.userId IS NULL OR sg.userId = :userId) ")
	List<SnomedTemplateSearchVo> searchByTemplateName(@Param("searchTerm") String searchTerm,
													  @Param("baseGroupEcl") String baseGroupEcl,
													  @Param("baseGroupDescription") String baseGroupDescription,
													  @Param("institutionId") Integer institutionId,
													  @Param("userId") Integer userId);

	@Query( " SELECT NEW net.pladema.snowstorm.repository.domain.SnomedTemplateSearchVo(sg.id, sg.description, s.id, s.sctid, s.pt) " +
			" FROM SnomedGroup sg " +
			" JOIN SnomedGroup baseGroup ON (sg.groupId = baseGroup.id) " +
			" JOIN SnomedRelatedGroup srg ON (sg.id = srg.groupId) " +
			" JOIN Snomed s ON (srg.snomedId = s.id) " +
			" WHERE sg.template = TRUE " +
			" 	AND baseGroup.ecl = :baseGroupEcl " +
			" 	AND baseGroup.description = :baseGroupDescription " +
			" 	AND baseGroup.groupId IS NULL " +
			" 	AND (sg.institutionId IS NULL OR sg.institutionId = :institutionId) " +
			" 	AND (sg.userId IS NULL OR sg.userId = :userId) ")
	List<SnomedTemplateSearchVo> getTemplates(@Param("baseGroupEcl") String baseGroupEcl,
											  @Param("baseGroupDescription") String baseGroupDescription,
											  @Param("institutionId") Integer institutionId,
											  @Param("userId") Integer userId);


	@Transactional(readOnly = true)
	@Query("SELECT sg " +
			"FROM SnomedGroup sg " +
			"WHERE sg.institutionId = :institutionId " +
			"AND sg.groupId = :groupId " +
			"AND sg.groupType = :groupType ")
	Optional<SnomedGroup> findByInstitutionIdAndGroupIdAndGroupType(@Param("institutionId") Integer institutionId,
																	@Param("groupId") Integer groupId,
																	@Param("groupType") Short groupType);

}
