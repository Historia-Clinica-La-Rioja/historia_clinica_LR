package ar.lamansys.sgh.clinichistory.mocks;


import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;

public class DocumentsTestMocks {

    public static Document createDocument(Integer internmentEpisodeId, Short type, Short sourceType, String statusId) {
        Document result = new Document();
        result.setSourceId(internmentEpisodeId);
        result.setStatusId(statusId);
        result.setTypeId(type);
        result.setSourceTypeId(sourceType);
        return result;
    }
}
