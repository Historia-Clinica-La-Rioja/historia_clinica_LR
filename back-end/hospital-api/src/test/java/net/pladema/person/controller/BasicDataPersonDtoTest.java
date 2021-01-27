package net.pladema.person.controller;

import net.pladema.person.controller.dto.BasicDataPersonDto;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class BasicDataPersonDtoTest {

    @Test
    public void test_completeName_success(){
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
