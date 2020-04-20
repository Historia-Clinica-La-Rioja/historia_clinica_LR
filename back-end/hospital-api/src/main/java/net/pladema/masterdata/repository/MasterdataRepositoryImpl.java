package net.pladema.masterdata.repository;

import net.pladema.internation.repository.projections.InternmentMasterDataProjection;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collection;

@Repository
public class MasterdataRepositoryImpl implements MasterdataRepository {

    private final EntityManager entityManager;

    public MasterdataRepositoryImpl(EntityManager entityManager){
        super();
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public <T> Collection<InternmentMasterDataProjection> findAllInternmentProjectedBy(Class<T> clazz) {
        Query query = entityManager.createQuery(
                "SELECT p FROM " + clazz.getSimpleName() + " p ", clazz);
        return query.getResultList();
    }
}
