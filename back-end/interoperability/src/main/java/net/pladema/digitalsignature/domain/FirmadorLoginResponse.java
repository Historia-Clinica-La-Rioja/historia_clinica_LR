package net.pladema.digitalsignature.domain;

import ar.lamansys.sgx.shared.restclient.services.domain.LoginResponse;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FirmadorLoginResponse implements LoginResponse {

    private String token;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public FirmadorLoginResponse(@JsonProperty("access_token") String token) {
        this.token = token;
    }
}
