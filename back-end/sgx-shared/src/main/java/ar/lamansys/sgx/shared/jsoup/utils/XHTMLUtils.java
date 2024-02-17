package ar.lamansys.sgx.shared.jsoup.utils;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class XHTMLUtils {

	public static String toXHTML(String html, boolean removeBaseTags) {
		final Document document = Jsoup.parse(html);
		document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
		removeBaseTags(document, removeBaseTags);
		return document.html();
	}

	private static void removeBaseTags(Document doc, boolean removeBaseTags) {
		if (removeBaseTags) {
			String body = doc.body().html();
			doc.select("body").remove();
			doc.select("head").remove();
			doc.select("html").remove();
			doc.prepend(body);
		}
	}
}