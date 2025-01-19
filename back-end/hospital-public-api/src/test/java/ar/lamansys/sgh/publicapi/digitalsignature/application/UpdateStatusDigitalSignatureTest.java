package ar.lamansys.sgh.publicapi.digitalsignature.application;

import ar.lamansys.sgh.publicapi.digitalsignature.application.port.out.DigitalSignatureCallbackStorage;
import ar.lamansys.sgh.publicapi.digitalsignature.application.port.out.exception.DigitalSignatureCallbackEnumException;
import ar.lamansys.sgh.publicapi.digitalsignature.application.port.out.exception.DigitalSignatureCallbackException;
import ar.lamansys.sgh.publicapi.digitalsignature.application.updatestatusdigitalsignature.UpdateStatusDigitalSignature;
import ar.lamansys.sgh.shared.infrastructure.input.service.digitalsignature.DigitalSignatureCallbackRequestDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.digitalsignature.DigitalSignatureStatusDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.digitalsignature.MultipleDigitalSignatureCallbackRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UpdateStatusDigitalSignatureTest {

	@Mock
	private DigitalSignatureCallbackStorage digitalSignatureCallbackStorage;

	private UpdateStatusDigitalSignature updateStatusDigitalSignature;

	@BeforeEach
	public void setUp() {
		updateStatusDigitalSignature = new UpdateStatusDigitalSignature(digitalSignatureCallbackStorage);
	}

	@Test
	public void testRun() throws DigitalSignatureCallbackException {
		DigitalSignatureStatusDto statusDto = new DigitalSignatureStatusDto(true, "Successful");
		DigitalSignatureCallbackRequestDto callbackRequestDto = new DigitalSignatureCallbackRequestDto(
				statusDto,
				Map.of("documentId", "1", "personId", "123"),
				"hashValue",
				"signatureHashValue"
		);
		MultipleDigitalSignatureCallbackRequestDto requestDto =
				new MultipleDigitalSignatureCallbackRequestDto(List.of(callbackRequestDto));

		updateStatusDigitalSignature.run(requestDto);

		ArgumentCaptor<DigitalSignatureCallbackRequestDto> captor =
				ArgumentCaptor.forClass(DigitalSignatureCallbackRequestDto.class);
		verify(digitalSignatureCallbackStorage, times(1)).updateSignatureStatus(captor.capture());

		assertEquals(callbackRequestDto.getHash(), captor.getValue().getHash());
		assertEquals(callbackRequestDto.getStatus().getSuccess(), captor.getValue().getStatus().getSuccess());
		assertEquals(callbackRequestDto.getStatus().getMsg(), captor.getValue().getStatus().getMsg());
		assertEquals(callbackRequestDto.getMetadata(), captor.getValue().getMetadata());
		assertEquals(callbackRequestDto.getSignatureHash(), captor.getValue().getSignatureHash());
	}

	@Test
	public void testRun_ThrowsDigitalSignatureCallbackException() throws DigitalSignatureCallbackException {
		DigitalSignatureStatusDto statusDto = new DigitalSignatureStatusDto(true, "Successful");
		DigitalSignatureCallbackRequestDto callbackRequestDto = new DigitalSignatureCallbackRequestDto(
				statusDto,
				Map.of("documentId", "1", "personId", "123"),
				"hashValue",
				"signatureHashValue"
		);
		MultipleDigitalSignatureCallbackRequestDto requestDto =
				new MultipleDigitalSignatureCallbackRequestDto(List.of(callbackRequestDto));

		doThrow(new DigitalSignatureCallbackException(DigitalSignatureCallbackEnumException.HASH_NOT_MATCH, "Hash mismatch"))
				.when(digitalSignatureCallbackStorage).updateSignatureStatus(callbackRequestDto);

		DigitalSignatureCallbackException exception = assertThrows(
				DigitalSignatureCallbackException.class,
				() -> updateStatusDigitalSignature.run(requestDto)
		);

		assertEquals(DigitalSignatureCallbackEnumException.HASH_NOT_MATCH, exception.getCode());
		assertEquals("Hash mismatch", exception.getMessage());

		verify(digitalSignatureCallbackStorage, times(1)).updateSignatureStatus(callbackRequestDto);
	}
}
