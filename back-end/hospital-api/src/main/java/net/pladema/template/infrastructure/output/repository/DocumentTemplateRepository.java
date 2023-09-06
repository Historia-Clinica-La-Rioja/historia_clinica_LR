package net.pladema.template.infrastructure.output.repository;

import net.pladema.template.infrastructure.output.repository.entity.DocumentTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTemplateRepository extends JpaRepository<DocumentTemplate, Integer> {
}
