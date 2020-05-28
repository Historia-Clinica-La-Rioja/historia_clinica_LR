package net.pladema.internation.repository.documents.searchdocument;

import net.pladema.sgx.repository.QueryPart;

public class DocumentDiagnosisSearchQuery extends DocumentSearchQuery {

    public DocumentDiagnosisSearchQuery(String plainText){
        super(plainText);
    }

    @Override
    public QueryPart where() {
        return new QueryPart("document.internmentEpisodeId = :internmentEpisodeId \n " +
                "and exists( \n" +
                "select 1 \n" +
                "from DocumentHealthCondition as dhcSub \n " +
                "join HealthCondition as hcSub on (dhcSub.pk.healthConditionId = hcSub.id) " +
                "join Snomed as s on (hcSub.sctidCode = snomed.id ) \n " +
                "where dhcSub.pk.documentId = document.id AND s.pt LIKE '%"+plainText+"%') \n");
    }

    @Override
    public String noResultMessage(){
        return "No se encontraron documentos con diagnóstico " + plainText;
    }
}
