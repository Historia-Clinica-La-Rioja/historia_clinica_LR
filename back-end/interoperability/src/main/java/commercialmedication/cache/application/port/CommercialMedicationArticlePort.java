package commercialmedication.cache.application.port;

import commercialmedication.cache.domain.decodedResponse.ArticleList;
import commercialmedication.cache.domain.decodedResponse.DatabaseUpdate;

import java.util.List;

public interface CommercialMedicationArticlePort {

	void saveAll(ArticleList articles);

    void saveAllNewArticlesFromUpdate(List<DatabaseUpdate> databaseUpdates);

	void editArticles(List<DatabaseUpdate> databaseUpdates);

	void updatePrices(List<DatabaseUpdate> databaseUpdates);

	void deleteAll(List<DatabaseUpdate> databaseUpdates);

}
