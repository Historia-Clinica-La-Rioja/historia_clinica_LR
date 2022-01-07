package ar.lamansys.refcounterref.infraestructure.output.repository.file;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reference_counter_reference_file")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReferenceCounterReferenceFile {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name="size", nullable = false)
    private long size;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="reference_counter_reference_id", nullable = false)
    private Integer referenceCounterReferenceId;

    @Column(name="type")
    private Integer type;

    public ReferenceCounterReferenceFile(String path, String contentType, long size, String name, Integer type){
        super();
        this.path = path;
        this.contentType = contentType;
        this.size = size;
        this.name = name;
        this.type = type;
    }
}