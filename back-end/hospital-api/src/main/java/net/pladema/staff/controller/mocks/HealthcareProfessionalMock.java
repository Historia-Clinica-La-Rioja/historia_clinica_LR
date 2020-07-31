package net.pladema.staff.controller.mocks;
import net.pladema.staff.controller.dto.ProfessionalDto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class HealthcareProfessionalMock {

    private HealthcareProfessionalMock(){
        super();
    }

    public static List<ProfessionalDto> mockListProfessionalDto(String name){
        return IntStream.range(0,5)
                .parallel()
                .mapToObj(v -> mockProfessionalDto(v))
                .collect(Collectors.toList());
    }

    public static ProfessionalDto mockProfessionalDto(int id) {
        ProfessionalDto result = new ProfessionalDto();
        result.setId(id);
        result.setLicenceNumber("19215/"+id);
        result.setFirstName("Ricardo " + id);
        result.setLastName("Gutierrez " + id);
        result.setIdentificationNumber("12345678");
        return result;
    }

}
