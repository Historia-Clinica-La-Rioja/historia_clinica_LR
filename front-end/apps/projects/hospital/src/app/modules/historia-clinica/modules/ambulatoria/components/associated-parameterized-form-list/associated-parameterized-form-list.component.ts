import { Component, Input } from '@angular/core';
import { AssociatedParameterizedFormInformation } from '../associated-parameterized-form-information/associated-parameterized-form-information.component';

@Component({
	selector: 'app-associated-parameterized-form-list',
	templateUrl: './associated-parameterized-form-list.component.html',
	styleUrls: ['./associated-parameterized-form-list.component.scss']
})
export class AssociatedParameterizedFormListComponent {

	@Input() parameterizedFormList: AssociatedParameterizedFormInformation[];

	constructor() { }

}
