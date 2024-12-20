package ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.mapper;

import ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.dto.CreateOutpatientServiceRequestDto;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceRequestToFileMapper {

	/**
	 * The controller receives a list of service requests and the files attached to them
	 * as separate fields of the http request.
	 * This method links each service request to its corresponding files.
	 * This method expects that the order of the service requests, their file names and the
	 * list of actual files matches.
	 * Note that the filenames may not be unique.
	 * The frontend may send something like this:
	 * createOutpatientDto: //JSON
	 * 		...
	 * 		serviceRequests: [
	 *          {..., fileNames: [a, b, c]},
	 *        	{..., fileNames: [b, d]},
	 *        	{..., fileNames: []},
	 *        	{..., fileNames: [a]},
	 *        	{..., fileNames: [a]}
	 * 		]
	 *
	 * serviceRequestFiles: //List of MultipartFile
	 * 	[File a, File b, File c, File b, File d, File a, File a]
	 */
	public static Map<CreateOutpatientServiceRequestDto, List<MultipartFile>> buildRequestFilesMap(
			List<CreateOutpatientServiceRequestDto> serviceRequests,
			List<MultipartFile> serviceRequestFiles
	)
	{
		Map<CreateOutpatientServiceRequestDto, List<MultipartFile>> result = new HashMap<>();
		int currentFileIdx = 0;
		for (int i = 0; i < serviceRequests.size(); i++) {

			var currentServiceRequest = serviceRequests.get(i);
			var currentServiceRequestFileNames = currentServiceRequest.getFileNames() == null ?
					Collections.emptyList() : currentServiceRequest.getFileNames();

			List<MultipartFile> currentServiceRequestFiles = new ArrayList<>();

			for (int j = 0; j < currentServiceRequestFileNames.size(); j++) {
				currentServiceRequestFiles.add(serviceRequestFiles.get(currentFileIdx));
				currentFileIdx++;
			}

			result.put(currentServiceRequest, currentServiceRequestFiles);
		}
		return result;
	}
}
