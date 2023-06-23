package net.pladema.person.repository;

import ar.lamansys.sgx.shared.repositories.QueryPart;
import lombok.NoArgsConstructor;
import net.pladema.patient.controller.dto.AuditPatientSearch;
import net.pladema.person.repository.domain.DuplicatePersonVo;

import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
public class AuditPersonSearchQuery {

	private boolean name;
	private boolean identify;
	private boolean birthdate;

	public AuditPersonSearchQuery(AuditPatientSearch auditPatientSearch) {
		if (auditPatientSearch.getName())
			this.name = true;
		if (auditPatientSearch.getIdentify())
			this.identify = true;
		if (auditPatientSearch.getBirthdate())
			this.birthdate = true;
	}

	public QueryPart select() {
		String select = "";
		if (name) {
			select = " person.first_name, \n" +
					" COALESCE(person.middle_names, '') AS middle_names, \n" +
					" person.last_name, \n" +
					" COALESCE(person.other_last_names, '') AS other_last_names, \n";
		}
		if (identify) {
			select += " person.identification_type_id, \n" +
					" person.identification_number, \n";
		}
		if (birthdate) {
			select +=  " person.birth_date, \n";
		}
		select += " count(1) \n";
		return new QueryPart(select);
	}

	public QueryPart from() {
		String from = " {h-schema}person as person \n" +
		"	join {h-schema}patient as patient on (patient.person_id = person.id) and (patient.deleted != true) and (patient.type_id != 6) \n";
		return new QueryPart(from);
	}

	public QueryPart where() {
		String where = "";
		if (name) {
			where += " person.first_name is not null AND person.last_name is not null ";
		}
		if (identify) {
			if (name)
				where += " AND ";
			where += " person.identification_type_id is not null AND person.identification_number is not null ";
		}
		if (birthdate) {
			if (name || identify) {
				where += " AND ";
			}
			where += " person.birth_date is not null ";
		}
		where += "\n";
		return new QueryPart(where);
	}

	public QueryPart groupBy() {
		String groupBy = "";
		if (name) {
			groupBy = " person.first_name, \n" +
					" COALESCE(person.middle_names, ''), \n" +
					" person.last_name, \n" +
					" COALESCE(person.other_last_names, ''), \n";
		}
		if (identify) {
			groupBy += " person.identification_type_id, \n" +
					" person.identification_number, \n";
		}
		if (birthdate) {
			groupBy +=  " person.birth_date, \n";
		}
		groupBy = groupBy.substring(0, groupBy.length()-3);
		groupBy += " \n";
		return new QueryPart(groupBy);
	}

	public QueryPart having() {
		String having = " count(1) > 1 \n";
		return new QueryPart(having);
	}

	public QueryPart orderBy() {
		String orderBy = " count(1) desc \n";
		return new QueryPart(orderBy);
	}

	public List<DuplicatePersonVo> construct(List<Object[]> resultQuery) {
		List<DuplicatePersonVo> result = new ArrayList<>();
		for (Object[] objects : resultQuery) {
			String firstName = null;
			String middleName = null;
			String lastName = null;
			String otherLastName = null;
			Short identifyTypeId = null;
			String identifyNumber = null;
			Date birthdate = null;
			LocalDate birthdateLD = null;
			BigInteger numberOfCandidates = null;
			int index = 0;
			if (this.name) {
				firstName = (String) objects[0];
				middleName = (String) objects[1];
				lastName = (String) objects[2];
				otherLastName = (String) objects[3];
				index = 4;
			}
			if (this.identify) {
				identifyTypeId = (Short) objects[index];
				identifyNumber = (String) objects[index + 1];
				index = index + 2;
			}
			if (this.birthdate) {
				birthdate = (Date) objects[index];
				index++;
			}
			numberOfCandidates = (BigInteger) objects[index];
			if (birthdate != null)
				birthdateLD = birthdate.toLocalDate();
			result.add(new DuplicatePersonVo(firstName, middleName, lastName, otherLastName, identifyTypeId, identifyNumber, birthdateLD, numberOfCandidates.longValue()));
		}
		return result;
	}


}
