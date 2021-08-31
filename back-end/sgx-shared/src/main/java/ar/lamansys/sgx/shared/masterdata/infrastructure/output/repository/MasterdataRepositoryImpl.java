package ar.lamansys.sgx.shared.masterdata.infrastructure.output.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Arrays;
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
    public <T> Collection<MasterDataProjection> findAllProjectedBy(Class<T> clazz, String...filterIds) {

        String sqlString = "SELECT p FROM " + clazz.getSimpleName() + " p ";

        if(filterIds.length > 0) {
            sqlString += "WHERE id IN :filterIds ";
            Query query = entityManager.createQuery(sqlString, clazz);
            return query.setParameter("filterIds", Arrays.asList(filterIds)).getResultList();
        }

        Query query = entityManager.createQuery(sqlString, clazz);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public <T> Collection<MasterDataProjection> findAllRestrictedBy(Class<T> clazz, String field, Short flag) {
        String sqlString = "SELECT p FROM " + clazz.getSimpleName() + " p " + "WHERE " + field + "= " + flag;
        Query query = entityManager.createQuery(sqlString, clazz);
        return query.getResultList();
    }
}
