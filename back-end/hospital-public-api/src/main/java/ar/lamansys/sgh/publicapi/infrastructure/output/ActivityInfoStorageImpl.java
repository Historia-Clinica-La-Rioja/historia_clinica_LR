package ar.lamansys.sgh.publicapi.infrastructure.output;

import ar.lamansys.sgh.publicapi.application.port.out.ActivityInfoStorage;
import ar.lamansys.sgh.publicapi.domain.BedRelocationInfoBo;
import ar.lamansys.sgh.publicapi.domain.SnomedBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityInfoStorageImpl implements ActivityInfoStorage {

    private static final Logger LOG = LoggerFactory.getLogger(ActivityInfoStorageImpl.class);

    private final EntityManager entityManager;
    private final AttentionReadsRepository attentionReadsRepository;

    public ActivityInfoStorageImpl(EntityManager entityManager, AttentionReadsRepository attentionReadsRepository) {
        this.entityManager = entityManager;
        this.attentionReadsRepository = attentionReadsRepository;
    }

    @Override
    public void processActivity(String refsetCode, String provinceCode, Long activityId) {
        LOG.debug("update ActivityInfoStorage -> refsetCode {}, provinceCode {}, activityId {}", refsetCode, provinceCode, activityId);
        List<AttentionReads> toUpdate = attentionReadsRepository.findByAttentionId(activityId);
        toUpdate.forEach(ar -> ar.setProcessed(true));
        LOG.trace("Updated -> {}", attentionReadsRepository.saveAll(toUpdate));
    }

    @Override
    public List<SnomedBo> getProceduresByActivity(String refsetCode, String provinceCode, Long activityId) {
        LOG.debug("getBedRelocationsByActivity ActivityInfoStorage -> refsetCode {}, provinceCode {}, activityId {}", refsetCode, provinceCode, activityId);

        String sqlString = "SELECT snm.sctid , snm.pt " +
                "FROM {h-schema}v_attention va " +
                "JOIN {h-schema}institution i ON (i.sisa_code = :refsetCode AND i.province_code = :provinceCode AND va.institution_id = i.id) " +
                "LEFT JOIN (SELECT cs.clinical_specialty_type_id, s.sctid, s.pt " +
                            "FROM {h-schema}clinical_specialty cs " +
                            "JOIN {h-schema}snomed s on s.sctid = cs.sctid_code) snm ON (va.clinical_speciality_id = snm.clinical_specialty_type_id) " +
                "WHERE va.id = :activityId ";

        Query query = entityManager.createNativeQuery(sqlString)
                .setParameter("refsetCode", refsetCode)
                .setParameter("provinceCode", provinceCode)
                .setParameter("activityId", activityId);

        List<Object[]> queryResult = query.getResultList();

        List<SnomedBo> result = queryResult
                .stream()
                .map(snomed -> new SnomedBo((String) snomed[0], (String) snomed[1]))
                .collect(Collectors.toList());

        LOG.trace("Output -> {}", result);
        return result;
    }

    @Override
    public List<SnomedBo> getSuppliesByActivity(String refsetCode, String provinceCode, Long activityId) {
        LOG.debug("getBedRelocationsByActivity ActivityInfoStorage -> refsetCode {}, provinceCode {}, activityId {}", refsetCode, provinceCode, activityId);

        List<SnomedBo> result = new ArrayList<>();

        LOG.trace("Output -> {}", result);
        return result;
    }

    @Override
    public List<BedRelocationInfoBo> getBedRelocationsByActivity(String refsetCode, String provinceCode, Long activityId) {
        LOG.debug("getBedRelocationsByActivity ActivityInfoStorage -> refsetCode {}, provinceCode {}, activityId {}", refsetCode, provinceCode, activityId);

        Integer outpatient = 1;

        String careTypeIdSubQuery = "SELECT s.care_type_id " +
                                    "FROM {h-schema}bed b " +
                                    "JOIN {h-schema}room r ON b.room_id = r.id " +
                                    "JOIN {h-schema}sector s ON r.sector_id = s.id " +
                                    "WHERE b.id = hpbr.destination_bed_id ";

        String sqlString = "SELECT hpbr.relocation_date, snm.sctid, snm.pt, ("+careTypeIdSubQuery+")" +
                "FROM {h-schema}v_attention va " +
                "JOIN {h-schema}historic_patient_bed_relocation hpbr ON (hpbr.internment_episode_id = va.encounter_id) " +
                "JOIN {h-schema}institution i ON (i.sisa_code = :refsetCode AND i.province_code = :provinceCode AND va.institution_id = i.id) " +
                "LEFT JOIN (SELECT cs.clinical_specialty_type_id, s.sctid, s.pt " +
                            "FROM {h-schema}clinical_specialty cs " +
                            "JOIN {h-schema}snomed s on s.sctid = cs.sctid_code) snm ON (va.clinical_speciality_id = snm.clinical_specialty_type_id) " +
                "WHERE va.id = :activityId AND va.scope_id = " + outpatient;

        Query query = entityManager.createNativeQuery(sqlString)
                .setParameter("refsetCode", refsetCode)
                .setParameter("provinceCode", provinceCode)
                .setParameter("activityId", activityId);

        List<Object[]> queryResult = query.getResultList();

        List<BedRelocationInfoBo> result = queryResult
                .stream()
                .map(this::parseToBedRelocationInfoBo)
                .collect(Collectors.toList());

        LOG.trace("Output -> {}", result);
        return result;
    }

    private BedRelocationInfoBo parseToBedRelocationInfoBo(Object[] rawBedRelocation) {
        return new BedRelocationInfoBo(
                (LocalDateTime) rawBedRelocation[0],
                (Integer) rawBedRelocation[3],
                new SnomedBo((String) rawBedRelocation[1], (String) rawBedRelocation[2])
        );
    }
}
