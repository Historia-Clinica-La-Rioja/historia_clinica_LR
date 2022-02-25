package ar.lamansys.odontology.domain;


public interface Publisher {

	public void run(Integer patientId, EOdontologyTopicDto EOdontologyTopicDto);
}
