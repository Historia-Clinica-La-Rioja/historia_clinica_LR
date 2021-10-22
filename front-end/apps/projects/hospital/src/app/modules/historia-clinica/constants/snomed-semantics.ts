export enum SemanticsEnum {
	BodyStructure = '123037004 |estructura corporal|',
	ClinicalFinding = '404684003 |hallazgo clínico (hallazgo)|',
	Event = '272379006 | evento (evento)|',
	Situation = '243796009 |situación con contexto explícito (situación)|',
	FamilyHistoricContext = '57177007 |antecedente familiar con contexto explícito (situación)|',
	SocialContext = '48176007 |contexto social|',
	Drug = '410942007 |sustancia|',
	Substance = '105590001 |sustancia|',
	ABOFinding = '112143006 |ABO group phenotype (finding)|',
	Disorder = '64572001 |trastorno|',
	MedicinalProduct = '763158003 |producto medicinal|',
	Procedure = '71388002 |procedimiento (procedimiento)|',
	AllergicDisposition = '609328004 |disposición alérgica (hallazgo)|',
	ReportableInmunizationsRefset = '2281000221106 |conjunto de referencias simples de inmunizaciones notificables (metadato fundacional)|',
	GenericMedidicine = '763158003: 732943007 |tiene base de sustancia de la potencia (atributo)|=*, [0..0] 774159003 |tiene proveedor (atributo)|=*',
}

const OR = ' OR ';
const CHILDREN_OF = '<';
const CHILDREN_AND_SELF_OF = '<<';
const MEMBER_OF = '^';
const MINUS = ' MINUS '


const ILLEGALLY_INDUCED_ABORTION = [CHILDREN_AND_SELF_OF, '49632008 |aborto ilegal (trastorno)| ', OR,
' 198863002 |readmisión hospitalaria para extracción de productos de la concepción retenidos, aborto ilegal (trastorno)| ', OR,
' 198756005 |aborto ilegal completo con infección pélvica o del tracto genital (trastorno)| ', OR,
' 785872000 |hemorragia excesiva debida a interrupción ilegal inducida de embarazo y posterior a ella (trastorno)| ', OR,
' 785869007 |hemorragia secundaria debida a interrupción ilegal inducida de embarazo y posterior a ella (trastorno)| '].join('');


export const SEMANTICS_CONFIG = {
	diagnosis: [CHILDREN_AND_SELF_OF, SemanticsEnum.ClinicalFinding, OR, CHILDREN_AND_SELF_OF, SemanticsEnum.Situation, OR, CHILDREN_AND_SELF_OF, SemanticsEnum.Event].join(''),
	bloodType: [CHILDREN_OF, SemanticsEnum.ABOFinding].join(''),
	personalRecord: [CHILDREN_AND_SELF_OF, SemanticsEnum.ClinicalFinding, OR, CHILDREN_AND_SELF_OF, SemanticsEnum.Situation, OR, CHILDREN_AND_SELF_OF, SemanticsEnum.Event].join(''),
	familyRecord: [CHILDREN_AND_SELF_OF, SemanticsEnum.ClinicalFinding, OR, CHILDREN_AND_SELF_OF, SemanticsEnum.Situation, OR, CHILDREN_AND_SELF_OF, SemanticsEnum.Event].join(''),
	allergy: [CHILDREN_OF, SemanticsEnum.AllergicDisposition].join(''),
	hospitalizationReason: [CHILDREN_OF, SemanticsEnum.ClinicalFinding, OR, SemanticsEnum.Event, OR, SemanticsEnum.Situation, OR, SemanticsEnum.SocialContext].join(''),
	vaccine: [MEMBER_OF, SemanticsEnum.ReportableInmunizationsRefset].join(''),
	medicine: [CHILDREN_OF, SemanticsEnum.GenericMedidicine].join(''),
	procedure: [CHILDREN_OF, SemanticsEnum.Procedure].join(''),
	consultationReason: [CHILDREN_AND_SELF_OF, SemanticsEnum.ClinicalFinding, OR, CHILDREN_AND_SELF_OF, SemanticsEnum.Procedure, OR, CHILDREN_AND_SELF_OF, SemanticsEnum.Situation, OR, CHILDREN_AND_SELF_OF, SemanticsEnum.Event].join('')
};
