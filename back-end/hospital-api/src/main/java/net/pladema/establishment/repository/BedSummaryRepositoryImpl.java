package net.pladema.establishment.repository;

import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisodeStatus;
import net.pladema.establishment.repository.domain.BedSummaryVo;
import net.pladema.establishment.repository.domain.HierarchicalUnitVo;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.repository.entity.HierarchicalUnit;
import net.pladema.establishment.repository.entity.Sector;
import net.pladema.establishment.repository.entity.SectorType;
import net.pladema.staff.repository.domain.ClinicalSpecialtyVo;
import net.pladema.staff.repository.entity.ClinicalSpecialty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BedSummaryRepositoryImpl implements BedSummaryRepository{

    private final EntityManager entityManager;

    public BedSummaryRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

	@Override
    @Transactional(readOnly = true)
    public List<BedSummaryVo> execute(Integer institutionId, Short[] sectorsType) {
        String sqlQuery =
                " SELECT b, s, MAX(ie.probableDischargeDate), cs, ct.description, "
                + " so.description, ag.description, st, hu, "
                + " COALESCE(status.isBlocked, false) "
                + " FROM Bed b "
                + " JOIN Room r ON b.roomId = r.id "
                + " JOIN Sector s ON r.sectorId = s.id "
				+ " JOIN SectorType st ON s.sectorTypeId = st.id "
                + " LEFT JOIN CareType ct ON s.careTypeId = ct.id "
                + " LEFT JOIN SectorOrganization so ON s.sectorOrganizationId = so.id "
                + " LEFT JOIN AgeGroup ag ON s.ageGroupId = ag.id "
                + " LEFT JOIN VClinicalServiceSector vcs ON s.id = vcs.sectorId "
                + " LEFT JOIN ClinicalSpecialty cs ON vcs.clinicalSpecialtyId = cs.id "
                + " LEFT JOIN InternmentEpisode ie ON b.id = ie.bedId "
				+ " LEFT JOIN HierarchicalUnitSector hus ON (hus.sectorId = s.id) "
				+ " LEFT JOIN HierarchicalUnit hu ON (hus.hierarchicalUnitId = hu.id) "
				+ " LEFT JOIN AttentionPlaceStatus status ON b.statusId = status.id "
                + " WHERE s.institutionId = :institutionId "
				+ " AND s.sectorTypeId IN (:sectorsType) "
                + " AND (b.free=true OR ( b.free=false AND ie.statusId = :internmentEpisodeActiveStatus OR s.sectorTypeId = "+SectorType.EMERGENCY_CARE_ID+") ) "
                + " GROUP BY b, s, cs, so, ct, ag, st, hu, status.isBlocked "
                + " ORDER BY s.id, cs.id, hu.id ";

        List<Object[]> result = entityManager.createQuery(sqlQuery)
                .setParameter("institutionId", institutionId)
                .setParameter("sectorsType", Arrays.asList(sectorsType))
                .setParameter("internmentEpisodeActiveStatus", InternmentEpisodeStatus.ACTIVE_ID)
                .getResultList();

        Map<Bed, BedSummaryVo> bedSummaries = new HashMap<>();
        result.forEach(
                r -> {
                    Bed bed = ((Bed) r[0]);
					ClinicalSpecialty clinicalSpecialty = (ClinicalSpecialty) r[3];
					HierarchicalUnit hierarchicalUnit = ((HierarchicalUnit) r[8]);
					Boolean isBlocked = (Boolean) r[9];
					if (bedSummaries.containsKey(bed)) {
						if (hierarchicalUnit != null)
							bedSummaries.get(bed).addHierarchicalUnit(new HierarchicalUnitVo(hierarchicalUnit));
						if (clinicalSpecialty != null)
							bedSummaries.get(bed).addSpecialty(new ClinicalSpecialtyVo( (ClinicalSpecialty) r[3]));
                    } else {
                        String careType = (String) r[4];
                        String sectorOrganization = (String) r[5];
                        String ageGroup = (String) r[6];
                        BedSummaryVo bedSummary = new BedSummaryVo(bed, (Sector) r[1], (LocalDateTime) r[2],
                                careType, sectorOrganization, ageGroup, (SectorType) r[7], isBlocked);
                        if (clinicalSpecialty != null)
                            bedSummary.addSpecialty(new ClinicalSpecialtyVo(clinicalSpecialty));
						if (hierarchicalUnit != null)
							bedSummary.addHierarchicalUnit(new HierarchicalUnitVo(hierarchicalUnit));
                        bedSummaries.put(bed, bedSummary);
                    }
                }
        );

        return new ArrayList<>(bedSummaries.values());
    }
}
