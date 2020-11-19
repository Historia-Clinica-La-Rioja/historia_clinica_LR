package net.pladema.clinichistory.hospitalization.service.documents.anamnesis.impl;

import net.pladema.clinichistory.documents.service.CreateDocumentFile;
import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;
import net.pladema.clinichistory.hospitalization.service.anamnesis.impl.CreateAnamnesisServiceImpl;
import net.pladema.sgx.pdf.PDFDocumentException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CreateAnamnesisServiceImplTest {

	private static final String TOKEN = "TOKEN";

	private CreateAnamnesisServiceImpl createAnamnesisServiceImpl;

	@MockBean
	private DocumentFactory documentFactory;

	@MockBean
	private InternmentEpisodeService internmentEpisodeService;

	@MockBean
	private CreateDocumentFile createDocumentFile;

	@Before
	public void setUp() {
		createAnamnesisServiceImpl = new CreateAnamnesisServiceImpl(documentFactory, internmentEpisodeService, createDocumentFile);

		//1. no crearlo asi, sino pedirle a spring el proxy
		//2. inyectarle un servicio que haya la validacion -> volver al "validation service"
		//3. testear que tenga una anotacion
		/*
		 queremos:
		 * testear logica y validaciones en conjunto
		 * chequear todas las validaciones posibles de una sola vez (no fallar a la primera)
		 * que las validaciones no hagan multiples get para validar diferentes cosas

		row = ...;


		(new AnamnesisValidator(serv1, serv2, serv3)).validate(hid, row);
		(new PatientValidator()).validate(pid);


---------------------------------------------------
		data.put("h", h);
		data.put("p", p);

		validations.validate(data); --> throw con lista de subexcepcion

		*/

	}



	@Test(expected = EntityNotFoundException.class)
	public void createDocument_withEpisodeThatNotExists() throws IOException, PDFDocumentException {
		var anamnesis = new AnamnesisBo();

		createAnamnesisServiceImpl.createDocument(8, anamnesis);
	}

	@Test(expected = ConstraintViolationException.class)
	public void createDocument_withAnamnesisAlreadyDone() throws IOException, PDFDocumentException {
		var anamnesis = new AnamnesisBo();
		anamnesis.setEncounterId(15);
		when(internmentEpisodeService.getPatient(any())).thenReturn(Optional.of(5));

		createAnamnesisServiceImpl.createDocument(8, anamnesis);
	}

}
