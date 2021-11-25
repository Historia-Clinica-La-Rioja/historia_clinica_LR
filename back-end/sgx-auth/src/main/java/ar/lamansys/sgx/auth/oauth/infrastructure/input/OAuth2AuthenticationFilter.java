package ar.lamansys.sgx.auth.oauth.infrastructure.input;

import ar.lamansys.sgx.auth.jwt.infrastructure.input.service.AuthenticationExternalService;
import ar.lamansys.sgx.auth.oauth.application.FetchUserInfo;
import ar.lamansys.sgx.auth.oauth.application.LoadUserAuthentication;
import ar.lamansys.sgx.auth.oauth.domain.OAuthUserInfoBo;
import ar.lamansys.sgx.auth.user.infrastructure.input.service.UserExternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFilter extends OncePerRequestFilter {

    private final FetchUserInfo fetchUserInfo;
    private final LoadUserAuthentication loadUserAuthentication;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = httpServletRequest.getHeader("Authorization");
        Optional<OAuthUserInfoBo> opUserInfo = fetchUserInfo.run(accessToken);

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
