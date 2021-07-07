import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToothDto } from '@api-rest/api-model';
import { ToothTreatment } from '../tooth/tooth.component';

@Component({
	selector: 'app-tooth-dialog',
	templateUrl: './tooth-dialog.component.html',
	styleUrls: ['./tooth-dialog.component.scss']
})
export class ToothDialogComponent implements OnInit {

	constructor(
		private formBuilder: FormBuilder,
		@Inject(MAT_DIALOG_DATA) public data: { tooth: ToothDto, quadrantCode: number }
	) { }

	readonly toothTreatment = ToothTreatment.AS_FRACTIONAL_TOOTH;

	form: FormGroup;
	findings = [{
		id: '399271000221103',
		description: 'Corona'
	},
	{
		id: 'blue_oval',
		description: 'Mancha Blanca'
	},
	{
		id: 4,
		description: 'Sellador'
	},
	{
		id: '699685006',
		description: 'surco oclusal profundo'
	},
	{
		id: 'restos_radiculares',
		description: 'Restos radiculares'
	},
	{
		id: '109564008', // ID PARA HALLAZGOS QUE SE PINTAN DE AZUL
		description: 'caries dental asociada con hipomineralización del esmalte (trastorno)'
	},
	{
		id: '399031000221108',
		description: 'obturación con amalgama cavidad compuesta'
	}
	];

	procedures = [{
		id: '404198007',
		description: 'extracción dentaria simple'
	}, {
		id: '789147006',
		description: 'implante'
	},
	{ // No tiene dibujo, se pinta de rojo
		id: '702645001',
		description: 'incrustacion estética'
	}, {
		id: 8,
		description: 'Sellador'
	}];

	outputProcedures;

	ngOnInit(): void {

		this.form = this.formBuilder.group(
			{
				findingId: [undefined],
				procedures: this.formBuilder.group({
					firstProcedureId: [undefined],
					secondProcedureId: [undefined],
					thirdProcedureId: [undefined],
				})
			}
		);

	}

	confirm() {
	}


}

