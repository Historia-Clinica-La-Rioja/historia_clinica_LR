package net.pladema.clinichistory.outpatient.createoutpatient.controller;


import ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.dto.CreateOutpatientServiceRequestDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.mapper.ServiceRequestToFileMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceRequestToFileMapperTest {

	private class MockFile implements MultipartFile {
		private String fileName;

		public MockFile(String fileName) {
		}
		@Override
		public String getName() {
			return fileName;
		}

		@Override
		public String getOriginalFilename() {
			return fileName;
		}

		@Override
		public String getContentType() {
			return null;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public long getSize() {
			return 0;
		}

		@Override
		public byte[] getBytes() throws IOException {
			return new byte[0];
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return null;
		}

		@Override
		public void transferTo(File dest) throws IOException, IllegalStateException {

		}
	}

	@Test
	public void create_outpatient_service_request_files_are_mapped_correctly() {
		List<CreateOutpatientServiceRequestDto> serviceRequests = new ArrayList<>();
		List<MultipartFile> serviceRequestFiles = new ArrayList<>();
		MultipartFile file0 = new MockFile("a");
		MultipartFile file1 = new MockFile("b");
		MultipartFile file2 = new MockFile("c");
		MultipartFile file3 = new MockFile("b");
		MultipartFile file4 = new MockFile("d");
		MultipartFile file5 = new MockFile("a");

		var dto0 = createDto("a", "b", "c");
		var dto1 = createDto("b", "d");
		var dto2 = createDto();
		var dto3 = createDto("a");
		var dto4 = createDto();

		var dtos = List.of(dto0, dto1, dto2, dto3, dto4);
		var files = List.of(file0, file1, file2, file3, file4, file5);

		var result = ServiceRequestToFileMapper.buildRequestFilesMap(dtos, files);

		Assertions.assertEquals(result.get(dto0).get(0), file0);
		Assertions.assertEquals(result.get(dto0).get(1), file1);
		Assertions.assertEquals(result.get(dto0).get(2), file2);

		Assertions.assertEquals(result.get(dto1).get(0), file3);
		Assertions.assertEquals(result.get(dto1).get(1), file4);

		Assertions.assertTrue(result.get(dto2).isEmpty());

		Assertions.assertEquals(result.get(dto3).get(0), file5);

		Assertions.assertTrue(result.get(dto4).isEmpty());

	}

	private CreateOutpatientServiceRequestDto createDto(String ...filenames) {
		var ret = new CreateOutpatientServiceRequestDto();
		ret.setFileNames(Arrays.asList(filenames));
		return ret;
	}
}
