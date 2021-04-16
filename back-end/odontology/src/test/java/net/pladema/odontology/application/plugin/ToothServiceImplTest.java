package net.pladema.odontology.application.plugin;

import net.pladema.odontology.application.plugin.exception.ToothServiceException;
import net.pladema.odontology.domain.TeethBo;
import net.pladema.odontology.domain.ToothStorage;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ToothServiceImplTest {

    private ToothService toothService;

    @MockBean
    private ToothStorage toothStorage;

    @Before
    public void setUp(){
    }

    @Test
    public void odontololy_info_success() throws ToothServiceException {
        toothService = new ToothServiceImpl(toothStorage);
        String testString = "Test property has to match";
        when(toothStorage.getTeeth()).thenReturn(new TeethBo(testString));
        Assertions.assertThat(toothService.getTeeth().getInfo())
                .isEqualTo(testString);
    }

    @Test
    public void odontololy_info_error() {
        toothService = new ToothServiceImpl(new ToothStorageErrorMockImpl());
        Exception exception = assertThrows(ToothServiceException.class, () ->
                toothService.getTeeth()
        );
        String expectedMessage = "Fallo algo";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNotNull().isEqualTo(expectedMessage);
    }
}
