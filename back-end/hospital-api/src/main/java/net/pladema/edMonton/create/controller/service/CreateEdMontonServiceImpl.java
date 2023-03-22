package net.pladema.edMonton.create.controller.service;

import net.pladema.edMonton.create.controller.service.domain.EdMontonAnswerBo;
import net.pladema.edMonton.create.controller.service.domain.EdMontonBo;

import net.pladema.edMonton.repository.EdMontonRepository;

import net.pladema.edMonton.repository.domain.Answer;

import net.pladema.edMonton.repository.domain.QuestionnaireResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;

@Service
public class CreateEdMontonServiceImpl implements CreateEdMontonService{

	private static final Logger LOG = LoggerFactory.getLogger(CreateEdMontonServiceImpl.class);

	private static final String OUTPUT = "Output -> {}";

 	private final EdMontonRepository edMontonRespository;


	 public CreateEdMontonServiceImpl(EdMontonRepository edMontonRepository){
		 this.edMontonRespository = edMontonRepository;
	 }


	@Override
	public EdMontonBo execute(EdMontonBo edMontonBo) {

		QuestionnaireResponse entity = createEdMontonTest(edMontonBo);

		edMontonRespository.save(entity);

		return edMontonBo;
	}

	private QuestionnaireResponse createEdMontonTest(EdMontonBo edMontonBo){
		QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
		EdMontonAnswerBo answerBo;
		Answer answer;
		Integer idCuestionario = 1;
		Integer idStatus= 2;
		questionnaireResponse.setPatientId(Math.toIntExact(edMontonBo.getPatientId()));
		if( edMontonBo.getAnswers() != null && edMontonBo.getAnswers().size()>0){
			questionnaireResponse.setAnswers( new ArrayList<Answer>());

			Iterator<EdMontonAnswerBo> it = edMontonBo.getAnswers().iterator();
			while (it.hasNext()){
				answerBo = it.next();


				answer = new Answer();
				answer.setAnswerId(Math.toIntExact(answerBo.getAnswerId()));
				answer.setItemId(Math.toIntExact(answerBo.getQuestionId()));

				questionnaireResponse.getAnswers().add(answer);
			}
				questionnaireResponse.setQuestionnaireId(idCuestionario);
				questionnaireResponse.setStatusId(idStatus);

		}
		return questionnaireResponse;
	}
}
