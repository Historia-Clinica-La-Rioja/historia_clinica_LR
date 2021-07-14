package net.pladema.establishment.service.fetchInstitutions;

import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchAllInstitutionsTest {

    private FetchAllInstitutions fetchAllInstitutions;

    @Mock
    private InstitutionRepository institutionRepository;

    @BeforeEach
    void setUp(){
        fetchAllInstitutions = new FetchAllInstitutions(institutionRepository);
    }

    @Test
    void success(){
        when(institutionRepository.findAll()).thenReturn(List.of(mock()));

        var result = fetchAllInstitutions.run();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(4, result.get(0).getId());
        Assertions.assertEquals("institution", result.get(0).getName());
        Assertions.assertEquals("institution.com", result.get(0).getWebsite());

    }

    private Institution mock(){
        Institution result = new Institution();
        result.setId(4);
        result.setName("institution");
        result.setCuit("123123a23a");
        result.setAddressId(4);
        result.setEmail("email");
        result.setWebsite("institution.com");
        result.setPhone("213124323");
        return result;
    }
}