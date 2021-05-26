package ar.lamansys.odontology.infrastructure.repository.tooth;

import ar.lamansys.odontology.domain.ToothBo;
import ar.lamansys.odontology.domain.ToothStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ToothStorageMockImplTest {

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void odontologyTeethAmountSuccess() {
        ToothStorage toothStorage = new ToothStorageMockImpl();
        List<ToothBo> resultService = toothStorage.getAll();
        assertThat(resultService.size()).isEqualTo(52);
    }

}