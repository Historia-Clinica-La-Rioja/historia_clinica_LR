package ar.lamansys.sgx.auth.oauth.infrastructure.output.mapper;

import ar.lamansys.sgx.auth.oauth.domain.OAuthUserInfoBo;
import ar.lamansys.sgx.auth.oauth.infrastructure.output.dto.OAuthUserInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface OAuthUserMapper {

    @Named("fromOAuthUserInfoDto")
    @Mapping(source = "preferred_username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "given_name", target = "firstName")
    @Mapping(source = "family_name", target = "lastName")
    OAuthUserInfoBo fromOAuthUserInfoDto(OAuthUserInfoDto oAuthUserInfoDto);

}
