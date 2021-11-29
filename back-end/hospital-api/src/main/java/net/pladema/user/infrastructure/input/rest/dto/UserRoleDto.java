package net.pladema.user.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDto {

   private short roleId;

   private String roleDescription;

   private Integer userId;

   private Integer institutionId;
}