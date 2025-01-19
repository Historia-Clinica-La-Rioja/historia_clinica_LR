import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
	name: 'showProblems'
})
export class ShowProblemsPipe implements PipeTransform {

	transform(problems: string[]): string {
		const problemToShow = problems[0];
		if (problems.length === 2)
			return `${problemToShow}\n(+ ${problems.length - 1} problema más)`;
		if (problems.length > 2)
			return `${problemToShow}\n(+ ${problems.length - 1} problemas más)`;
		return problemToShow;
	}

}
