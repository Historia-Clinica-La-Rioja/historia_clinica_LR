package net.pladema.odontology;

import net.pladema.odontology.repository.OdontologyRepository;
import net.pladema.odontology.service.OdontologyService;
import net.pladema.odontology.service.impl.OdontologyServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class OdontologyServiceImplTest {

    private OdontologyService odontologyService;

    @MockBean
    private OdontologyRepository odontologyRepository;

    @Before
    public void setUp(){
        odontologyService = new OdontologyServiceImpl(odontologyRepository);
    }

    @Test
    public void odontololy_info_success(){
        String testString = "Test property has to match";
        when(odontologyRepository.getInfo()).thenReturn(testString);
        Assertions.assertThat(odontologyService.getInfo()).isEqualTo(testString);
    }
}
