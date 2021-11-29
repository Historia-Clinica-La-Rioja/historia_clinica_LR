package net.pladema.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleBo {

   private short roleId;

   private String roleDescription;

   private Integer userId;

   private Integer institutionId;
}