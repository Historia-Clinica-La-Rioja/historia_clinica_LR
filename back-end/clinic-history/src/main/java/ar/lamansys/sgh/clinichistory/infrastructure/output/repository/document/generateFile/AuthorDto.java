package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class AuthorDto implements Serializable {

    private Integer id;

    private String firstName;

    private String lastName;

    private String licence;

	private String nameSelfDetermination;
}
