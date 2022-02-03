package net.pladema.person.controller;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicDataPersonDtoTest {

    @Test
    void test_completeName_success(){
        BasicDataPersonDto basicDataPersonDto = new BasicDataPersonDto();

        basicDataPersonDto.setFirstName("Juan");
        basicDataPersonDto.setMiddleNames("José");
        basicDataPersonDto.setLastName("López");
        basicDataPersonDto.setOtherLastNames("Arriaga");

        Assertions.assertThat(basicDataPersonDto.completeName()).isEqualTo("López Arriaga, Juan José");

        basicDataPersonDto.setMiddleNames(null);
        Assertions.assertThat(basicDataPersonDto.completeName()).isEqualTo("López Arriaga, Juan");

        basicDataPersonDto.setLastName(null);
        Assertions.assertThat(basicDataPersonDto.completeName()).isEqualTo("Arriaga, Juan");

        basicDataPersonDto.setOtherLastNames(null);
        Assertions.assertThat(basicDataPersonDto.completeName()).isEqualTo("Juan");


        basicDataPersonDto.setFirstName(null);
        Assertions.assertThat(basicDataPersonDto.completeName()).isNull();

        basicDataPersonDto.setLastName("López");
        Assertions.assertThat(basicDataPersonDto.completeName()).isEqualTo("López");

        basicDataPersonDto.setOtherLastNames("Arriaga");
        Assertions.assertThat(basicDataPersonDto.completeName()).isEqualTo("López Arriaga");


        basicDataPersonDto.setFirstName("Juan");
        basicDataPersonDto.setMiddleNames("José");
        basicDataPersonDto.setLastName(null);
        basicDataPersonDto.setOtherLastNames(null);
        Assertions.assertThat(basicDataPersonDto.completeName()).isEqualTo("Juan José");

    }
}
