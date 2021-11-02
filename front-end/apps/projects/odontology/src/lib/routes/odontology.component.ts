import { Component, OnInit } from '@angular/core';
import { OdontologyService } from '../services/odontology.service';
import { Observable } from 'rxjs';

@Component({
	selector: 'lib-odontology',
	template: `
		<p>
			{{odontologyInfo$ | async}}
		</p>
	`,
	styles: []
})
export class OdontologyComponent {

	odontologyInfo$: Observable<string>;

	constructor(private odontologyService: OdontologyService) {
		this.odontologyInfo$ = this.odontologyService.getInfo();
	}

}
