package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ports;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ResponsibleDoctorVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
public class DocumentAuthorPortImpl implements DocumentAuthorPort {

    private final Logger logger;

    private final EntityManager entityManager;

    public DocumentAuthorPortImpl(EntityManager entityManager) {
        logger = LoggerFactory.getLogger(this.getClass());
        this.entityManager = entityManager;
    }

    @Override
    public ResponsibleDoctorVo getAuthor(Long documentId) {
        logger.debug("Get author from document {} -> documentId {}", documentId);

        String sqlString = "" +
                "SELECT hp.id, p.first_name, p.last_name, hp.license_number " +
                "FROM document AS d " +
                "JOIN users AS u ON (d.created_by = u.id) " +
                "JOIN healthcare_professional hp ON (u.person_id = hp.person_id) " +
                "JOIN person p ON (hp.person_id = p.id) " +
                "WHERE d.id = :documentId" +
                "";
        Query query = entityManager.createNativeQuery(sqlString);

        query.setParameter("documentId", documentId);

        List<Object[]> rows = query.getResultList();
        if (rows.isEmpty())
            return null;
        Object[] row = rows.get(0);
        ResponsibleDoctorVo result = new ResponsibleDoctorVo((Integer) row[0], (String) row[1], (String) row[2], (String) row[3]);
        logger.trace("execute result query -> {}", result);
        return result;
    }
}
