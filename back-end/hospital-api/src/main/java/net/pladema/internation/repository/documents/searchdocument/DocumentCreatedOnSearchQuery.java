package net.pladema.internation.repository.documents.searchdocument;

import net.pladema.sgx.repository.QueryPart;

public class DocumentCreatedOnSearchQuery extends DocumentSearchQuery {

    public DocumentCreatedOnSearchQuery(String plainText) {
        super(plainText);
    }

    @Override
    public QueryPart where() {
        return new QueryPart("document.internmentEpisodeId = :internmentEpisodeId \n" +
                "AND DATE(document.creationable.createdOn) = '" + plainText + "' \n");
    }

    @Override
    public String noResultMessage(){
        return "No se encontraron documentos creados el día " + plainText;
    }
}
