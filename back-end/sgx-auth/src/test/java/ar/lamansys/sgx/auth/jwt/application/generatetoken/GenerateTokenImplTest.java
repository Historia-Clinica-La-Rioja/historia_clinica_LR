package ar.lamansys.sgx.auth.jwt.application.generatetoken;

import ar.lamansys.sgx.auth.jwt.domain.token.ETokenType;
import ar.lamansys.sgx.auth.jwt.infrastructure.output.token.TokenUtils;
import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;
import ar.lamansys.sgx.auth.jwt.domain.token.TokenData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Optional;

import static ar.lamansys.sgx.auth.SgxAsserts.assertPresent;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class  GenerateTokenImplTest {

    private GenerateToken generateToken;

    private String secret;

    @BeforeEach
    public void setUp() {
        secret = "shhh";
        generateToken = new GenerateTokenImpl(secret, Duration.ofHours(1), Duration.ofHours(3));
    }

    @Test
    @DisplayName("Generate token success")
    void generateTokenSuccess() {
        JWTokenBo tokens = generateToken.generateTokens(5, "adminUser");
        Optional<TokenData> tokenDataOptional = TokenUtils.parseToken(tokens.token, secret, ETokenType.NORMAL);
        TokenData tokenData = assertPresent(tokenDataOptional);
        assertAll("token",
                () -> assertEquals("adminUser", tokenData.username),
                () -> assertEquals(5, tokenData.userId)
        );
    }


}