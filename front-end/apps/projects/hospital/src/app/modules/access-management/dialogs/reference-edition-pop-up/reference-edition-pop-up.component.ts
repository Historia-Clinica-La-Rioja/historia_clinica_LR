import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
	selector: 'app-reference-edition-pop-up',
	templateUrl: './reference-edition-pop-up.component.html',
	styleUrls: ['./reference-edition-pop-up.component.scss']
})
export class ReferenceEditionPopUpComponent {

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: ReferenceEditionData,
	) { }

}

interface ReferenceEditionData {
	referenceId: number
}
