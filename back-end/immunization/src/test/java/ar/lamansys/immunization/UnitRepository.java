package ar.lamansys.immunization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest(showSql = false)
public class UnitRepository {
	
    @Autowired
    private TestEntityManager entityManager;

    protected <E> E save(E entity) {
        E saved = entityManager.persist(entity);
        entityManager.flush();
        return saved;
    }

    protected <E> E merge(E entity){
        return entityManager.merge(entity);
    }

    protected void flush() {
        entityManager.flush();
    }
}
