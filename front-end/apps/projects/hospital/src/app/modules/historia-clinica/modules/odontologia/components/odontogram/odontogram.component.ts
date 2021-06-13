import { Component, OnInit } from '@angular/core';
import { OdontogramService } from '../../api-rest/odontogram.service';
import { ToothTreatment } from '../tooth/tooth.component';

@Component({
	selector: 'app-odontogram',
	templateUrl: './odontogram.component.html',
	styleUrls: ['./odontogram.component.scss']
})
export class OdontogramComponent implements OnInit {

	constructor(
		private odontogramService: OdontogramService,
	) { }

	readonly toothTreatment = ToothTreatment.AS_WHOLE_TOOTH;

	quadrants;
	ngOnInit(): void {


		this.odontogramService.getOdontogram().subscribe(
			odontogram => {

				const quadrant1 = odontogram.find(q => q.permanent && !q.left && q.top);
				const quadrant2 = odontogram.find(q => q.permanent && q.left && q.top);
				const quadrant5 = odontogram.find(q => !q.permanent && !q.left && q.top);
				const quadrant6 = odontogram.find(q => !q.permanent && q.left && q.top);
				const quadrant8 = odontogram.find(q => !q.permanent && !q.left && !q.top);
				const quadrant7 = odontogram.find(q => !q.permanent && q.left && !q.top);
				const quadrant4 = odontogram.find(q => q.permanent && !q.left && !q.top);
				const quadrant3 = odontogram.find(q => q.permanent && q.left && !q.top);

				this.quadrants = { quadrant1, quadrant2, quadrant3, quadrant4, quadrant5, quadrant6, quadrant7, quadrant8 };
			}
		);
	}

	openToothDialog(toothId) {
		console.log(toothId);
	}


}
