package commercial_medication.update_schema.cache.application.port;

import commercial_medication.update_schema.cache.domain.decodedResponse.ArticleList;
import commercial_medication.update_schema.cache.domain.decodedResponse.DatabaseUpdate;

import java.util.List;

public interface CommercialMedicationArticlePort {

	void saveAll(ArticleList articles);

    void saveAllNewArticlesFromUpdate(List<DatabaseUpdate> databaseUpdates);

	void editArticles(List<DatabaseUpdate> databaseUpdates);

	void updatePrices(List<DatabaseUpdate> databaseUpdates);

	void deleteAll(List<DatabaseUpdate> databaseUpdates);

}
