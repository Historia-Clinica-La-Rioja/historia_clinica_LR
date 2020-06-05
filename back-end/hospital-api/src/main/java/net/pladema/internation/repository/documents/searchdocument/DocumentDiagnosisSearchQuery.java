package net.pladema.internation.repository.documents.searchdocument;

import net.pladema.sgx.repository.QueryPart;

public class DocumentDiagnosisSearchQuery extends DocumentSearchQuery {

    public DocumentDiagnosisSearchQuery(String plainText){
        super(plainText);
    }

    @Override
    public QueryPart where() {
        String pattern = plainText.toLowerCase();
        return super.where().concatPart(new QueryPart(
                "AND exists( \n" +
                "select 1 \n" +
                "from DocumentHealthCondition as dhcSub \n " +
                "join HealthCondition as hcSub on (dhcSub.pk.healthConditionId = hcSub.id) \n" +
                "join Snomed as s on (hcSub.sctidCode = s.id ) \n " +
                "where dhcSub.pk.documentId = document.id AND LOWER(s.pt) LIKE '%"+pattern+"%') \n"));
    }

    @Override
    public String noResultMessage(){
        return "No se encontraron documentos con diagnóstico " + plainText;
    }
}
