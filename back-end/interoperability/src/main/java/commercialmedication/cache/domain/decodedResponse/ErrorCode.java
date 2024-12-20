package commercialmedication.cache.domain.decodedResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "error")
public class ErrorCode {

	public final static Short NO_ERROR_CODE = 0;

	@XmlElement(name = "codigo")
	private Short code;

	@XmlElement(name = "descripcion")
	private String description;

}
