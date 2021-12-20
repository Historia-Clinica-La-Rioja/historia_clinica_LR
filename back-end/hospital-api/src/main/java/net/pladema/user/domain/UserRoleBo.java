package net.pladema.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleBo {

   private short roleId;

   @Nullable
   private String roleDescription;

   private Integer userId;

   private Integer institutionId;
}