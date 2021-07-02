package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileDto {
    private Integer fileId;
    private String fileName;
}
