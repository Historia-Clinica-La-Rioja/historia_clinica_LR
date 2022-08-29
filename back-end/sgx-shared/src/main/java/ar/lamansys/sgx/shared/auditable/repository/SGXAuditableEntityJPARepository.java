package ar.lamansys.sgx.shared.auditable.repository;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
public interface SGXAuditableEntityJPARepository<T extends SGXAuditableEntity<ID>, ID> extends JpaRepository<T, ID> {


    @Override
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE #{#entityName} e  "
            + "SET e.deleteable.deleted = true "
            + ", e.deleteable.deletedOn = CURRENT_TIMESTAMP "
            + ", e.deleteable.deletedBy = ?#{ principal.userId } "
            + "WHERE e.id = :id ")
    void deleteById(@Param("id") ID var1);


    @Override
    @Transactional
    default void delete(T entity) {
        deleteById(entity.getId());
    }

    @Override
    @Transactional
    default void deleteAll(Iterable<? extends T> entities) {
        entities.forEach(entitiy -> deleteById(entitiy.getId()));
    }

    @Override
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE #{#entityName} e  "
            + "SET e.deleteable.deleted = true "
            + ", e.deleteable.deletedBy = ?#{ principal.userId } "
            + ", e.deleteable.deletedOn = CURRENT_TIMESTAMP ")
    void deleteAll();

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE #{#entityName} e  "
			+ "SET e.deleteable.deleted = false "
			+ ", e.deleteable.deletedOn = null "
			+ ", e.deleteable.deletedBy = null "
			+ ", e.updateable.updatedOn = CURRENT_TIMESTAMP "
			+ ", e.updateable.updatedBy = ?#{ principal.userId } "
			+ "WHERE e.id = :id ")
	void reactivate(@Param("id") ID var1);

	default T reactivate(T entity) {
		this.reactivate(entity.getId());
		return findById(entity.getId()).orElse(null);
	}
}
