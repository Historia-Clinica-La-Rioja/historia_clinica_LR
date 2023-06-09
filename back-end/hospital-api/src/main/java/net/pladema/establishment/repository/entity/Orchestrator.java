package net.pladema.establishment.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;

import java.sql.Time;
import java.text.SimpleDateFormat;

@Entity
@Table(name = "orchestrator")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Orchestrator {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", nullable = false, length = 40)
	private String name;

	@Column(name = "base_topic", nullable = false, length = 250)
	private String baseTopic;

	@Column(name = "sector_id", nullable = false)
	private Integer sectorId;

	@Column(name = "attemps_number",  columnDefinition = "int default 3", nullable = false)
	private Integer attempsNumber = 3;

	@Column(name = "execution_start_time",  columnDefinition = "time default '22:00:00'", nullable = false)
	private Time executionStartTime = setTimeDefault("08:00:00");

	@Column(name = "execution_end_time",  columnDefinition = "time default '06:00:00'", nullable = false)
	private Time executionEndTime = setTimeDefault("06:00:00");

	@Column(name = "weight_days", columnDefinition = "float default 0.3", nullable = false)
	private Double weightDays = 0.3;

	@Column(name = "weight_size", columnDefinition = "float default 0.01",nullable = false)
	private Double weightSize = 0.01;

	@Column(name = "weight_priority", columnDefinition = "float default 0.2",nullable = false)
	private Double weightPriority = 0.2;

	@Column(name = "number_to_move",  columnDefinition = "int default 10", nullable = false)
	private Integer numberToMove = 10;


	@SneakyThrows
	private Time setTimeDefault(String timeString){
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		Time time;
		java.util.Date date = format.parse(timeString);
		time = new Time(date.getTime());
		return time;
	}
}
