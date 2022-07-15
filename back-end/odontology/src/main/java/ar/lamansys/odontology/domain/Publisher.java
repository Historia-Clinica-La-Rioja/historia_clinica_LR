package ar.lamansys.odontology.domain;


public interface Publisher {

	public void run(Integer patientId, Integer institutionId, EOdontologyTopicDto EOdontologyTopicDto);
}
