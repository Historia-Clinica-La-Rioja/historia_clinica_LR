package ar.lamansys.sgh.publicapi.userinformation.infrastructure.output;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FetchUserPersonFromTokenRepository extends JpaRepository<UserPersonData, Integer> {
}
