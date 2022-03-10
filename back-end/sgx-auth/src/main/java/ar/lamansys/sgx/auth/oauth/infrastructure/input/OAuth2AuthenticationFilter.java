package ar.lamansys.sgx.auth.oauth.infrastructure.input;

import ar.lamansys.sgx.auth.oauth.application.FetchUserInfo;
import ar.lamansys.sgx.auth.oauth.application.LoadUserAuthentication;
import ar.lamansys.sgx.auth.oauth.domain.OAuthUserInfoBo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
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

    @Value("${ws.oauth.enabled:false}")
    private boolean filterEnabled;

    private final FetchUserInfo fetchUserInfo;

    private final LoadUserAuthentication loadUserAuthentication;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (this.filterEnabled
                && securityContext.getAuthentication() == null) {
            String accessToken = httpServletRequest.getHeader("Authorization");
            if (accessToken != null) {
                Optional<OAuthUserInfoBo> opUserInfo = fetchUserInfo.run(accessToken);
                opUserInfo.flatMap(loadUserAuthentication::run)
                        .ifPresent(securityContext::setAuthentication);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
