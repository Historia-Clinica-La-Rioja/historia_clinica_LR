package net.pladema.edMonton.create.service.domain;


import com.fasterxml.jackson.annotation.JsonCreator;

import net.pladema.edMonton.domain.exception.EdMontonEnumException;
import net.pladema.edMonton.domain.exception.EdMontonException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public enum EedmontonTestQuestion {

	Q1( (short)2, "Por favor imagina que este círculo pre-dibujado es un reloj." +
			" Me gustaría que pusiera los números en las posiciones correctas y luego poner" +
			" las manilas o manecillas para indicar la hora “Las once con diez minutos" ),
	Q2( (short)4, "En el ultimo año, ¿Cuántas veces ha estado hospitalizado?"),
	Q3( (short)5, "En general, ¿Cómo describiría su salud?"),
	Q4( (short)7, "Me gustaría que se sentara en esta silla con su espalda y brazos relajados." +
			" Luego, cuando yo diga “YA”, por favor párese y camine a un ritmo cómodo y seguro a la marca del suelo" +
			" (aproximadamente a 3 metros de distancia), regrese a su silla y se sienta."),
	Q5( (short)9, "¿Con cuántas de las siguientes actividades necesita ayuda? (preparar la comida, comparas, " +
			"transporte, comunicación telefónica, cuidado del hogar, lavado de ropa, manejo de dinero, tomar medicamentos)"),

	Q6( (short)11, "Cuando Ud. necesita ayuda, ¿puede contar con alguien que esté dispuesto y disponible para atender sus necesidades o problemas?"),
	Q7( (short)13, "¿Usa 5 o más medicamentos en el día a día?"),
	Q8( (short)14, "¿En ocasiones, ¿se le olvida tomarse los medicamentos?"),
	Q9( (short)16, "Recientemente, ¿ha perdido peso como para que su ropa le quede suelta?"),
	Q10( (short)18, "¿Se siente con frecuencia triste o deprimido?"),
	Q11( (short)20, "¿Tiene algún problema con el control para orinar, es decir puede contener la orina si así lo desea? ");
	private final Short questionId;
	private final String question;

	 EedmontonTestQuestion(short questionId, String question){
		this.questionId = questionId;
		this.question = question;
	}

	@JsonCreator
	public static List<EedmontonTestQuestion> getAll() {return Arrays.asList(EedmontonTestQuestion.values()); }

	@JsonCreator
	public static EedmontonTestQuestion getById(Short id){
		 if(id == null)
			 return null;
		 return Stream.of(values())
				 .filter( eat -> id.equals(eat.getQuestionId()))
				 .findAny()
				 .orElseThrow(() -> new EdMontonException(EdMontonEnumException.EDMONTON_TEST_QUESTION_NOT_EXISTS, String.format("el valor %s es invalido como pregunta", id)));
	}


	public Short getQuestionId() {return questionId;}

	public String getQuestion(){ return question; }
}
