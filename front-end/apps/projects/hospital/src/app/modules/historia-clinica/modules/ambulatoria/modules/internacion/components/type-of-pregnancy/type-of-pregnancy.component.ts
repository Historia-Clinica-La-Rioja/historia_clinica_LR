import { Component } from '@angular/core';
import { ControlDynamicFormService } from '../../services/control-dynamic-form.service';
import { ObstetricFormService } from '../../services/obstetric-form.service';

@Component({
	selector: 'app-type-of-pregnancy',
	templateUrl: './type-of-pregnancy.component.html',
	styleUrls: ['./type-of-pregnancy.component.scss']
})
export class TypeOfPregnancyComponent {

	SIMPLE = TypeOfPregnancy.SIMPLE;
	MULTIPLE = TypeOfPregnancy.MULTIPLE;
	UNDEFINED = TypeOfPregnancy.UNDEFINED;
	selectedOption = null;

	constructor(
		private readonly controlDynamicFormService: ControlDynamicFormService,
		private readonly obstetricFormService: ObstetricFormService,
	) {
		this.obstetricFormService.getValue().subscribe((obstetricEvent) => {
			this.selectedOption = this.calculateSelectedOption(obstetricEvent?.newborns?.length);
		});
	}

	private calculateSelectedOption(newbornsCount: number | undefined): TypeOfPregnancy | null {
		if (newbornsCount !== undefined && newbornsCount !== 0) {
			return (newbornsCount === 1) ? TypeOfPregnancy.SIMPLE : TypeOfPregnancy.MULTIPLE;
		}
		return null;
	}

	emmitEvent() {
		this.controlDynamicFormService.updateSelectedOption(this.selectedOption);
	}
}


export const enum TypeOfPregnancy {
	SIMPLE = "SIMPLE",
	MULTIPLE = "MULTIPLE",
	UNDEFINED = "UNDEFINED",
}
