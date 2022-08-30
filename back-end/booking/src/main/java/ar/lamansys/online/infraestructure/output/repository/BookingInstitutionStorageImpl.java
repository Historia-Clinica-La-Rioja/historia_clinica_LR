package ar.lamansys.online.infraestructure.output.repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import ar.lamansys.online.application.integration.BookingInstitutionStorage;
import ar.lamansys.online.domain.integration.BookingInstitutionBo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BookingInstitutionStorageImpl implements BookingInstitutionStorage {

    private final EntityManager entityManager;

    public BookingInstitutionStorageImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<BookingInstitutionBo> findBookingInstitutions() {
        log.debug("Find booking institutions");
        String sqlString = "" +
                "SELECT bi.institution_id AS id, " +
                "i.name AS description " +
                "FROM booking_institution bi " +
                "JOIN institution i ON (i.id = bi.institution_id)";

        List<Object[]> rows = entityManager.createNativeQuery(sqlString).getResultList();
        return createInstitutionBoList(rows);
    }

    private List<BookingInstitutionBo> createInstitutionBoList(List<Object[]> rows) {
        if (rows.isEmpty())
            return Collections.emptyList();
        return rows.stream().map(row ->
                new BookingInstitutionBo((Integer) row[0], (String) row[1])
        ).collect(Collectors.toList());
    }
}
