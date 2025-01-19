package net.pladema.imagenetwork.infrastructure.output.database.repository;

import net.pladema.imagenetwork.infrastructure.output.database.entity.ErrorDownloadStudy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorDownloadStudyRepository extends JpaRepository<ErrorDownloadStudy, Integer> {

}
