package net.pladema.medication.application.port;

import net.pladema.medication.domain.decodedResponse.ArticleList;
import net.pladema.medication.domain.decodedResponse.DatabaseUpdate;

import java.util.List;

public interface CommercialMedicationArticlePort {

	void saveAll(ArticleList articles);

    void saveAllNewArticlesFromUpdate(List<DatabaseUpdate> databaseUpdates);

	void editArticles(List<DatabaseUpdate> databaseUpdates);

	void updatePrices(List<DatabaseUpdate> databaseUpdates);

	void deleteAll(List<DatabaseUpdate> databaseUpdates);

}
