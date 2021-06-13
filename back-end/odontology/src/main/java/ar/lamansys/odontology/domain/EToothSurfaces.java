package ar.lamansys.odontology.domain;

public enum EToothSurfaces {

    MESIAL(new OdontologySnomedBo("245652002","Cara mesial de pieza dentaria [como un todo] (estructura corporal)")),
    DISTAL(new OdontologySnomedBo("245653007","cara distal de pieza dentaria [como un todo] (estructura corporal)")),
    VESTIBULAR(new OdontologySnomedBo("245647007","cara vestibular de pieza dentaria [como un todo] (estructura corporal)")),
    OCLUSAL(new OdontologySnomedBo("362103001","cara oclusal de pieza dentaria [como un todo] (estructura corporal)")),
    INCISAL(new OdontologySnomedBo("245645004","borde incisal de pieza dentaria (estructura corporal)")),
    PALATINA(new OdontologySnomedBo("245650005","cara palatina de diente [como un todo] (estructura corporal)")),
    LINGUAL(new OdontologySnomedBo("362104007","cara lingual de pieza dentaria [como un todo] (estructura corporal)")),
    ;

    private OdontologySnomedBo snomed;

    EToothSurfaces(OdontologySnomedBo odontologySnomedBo) {
        this.snomed = odontologySnomedBo;
    }

    public OdontologySnomedBo getValue() {
        return snomed;
    }
}
