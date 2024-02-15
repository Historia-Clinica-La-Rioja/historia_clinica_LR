package net.pladema.template.infrastructure.output.repository;

import net.pladema.template.infrastructure.output.repository.entity.DocumentTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentTemplateRepository extends JpaRepository<DocumentTemplate, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT 1 " +
            "FROM DocumentTemplate AS dt " +
            "WHERE dt.userId = :userId " +
            "AND dt.typeId = :typeId " +
            "AND LOWER(dt.name) LIKE LOWER(:name) " +
            "AND dt.deleted = false")
    Optional<Integer> exists(@Param("userId") Integer userId,
                             @Param("name") String name,
                             @Param("typeId") Short typeId);

    @Transactional(readOnly = true)
    @Query("SELECT dt " +
            "FROM DocumentTemplate AS dt " +
            "WHERE dt.userId = :userId " +
            "AND dt.typeId = :typeId " +
            "AND dt.deleted = false")
    List<DocumentTemplate> getTemplates(@Param("userId") Integer userId, @Param("typeId") Short typeId);

    @Transactional(readOnly = true)
    @Query("SELECT dt.fileId " +
            "FROM DocumentTemplate AS dt " +
            "WHERE dt.id = :id " +
            "AND dt.deleted = false")
    Optional<Long> getFileId(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE DocumentTemplate dt " +
            "SET dt.deleted = true " +
            "WHERE dt.id = :id")
    void deleteById(@Param("id") Long id);

}