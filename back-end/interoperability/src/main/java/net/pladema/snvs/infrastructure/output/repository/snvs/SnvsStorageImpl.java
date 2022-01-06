package net.pladema.snvs.infrastructure.output.repository.snvs;

import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.repository.EEnvironment;
import net.pladema.snvs.application.ports.event.SnvsStorage;
import net.pladema.snvs.domain.event.SnvsEventInfoBo;
import net.pladema.snvs.domain.event.exceptions.SnvsEventInfoBoException;
import net.pladema.snvs.domain.problem.SnvsProblemBo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SnvsStorageImpl implements SnvsStorage {

    private final EntityManager entityManager;

    private final String environment;

    public SnvsStorageImpl(EntityManager entityManager, @Value("${ws.sisa.snvs.environment}") String environment) {
        this.entityManager = entityManager;
        this.environment = environment;
    }

    @Override
    public Optional<SnvsEventInfoBo> fetchSnvsEventInfo(SnvsProblemBo problemBo, Integer manualClassificationId) throws SnvsEventInfoBoException {
        log.debug("SnvsStorageImpl fetchSnvsEventInfo from -> problemBo {}, manualClassificationId {}", problemBo, manualClassificationId);
        if (problemBo == null || manualClassificationId == null)
            return Optional.empty();
        Query query = entityManager.createQuery(
                "SELECT  snvsg.eventId, snvsg.groupEventId, snvsg.manualClassificationId, snvsg.environment " +
                        "FROM Snomed s " +
                        "JOIN SnomedRelatedGroup srg ON (srg.snomedId = s.id) " +
                        "JOIN SnomedGroup sg ON (srg.groupId = sg.id) " +
                        "JOIN SnvsGroup snvsg ON (snvsg.groupId = sg.id) " +
                        "WHERE s.sctid = :sctid " +
                        "AND s.pt = :pt " +
                        "AND snvsg.manualClassificationId = :manualClassificationId " +
                        "AND snvsg.environment = :environment " );
        query.setParameter("sctid", problemBo.getSctid());
        query.setParameter("pt", problemBo.getPt());
        query.setParameter("manualClassificationId", manualClassificationId);
        query.setParameter("environment", EEnvironment.valueOf(environment));
        List<Object[]> queryResult = query.getResultList();
        Object[] row = queryResult.get(0);
        log.debug("SnvsStorageImpl fetchSnvsEventInfo from -> problemBo {}, manualClassificationId {}", problemBo, manualClassificationId);
        return Optional.of(new SnvsEventInfoBo((Integer)row[0], (Integer)row[1], (Integer) row[2],(Integer) row[3]));
    }
}
