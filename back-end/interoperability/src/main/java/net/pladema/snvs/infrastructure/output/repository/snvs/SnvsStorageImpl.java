package net.pladema.snvs.infrastructure.output.repository.snvs;

import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.services.domain.ManualClassificationBo;
import net.pladema.snvs.application.ports.event.SnvsStorage;
import net.pladema.snvs.domain.event.SnvsEventBo;
import net.pladema.snvs.domain.event.SnvsEventInfoBo;
import net.pladema.snvs.domain.event.SnvsEventManualClassificationsBo;
import net.pladema.snvs.domain.event.exceptions.SnvsEventInfoBoException;
import net.pladema.snvs.domain.problem.SnvsProblemBo;
import net.pladema.snvs.infrastructure.output.repository.snvs.entity.EEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SnvsStorageImpl implements SnvsStorage {

    private final EntityManager entityManager;

    private final String environment;

    public SnvsStorageImpl(EntityManager entityManager,
                           @Value("${ws.sisa.snvs.environment}") String environment) {
        this.entityManager = entityManager;
        this.environment = environment;
    }

    @Override
    public Optional<SnvsEventInfoBo> fetchSnvsEventInfo(SnvsProblemBo problemBo, Integer manualClassificationId, Integer groupEventId, Integer eventId) throws SnvsEventInfoBoException {
        log.debug("SnvsStorageImpl fetchSnvsEventInfo from -> problemBo {}, manualClassificationId {}, groupEventId {}, eventId {}", problemBo, manualClassificationId, groupEventId, eventId);
        if (problemBo == null || manualClassificationId == null)
            return Optional.empty();
        Query query = entityManager.createQuery(
                "SELECT  snvsg.eventId, snvsg.groupEventId, snvsg.manualClassificationId, snvsg.environment " +
                        "FROM Snomed s " +
                        "JOIN SnomedRelatedGroup srg ON (srg.snomedId = s.id) " +
                        "JOIN SnvsGroup snvsg ON (snvsg.groupId = srg.groupId) " +
                        "WHERE s.sctid = :sctid " +
                        "AND s.pt = :pt " +
                        "AND snvsg.manualClassificationId = :manualClassificationId " +
                        "AND snvsg.environment = :environment " +
                        "AND snvsg.eventId = :eventId " +
                        "AND snvsg.groupEventId = :groupEventId " );
        query.setParameter("sctid", problemBo.getSctid());
        query.setParameter("pt", problemBo.getPt());
        query.setParameter("manualClassificationId", manualClassificationId);
        query.setParameter("eventId", eventId);
        query.setParameter("groupEventId", groupEventId);
        query.setParameter("environment", EEnvironment.map(environment).getId().intValue());
        List<Object[]> queryResult = query.getResultList();
        Object[] row = queryResult.get(0);
        log.debug("SnvsStorageImpl fetchSnvsEventInfo from -> problemBo {}, manualClassificationId {}, groupEventId {}, eventId {}", problemBo, manualClassificationId, groupEventId, eventId);
        return Optional.of(new SnvsEventInfoBo((Integer)row[0], (Integer)row[1], (Integer) row[2],(Integer) row[3]));
    }

    @Override
    public List<SnvsEventManualClassificationsBo> fetchManualClassification(SnvsProblemBo problemBo) {
        log.debug("SnvsStorageImpl fetchManualClassification from -> problemBo {}", problemBo);
        if (problemBo == null)
            return new ArrayList<>();
        Query query = entityManager.createQuery(
                "SELECT DISTINCT sg.description, ng.eventId, ng.groupEventId, ng.environment, mc.id, mc.description " +
                        "FROM Snomed s " +
                        "JOIN SnomedRelatedGroup srg ON (s.id = srg.snomedId) " +
                        "JOIN SnomedGroup sg ON (sg.id = srg.groupId) " +
                        "JOIN SnvsGroup ng ON (ng.groupId = srg.groupId) " +
                        "JOIN ManualClassification mc ON (mc.id = ng.manualClassificationId) " +
                        "WHERE sctid = :sctid " +
                        "AND pt = :pt " +
                        "AND ng.environment = :environment " );
        query.setParameter("sctid", problemBo.getSctid());
        query.setParameter("pt", problemBo.getPt());
        query.setParameter("environment", EEnvironment.map(environment).getId().intValue());


        List<Object[]> queryResult = query.getResultList();
        return queryResult.stream()
                .map(this::buildSnvsEventManualClassification)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(SnvsEventManualClassificationsBo::getSnvsEventBo))
                .values().stream()
                .map(this::buildResult)
                .collect(Collectors.toList());
    }

    private SnvsEventManualClassificationsBo buildResult(List<SnvsEventManualClassificationsBo> snvsEventManualClassificationsBos) {
        var result = snvsEventManualClassificationsBos.get(0);
        if (snvsEventManualClassificationsBos.size() == 1)
            return result;
        for (int i = 1; i < snvsEventManualClassificationsBos.size(); i++)
            result.joinManualClassifications(snvsEventManualClassificationsBos.get(i).getManualClassifications());
        return result;
    }

    private SnvsEventManualClassificationsBo buildSnvsEventManualClassification(Object[] row){
        try {
            SnvsEventBo event = new SnvsEventBo((String)row[0], (Integer)row[1], (Integer)row[2], (Integer) row[3]);
            return new SnvsEventManualClassificationsBo(event, new ArrayList<>(Arrays.asList(new ManualClassificationBo((Integer) row[4], (String) row[5]))));
        } catch (SnvsEventInfoBoException e) {
            return null;
        }
    }


}
