export enum SemanticsEnum {
    BodyStructure = '123037004 |estructura corporal|',
    ClinicalFinding = '404684003 |hallazgo|',
    Event = ' 272379006 |evento|',
    Situation = '243796009 |situación|',
    FamilyHistoricContext = '57177007 |situation|',
    SocialContext = '48176007 |contexto social|',
    Drug = '410942007 |sustancia|',
    Substance = '105590001 |sustancia|',
    ABOFinding = '112143006 |ABO group phenotype (finding)|',
    Disorder = '64572001 |trastorno|',
    ClinicalDrug = '|(fármaco de uso clínico)|', //TODO: no anda, investigar codigo
	MedicinalProduct = '763158003 |producto medicinal|'
}

const OR = ' OR ';
const CHILDREN_OF = '<';
const WITHOUT = ' minus ';
const BEGIN = '(';
const END = ')';

export const SEMANTICS_CONFIG = {
    diagnosis: [CHILDREN_OF, SemanticsEnum.ClinicalFinding].join(""),
    bloodType: [CHILDREN_OF, SemanticsEnum.ABOFinding].join(""),
    personalRecord: [CHILDREN_OF, SemanticsEnum.ClinicalFinding, OR, BEGIN, SemanticsEnum.Situation, WITHOUT, SemanticsEnum.FamilyHistoricContext, END,
        OR, SemanticsEnum.SocialContext].join(""),
    familyRecord: [CHILDREN_OF, SemanticsEnum.FamilyHistoricContext].join(""),
    allergy: [CHILDREN_OF, SemanticsEnum.ClinicalFinding, OR, CHILDREN_OF, SemanticsEnum.Disorder, OR, CHILDREN_OF, SemanticsEnum.Situation].join(""),
    hospitalizationReason: [CHILDREN_OF, SemanticsEnum.ClinicalFinding, OR, SemanticsEnum.Event, OR, SemanticsEnum.Situation, OR, SemanticsEnum.SocialContext].join(""),
    vaccine: [CHILDREN_OF, SemanticsEnum.MedicinalProduct].join(""),
	medicine: [CHILDREN_OF, SemanticsEnum.Drug].join(""),
	consultationReason: [CHILDREN_OF, SemanticsEnum.ClinicalFinding].join("")
}
