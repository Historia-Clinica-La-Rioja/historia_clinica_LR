package ar.lamansys.sgh.clinichistory.application.markaserroraproblem;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentHealthConditionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class IsWithinExpirationTimeLimit {

    private final DocumentHealthConditionRepository documentHealthConditionRepository;

    @Value("${app.problems.set-incorrect.time-window:24h}")
    private Duration timeWindow;

    public Boolean run(Integer problemId) {
        return documentHealthConditionRepository.getCreatedOn(problemId)
                .filter(healthConditionTime -> LocalDateTime.now().isBefore(healthConditionTime.plus(timeWindow)))
                .isPresent();
    }
}
