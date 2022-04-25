package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import ar.lamansys.sgh.clinichistory.domain.document.AuthorBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentAuthorFinder {

    private final Logger logger;

    private final EntityManager entityManager;

    public DocumentAuthorFinder(EntityManager entityManager) {
        logger = LoggerFactory.getLogger(this.getClass());
        this.entityManager = entityManager;
    }

    public AuthorBo getAuthor(Long documentId) {
        logger.debug("Get author from document -> documentId={}", documentId);

        String sqlString = "" +
                "SELECT hp.id, p.firstName, p.lastName, hp.licenseNumber, pe.nameSelfDetermination" +
                "FROM Document AS d " +
                "JOIN UserPerson AS up ON (d.creationable.createdBy = up.pk.userId) " +
                "JOIN HealthcareProfessional hp ON (up.pk.personId = hp.personId) " +
                "JOIN Person p ON (hp.personId = p.id) " +
				"JOIN PersonExtended pe ON (p.id = pe.id) " +
                "WHERE d.id = :documentId" +
                "";

		List<Object[]> rows = new ArrayList<>();
		Integer searchTries = 0;
        Query query;

		while (rows.isEmpty() && (searchTries < 10)){
			query = entityManager.createQuery(sqlString);
			query.setParameter("documentId", documentId);
			rows = query.getResultList();
			searchTries++;
		}

        if (rows.isEmpty())
            return null;
        Object[] row = rows.get(0);
        AuthorBo result =  new AuthorBo((Integer) row[0], (String) row[1], (String) row[2], (String) row[3], (String) row[4]);
        logger.trace("execute result query -> {}", result);
        return result;
    }
}
