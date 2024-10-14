package net.pladema.imagenetwork.domain;

import ar.lamansys.sgx.shared.exceptions.SelfValidating;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Objects;

import static net.pladema.imagenetwork.domain.PacsListBo.CONTEXT;
import static net.pladema.imagenetwork.domain.PacsListBo.SCHEMA;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PacsBo extends SelfValidating<PacsBo> {

    @NotNull
    private Integer id;

    @NotBlank
    private String domain;

    public URI getURI() {
        return UriComponentsBuilder.newInstance()
                        .scheme(SCHEMA)
                        .host(domain)
                        .path(CONTEXT)
                        .build()
                        .toUri();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PacsBo)) return false;
        PacsBo pacsBo = (PacsBo) o;
        return Objects.equals(this.getId(), pacsBo.getId());
    }

    public String getUrl() {
        return getURI().toString();
    }

    public static String getDomain(String url) {
        return URI.create(url).getHost();
    }
}
