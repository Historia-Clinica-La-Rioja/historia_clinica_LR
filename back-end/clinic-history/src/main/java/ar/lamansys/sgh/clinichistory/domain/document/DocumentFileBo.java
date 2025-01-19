package ar.lamansys.sgh.clinichistory.domain.document;

import lombok.Getter;

@Getter
public class DocumentFileBo {

    private final Long id;

    private final Integer sourceId;

    private final Short sourceTypeId;

    private final Short typeId;

    private final String filepath;

    private final String filename;

    private final String uuidfile;

    private final String checksum;

	private final Short signatureStatusId;

	private final String digitalSignatureHash;

    public DocumentFileBo(Long id, Integer sourceId, Short sourceTypeId, Short typeId, String filepath,
                          String filename, String uuidfile, String checksum, Short signatureStatusId, String digitalSignatureHash) {
        this.id = id;
        this.sourceId = sourceId;
        this.sourceTypeId = sourceTypeId;
        this.typeId = typeId;
        this.filepath = filepath;
        this.filename = filename;
        this.uuidfile = uuidfile;
        this.checksum = checksum;
		this.signatureStatusId = signatureStatusId;
		this.digitalSignatureHash = digitalSignatureHash;
    }
}
