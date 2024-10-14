import { Pipe, PipeTransform } from '@angular/core';
import { DiagnosesGeneralStateDto } from '@api-rest/api-model';

@Pipe({
  name: 'problemStatus'
})
export class ProblemStatusPipe implements PipeTransform {

	transform(problem: DiagnosesGeneralStateDto): string {
		return problem.presumptive
			? "guardia.episode.medical_discharge.problems.PRESUMPTIVE"
			: "guardia.episode.medical_discharge.problems.CONFIRMED";
	}

}
