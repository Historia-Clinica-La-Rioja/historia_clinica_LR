package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CounterReferenceProfessionalInfoDto implements Serializable {

    private static final long serialVersionUID = 5383370005741451245L;

    private Integer id;

    private String firstName;

    private String lastName;

}
