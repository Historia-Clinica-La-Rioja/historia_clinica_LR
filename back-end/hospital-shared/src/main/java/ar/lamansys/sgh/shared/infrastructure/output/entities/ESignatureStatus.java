package ar.lamansys.sgh.shared.infrastructure.output.entities;


import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum ESignatureStatus {

	CANNOT_BE_SIGNED(-1, "No apto para firmar"),
    PENDING(1, "Firma pendiente"),
    IN_PROGRESS(2, "Firma en proceso"),
    SIGNED(3, "Firmado")
	;
    private Short id;
    private String value;

    ESignatureStatus(Number id, String value) {
        this.id = id.shortValue();
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
    public Short getId() {
        return id;
    }

    public static ESignatureStatus map(Short id) {
        for(ESignatureStatus e : values()) {
            if(e.id.equals(id)) return e;
        }
        throw new NotFoundException("signature-status-not-exists", String.format("El estado de firma %s no existe", id));
    }

}
