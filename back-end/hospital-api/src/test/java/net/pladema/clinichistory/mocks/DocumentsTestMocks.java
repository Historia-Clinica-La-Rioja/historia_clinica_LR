package net.pladema.clinichistory.mocks;

import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentStatus;

public class DocumentsTestMocks {

    public static Document createDocument(Integer internmentEpisodeId, Short type) {
        Document result = new Document();
        result.setInternmentEpisodeId(internmentEpisodeId);
        result.setStatusId(DocumentStatus.FINAL);
        result.setTypeId(type);
        return result;
    }

}
