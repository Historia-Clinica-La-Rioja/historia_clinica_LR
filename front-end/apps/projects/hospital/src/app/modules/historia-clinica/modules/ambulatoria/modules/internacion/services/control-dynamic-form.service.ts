import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { TypeOfPregnancy } from '../components/type-of-pregnancy/type-of-pregnancy.component';

@Injectable()

export class ControlDynamicFormService {
	private selectedOptionSource = new Subject<TypeOfPregnancy>();
	selectedOption$ = this.selectedOptionSource.asObservable();

	updateSelectedOption(option: TypeOfPregnancy): void {
		this.selectedOptionSource.next(option);
	}

}
