package net.pladema.establishment.repository;

import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisodeStatus;
import net.pladema.establishment.repository.domain.BedSummaryVo;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.repository.entity.BedCategory;
import net.pladema.establishment.repository.entity.Sector;
import net.pladema.establishment.repository.entity.SectorType;
import net.pladema.staff.repository.domain.ClinicalSpecialtyVo;
import net.pladema.staff.repository.entity.ClinicalSpecialty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public List<BedSummaryVo> execute(Integer institutionId) {
        String sqlQuery =
                " SELECT b, bc, s, MAX(ie.probableDischargeDate), cs, ct.description, so.description, ag.description "
                + " FROM Bed b "
                + " JOIN BedCategory bc ON b.bedCategoryId = bc.id "
                + " JOIN Room r ON b.roomId = r.id "
                + " JOIN Sector s ON r.sectorId = s.id "
                + " LEFT JOIN CareType ct ON s.careTypeId = ct.id "
                + " LEFT JOIN SectorOrganization so ON s.sectorOrganizationId = so.id "
                + " LEFT JOIN AgeGroup ag ON s.ageGroupId = ag.id "
                + " LEFT JOIN ClinicalSpecialtySector css ON s.id = css.sectorId "
                + " LEFT JOIN ClinicalSpecialty cs ON css.clinicalSpecialtyId = cs.id "
                + " LEFT JOIN InternmentEpisode ie ON b.id = ie.bedId "
                + " WHERE s.institutionId = :institutionId "
                + " AND (s.sectorTypeId = :internmentSectorType OR s.sectorTypeId IS NULL) "
                + " AND (b.free=true OR ( b.free=false AND ie.statusId = :internmentEpisodeActiveStatus ) ) "
                + " GROUP BY b, bc, s, cs, so, ct, ag "
                + " ORDER BY s.id, cs.id ";

        List<Object[]> result = entityManager.createQuery(sqlQuery)
                .setParameter("institutionId", institutionId)
                .setParameter("internmentSectorType", SectorType.INTERNMENT_ID)
                .setParameter("internmentEpisodeActiveStatus", InternmentEpisodeStatus.ACTIVE_ID)
                .getResultList();

        Map<Bed, BedSummaryVo> bedSummaries = new HashMap<>();
        result.forEach(
                r -> {
                    Bed bed = ((Bed) r[0]);
                    if (bedSummaries.containsKey(bed)) {
                        bedSummaries.get(bed).addSpecialty(new ClinicalSpecialtyVo( (ClinicalSpecialty) r[4]));
                    } else {
                        String careType = (String) r[5];
                        String sectorOrganization = (String) r[6];
                        String ageGroup = (String) r[7];
                        BedSummaryVo bedSummary = new BedSummaryVo(bed, (BedCategory) r[1], (Sector) r[2], (LocalDateTime) r[3],
                                careType, sectorOrganization, ageGroup);
                        ClinicalSpecialty clinicalSpecialty =  (ClinicalSpecialty) r[4];
                        if (clinicalSpecialty != null)
                            bedSummary.addSpecialty(new ClinicalSpecialtyVo(clinicalSpecialty));
                        bedSummaries.put(bed, bedSummary);
                    }
                }
        );

        return new ArrayList<>(bedSummaries.values());
    }
}
