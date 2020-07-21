package net.pladema.medicalconsultation.diary.repository;

import net.pladema.medicalconsultation.diary.repository.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer> {
}
