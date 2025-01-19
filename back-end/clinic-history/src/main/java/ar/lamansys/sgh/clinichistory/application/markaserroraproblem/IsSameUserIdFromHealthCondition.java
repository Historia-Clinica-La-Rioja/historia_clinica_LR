package ar.lamansys.sgh.clinichistory.application.markaserroraproblem;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentHealthConditionRepository;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class IsSameUserIdFromHealthCondition {

    private final DocumentHealthConditionRepository documentHealthConditionRepository;

    public Boolean run(Integer problemId) {
        return documentHealthConditionRepository.getUserId(problemId)
                .filter(userIdFromHealthCondition -> UserInfo.getCurrentAuditor().equals(userIdFromHealthCondition))
                .isPresent();
    }
}
