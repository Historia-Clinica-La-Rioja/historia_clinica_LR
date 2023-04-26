package ar.lamansys.pac.application.parsetoken;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ar.lamansys.pac.application.exception.PacException;
import ar.lamansys.pac.application.exception.PacExceptionEnum;
import ar.lamansys.pac.domain.StudyPermissionDataBo;
import ar.lamansys.pac.domain.jwt.JWTUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ParseToken {

	@Value("${token.secret}")
	private String secret;

	public StudyPermissionDataBo run(String token) throws PacException {
		return JWTUtils.parseClaims(token, secret)
				.map(claims -> {
					if (!claims.containsKey("studyInstanceUID"))
						throw new PacException(PacExceptionEnum.MALFORMED, PacExceptionEnum.MALFORMED.getMessage());
					String studyUID = (String) claims.get("studyInstanceUID");
					return new StudyPermissionDataBo(studyUID);
				})
				.orElseThrow(() -> new PacException(PacExceptionEnum.UNAUTHORIZED, PacExceptionEnum.UNAUTHORIZED.getMessage()));
	}
}
