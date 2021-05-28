package net.pladema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@DataJpaTest(showSql = false)
@EnableJpaRepositories(basePackages = {"net.pladema", "ar.lamansys.sgh.clinichistory"})
@EntityScan(basePackages = {"net.pladema", "ar.lamansys.sgh.clinichistory"})
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
