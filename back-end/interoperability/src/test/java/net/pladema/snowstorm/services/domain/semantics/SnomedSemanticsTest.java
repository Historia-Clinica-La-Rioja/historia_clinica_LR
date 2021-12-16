package net.pladema.snowstorm.services.domain.semantics;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestPropertySource(value = "classpath:snomed-semantics.properties")
@EnableConfigurationProperties(value = SnomedSemantics.class)
class SnomedSemanticsTest {

    @Autowired
    private SnomedSemantics snomedSemantics;

    @Test
    void successLoadProperties() {
        Assertions.assertEquals("< 112143006 |ABO group phenotype (finding)|", snomedSemantics.getEcl(SnomedECL.BLOOD_TYPE));
        Assertions.assertEquals("<< 404684003 |hallazgo clínico (hallazgo)| OR << 243796009 |situación con contexto explícito (situación)| OR << 272379006 | evento (evento)|", snomedSemantics.getEcl(SnomedECL.DIAGNOSIS));
        Assertions.assertEquals("<< 404684003 |hallazgo clínico (hallazgo)| OR <<  243796009 |situación con contexto explícito (situación)| OR << 272379006 | evento (evento)|", snomedSemantics.getEcl(SnomedECL.FAMILY_RECORD));
        Assertions.assertEquals("<< 404684003 |hallazgo clínico (hallazgo)| OR <<  243796009 |situación con contexto explícito (situación)| OR << 272379006 | evento (evento)|", snomedSemantics.getEcl(SnomedECL.PERSONAL_RECORD));
        Assertions.assertEquals("<< 404684003 |hallazgo clínico (hallazgo)| OR << 71388002 |procedimiento (procedimiento)| OR << 243796009 |situación con contexto explícito (situación)| OR << 272379006 | evento (evento)|", snomedSemantics.getEcl(SnomedECL.CONSULTATION_REASON));
        Assertions.assertEquals("< 404684003 |hallazgo clínico (hallazgo)| OR 272379006 | evento (evento)| OR 243796009 |situación con contexto explícito (situación)| OR 48176007 |contexto social|", snomedSemantics.getEcl(SnomedECL.HOSPITAL_REASON));
        Assertions.assertEquals("< 71388002 |procedimiento (procedimiento)|", snomedSemantics.getEcl(SnomedECL.PROCEDURE));
        Assertions.assertEquals("< 609328004 |disposición alérgica (hallazgo)|", snomedSemantics.getEcl(SnomedECL.ALLERGY));
        Assertions.assertEquals("^ 2281000221106 |conjunto de referencias simples de inmunizaciones notificables (metadato fundacional)|", snomedSemantics.getEcl(SnomedECL.VACCINE));
        Assertions.assertEquals("< 763158003: 732943007 |tiene base de sustancia de la potencia (atributo)|=*, [0..0] 774159003 |tiene proveedor (atributo)|=*", snomedSemantics.getEcl(SnomedECL.MEDICINE));
    }
}