package commercial_medication.update_schema.cache.domain.service;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CDataAdapter extends XmlAdapter<String, String> {

	@Override
	public String marshal(String v) {
		return "<![CDATA[" + v + "]]>";
	}

	@Override
	public String unmarshal(String v) {
		return v;
	}

}

