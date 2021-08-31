package ar.lamansys.odontology.domain;

public enum EToothSurfaces {

    MESIAL(new OdontologySnomedBo("245652002","cara mesial de pieza dentaria")),
    DISTAL(new OdontologySnomedBo("245653007","cara distal de pieza dentaria")),
    VESTIBULAR(new OdontologySnomedBo("245647007","cara vestibular de pieza dentaria")),
    OCLUSAL(new OdontologySnomedBo("362103001","cara oclusal de pieza dentaria")),
    INCISAL(new OdontologySnomedBo("245645004","borde incisal de pieza dentaria")),
    PALATINA(new OdontologySnomedBo("245650005","cara palatina de pieza dentaria")),
    LINGUAL(new OdontologySnomedBo("362104007","cara lingual de pieza dentaria")),
    ;

    private OdontologySnomedBo snomed;

    EToothSurfaces(OdontologySnomedBo odontologySnomedBo) {
        this.snomed = odontologySnomedBo;
    }

    public OdontologySnomedBo getValue() {
        return snomed;
    }
}
