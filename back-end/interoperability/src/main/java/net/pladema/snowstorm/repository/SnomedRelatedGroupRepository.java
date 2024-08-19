package net.pladema.snowstorm.repository;

import java.util.List;
import java.util.Optional;


import net.pladema.cipres.domain.SnomedBo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.snowstorm.repository.entity.SnomedRelatedGroup;


@Repository
public interface SnomedRelatedGroupRepository extends JpaRepository<SnomedRelatedGroup, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.snowstorm.repository.entity.SnomedRelatedGroup(srg.id, srg.snomedId, srg.groupId, srg.orden, srg.lastUpdate) " +
			"FROM SnomedRelatedGroup srg " +
			"WHERE srg.groupId = :groupId " +
			"AND srg.snomedId = :snomedId ")
	Optional<SnomedRelatedGroup> getByGroupIdAndSnomedId(@Param("groupId") Integer groupId, @Param("snomedId") Integer snomedId);

	@Transactional(readOnly = true)
	@Query("SELECT MAX(srg.orden) " +
			"FROM SnomedRelatedGroup srg " +
			"WHERE srg.groupId = :groupId ")
	Optional<Integer> getLastOrdenByGroupId(@Param("groupId") Integer groupId);


	@Transactional(readOnly = true)
	@Query("SELECT srg.snomedId " +
			"FROM SnomedRelatedGroup srg " +
			"WHERE srg.id = :id ")
	Optional<Integer> getSnomedIdById(@Param("id") Integer id);

	@Transactional(readOnly = true)
	@Query( "SELECT DISTINCT new net.pladema.cipres.domain.SnomedBo(s.id, s.sctid, s.pt) " +
			"FROM SnomedGroup sg " +
			"JOIN SnomedGroup baseGroup ON sg.groupId = baseGroup.id " +
			"JOIN SnomedRelatedGroup srg ON sg.id = srg.groupId " +
			"JOIN Snomed s ON srg.snomedId = s.id " +
			"WHERE sg.groupType = :snomedGroupTypeId " +
			"AND baseGroup.description = :description " +
			"AND sg.institutionId = :institutionId " +
			"AND sg.userId is null ")
	List<SnomedBo> getAllByInstitutionAndDescriptionAndGroupType(@Param("institutionId") Integer institutionId,
																 @Param("description") String description,
																 @Param("snomedGroupTypeId") Short snomedGroupTypeId);

	@Transactional(readOnly = true)
	@Query( "SELECT DISTINCT new net.pladema.cipres.domain.SnomedBo(s.id, s.sctid, s.pt) " +
			"FROM SnomedGroup sg " +
			"JOIN SnomedGroup baseGroup ON (sg.groupId = baseGroup.id) " +
			"JOIN SnomedRelatedGroup srg ON (sg.id = srg.groupId) " +
			"JOIN Snomed s ON (srg.snomedId = s.id) " +
			"WHERE sg.groupType = :snomedGroupTypeId " +
			"AND baseGroup.description = :description " +
			"AND sg.userId is null ")
	List<SnomedBo> getAllByDescriptionAndGroupType(@Param("description") String description,
												   @Param("snomedGroupTypeId") Short snomedGroupTypeId);

	@Transactional(readOnly = true)
	@Query( "SELECT DISTINCT new net.pladema.cipres.domain.SnomedBo(s.id, s.sctid, s.pt) " +
			"FROM SnomedGroup sg " +
			"JOIN SnomedGroup baseGroup ON (sg.groupId = baseGroup.id) " +
			"JOIN SnomedRelatedGroup srg ON (sg.id = srg.groupId) " +
			"JOIN Snomed s ON (srg.snomedId = s.id) " +
			"JOIN CareLineInstitutionPractice clip ON (clip.snomedRelatedGroupId = srg.id) " +
			"JOIN CareLineInstitution cli ON (clip.careLineInstitutionId = cli.id) " +
			"WHERE sg.groupType = :snomedGroupTypeId " +
			"AND cli.careLineId = :careLineId " +
			"AND baseGroup.description = :description " +
			"AND sg.userId is null ")
	List<SnomedBo> getAllByDescriptionAndGroupTypeAndCareLineId(@Param("description") String description,
												   @Param("snomedGroupTypeId") Short snomedGroupTypeId,
													@Param("careLineId") Integer careLineId);

	@Transactional(readOnly = true)
	@Query("SELECT srg.id " +
			"FROM SnomedRelatedGroup srg " +
			"WHERE srg.snomedId IN :snomedIds")
	List<Integer> getIdsBySnomedIds(@Param("snomedIds") List<Integer> snomedIds);

	@Transactional(readOnly = true)
	@Query( "SELECT DISTINCT new net.pladema.cipres.domain.SnomedBo(s.id, s.sctid, s.pt) " +
			"FROM SnomedGroup sg " +
			"JOIN SnomedGroup baseGroup ON sg.groupId = baseGroup.id " +
			"JOIN SnomedRelatedGroup srg ON sg.id = srg.groupId " +
			"JOIN Snomed s ON srg.snomedId = s.id " +
			"JOIN Institution i ON (sg.institutionId = i.id) " +
			"JOIN Address a ON (i.addressId = a.id) " +
			"JOIN City c ON (a.cityId = c.id) " +
			"WHERE sg.groupType = :snomedGroupTypeId " +
			"AND baseGroup.description = :description " +
			"AND c.departmentId = :departmentId")
	List<SnomedBo> getAllByDepartmentId(@Param("description") String description,
										@Param("snomedGroupTypeId") Short snomedGroupTypeId,
										@Param("departmentId") Short departmentId);

	@Transactional(readOnly = true)
	@Query("SELECT srg.snomedId " +
			"FROM SnomedRelatedGroup srg " +
			"WHERE srg.groupId = :groupId")
	List<Integer> getConceptsIdsByGroupId(@Param("groupId") Integer groupId);

}
