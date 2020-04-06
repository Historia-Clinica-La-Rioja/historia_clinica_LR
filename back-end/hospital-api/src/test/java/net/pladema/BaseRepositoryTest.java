package net.pladema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

public class BaseRepositoryTest {
	
    @Autowired
    private TestEntityManager entityManager;
    
    protected <E> E save(E entity) {
        E saved = entityManager.persist(entity);
        entityManager.flush();
        return saved;
    }
    
}
