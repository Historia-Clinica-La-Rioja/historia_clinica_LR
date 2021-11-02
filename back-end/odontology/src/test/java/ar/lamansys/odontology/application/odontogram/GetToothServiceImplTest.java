package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.application.odontogram.exception.ToothNotFoundException;
import ar.lamansys.odontology.domain.ToothStorage;
import ar.lamansys.odontology.infrastructure.repository.ToothStorageMockImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetToothServiceImplTest {
    private GetToothService getToothService;

    @BeforeEach
    public void setUp() {
        ToothStorage toothStorage = new ToothStorageMockImpl();

        getToothService = new GetToothServiceImpl(toothStorage);
    }

    @Test
    public void toothDoesNotExist() {
        String nonExistentToothId = "IdDoesNotExist";
        Exception exception = Assertions.assertThrows(ToothNotFoundException.class, () ->
                getToothService.run(nonExistentToothId));
        String expectedMessage = "El diente con id " + nonExistentToothId + " no existe";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

    }
}