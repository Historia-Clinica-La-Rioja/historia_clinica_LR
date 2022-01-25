package ar.lamansys.sgh.clinichistory.application.fetchDocumentFile;

import ar.lamansys.sgh.clinichistory.application.fetchdocumentfile.FetchDocumentFileById;
import ar.lamansys.sgh.clinichistory.application.fetchdocumentfile.exceptions.FetchDocumentFileException;
import ar.lamansys.sgh.clinichistory.application.ports.DocumentFileStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchDocumentFileByIdTest {

    private FetchDocumentFileById fetchDocumentFileById;

    @Mock
    private DocumentFileStorage documentFileStorage;

    @BeforeEach
    void setUp(){
        fetchDocumentFileById = new FetchDocumentFileById(documentFileStorage);
    }

    @Test
    void unknownDocument(){
        when(documentFileStorage.findById(any())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(FetchDocumentFileException.class, () ->
                fetchDocumentFileById.run(1L));
        assertEquals("No existe un archivo para el documento con id 1", exception.getMessage());

    }
}