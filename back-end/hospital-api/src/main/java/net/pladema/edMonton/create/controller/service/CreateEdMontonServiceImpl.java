package net.pladema.edMonton.create.controller.service;

import net.pladema.edMonton.create.controller.service.domain.EdMontonAnswerBo;
import net.pladema.edMonton.create.controller.service.domain.EdMontonBo;

import net.pladema.edMonton.get.service.GetEdMontonService;
import net.pladema.edMonton.getPdfEdMonton.dto.QuestionnaireDto;
import net.pladema.edMonton.repository.EdMontonRepository;

import net.pladema.edMonton.repository.domain.Answer;

import net.pladema.edMonton.repository.domain.QuestionnaireResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service
public class CreateEdMontonServiceImpl implements CreateEdMontonService{

	private static final Logger LOG = LoggerFactory.getLogger(CreateEdMontonServiceImpl.class);

	private static final String OUTPUT = "Output -> {}";

 	private final EdMontonRepository edMontonRespository;

	 private final GetEdMontonService getEdMontonService;


	 public CreateEdMontonServiceImpl(EdMontonRepository edMontonRepository, GetEdMontonService getEdMontonService){
		 this.edMontonRespository = edMontonRepository;
		 this.getEdMontonService = getEdMontonService;
	 }


	@Override
	public EdMontonBo execute(EdMontonBo edMontonBo) {

		QuestionnaireDto questionnaireDto = new QuestionnaireDto();

		 Integer idPatient = edMontonBo.getPatientId();

		 List<Answer> lst = getEdMontonService.findPatientEdMonton(idPatient);

		 if(lst != null){
			 for(Answer answer : lst){
				 Integer id;
				 id = answer.getQuestionnaireResponseId();
				 QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse(edMontonRespository.findById(id));
				 edMontonRespository.updateStatusById(questionnaireResponse.getId(), 3);
			 }

			 QuestionnaireResponse entity = createEdMontonTest(edMontonBo);

			 edMontonRespository.save(entity);
		 }
		return edMontonBo;
	}

	private QuestionnaireResponse createEdMontonTest(EdMontonBo edMontonBo){
		QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
		EdMontonAnswerBo answerBo;
		Answer answer;
		Integer idCuestionario = 1;
		Integer idStatus= 2;
		Integer resultFinal = edMontonBo.getResult();
		Answer answerOne = new Answer();


		questionnaireResponse.setPatientId(Math.toIntExact(edMontonBo.getPatientId()));
		if( edMontonBo.getAnswers() != null && edMontonBo.getAnswers().size()>0){
			questionnaireResponse.setAnswers( new ArrayList<Answer>());

			Iterator<EdMontonAnswerBo> it = edMontonBo.getAnswers().iterator();
			while (it.hasNext()){
				answerBo = it.next();


				answer = new Answer();
				answer.setAnswerId(Math.toIntExact(answerBo.getAnswerId()));
				answer.setItemId(Math.toIntExact(answerBo.getQuestionId()));
				answer.setValue(String.valueOf(answerBo.getValue()));
				questionnaireResponse.getAnswers().add(answer);

			}
				answerOne.setItemId(21);
				answerOne.setValue(String.valueOf(resultFinal));
				questionnaireResponse.getAnswers().add(answerOne);



				questionnaireResponse.setQuestionnaireId(idCuestionario);
				questionnaireResponse.setStatusId(idStatus);

		}
		return questionnaireResponse;
	}
}
