package net.pladema.emergencycare.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.emergencycare.application.port.output.GetUserCompleteNameByUserIdPort;
import net.pladema.user.application.getusercompletename.GetUserCompleteName;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetUserCompleteNameByUserIdImplByUserId implements GetUserCompleteNameByUserIdPort {

    private final GetUserCompleteName getUserCompleteName;

    @Override
    public String run(Integer userId) {
        return getUserCompleteName.run(userId);
    }

}
