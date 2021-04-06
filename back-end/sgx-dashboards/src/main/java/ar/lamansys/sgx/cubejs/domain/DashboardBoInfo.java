package ar.lamansys.sgx.cubejs.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;


@Getter
@AllArgsConstructor
public class DashboardBoInfo {

    private ResponseEntity<String> response;

}
