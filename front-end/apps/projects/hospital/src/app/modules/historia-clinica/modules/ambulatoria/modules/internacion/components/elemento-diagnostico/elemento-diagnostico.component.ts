import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiagnosisDto } from '@api-rest/api-model';
import { DiagnosisCreationEditionComponent } from '../../dialogs/diagnosis-creation-edition/diagnosis-creation-edition.component';

@Component({
	selector: 'app-elemento-diagnostico',
	templateUrl: './elemento-diagnostico.component.html',
	styleUrls: ['./elemento-diagnostico.component.scss']
})
export class ElementoDiagnosticoComponent implements OnInit {

	@Input()
	diagnosis: DiagnosisDto;

	@Input()
	isMain: boolean;

	constructor(public dialog: MatDialog) { }

  	ngOnInit(): void {
  	}

	openDiagnosisEditionDialog() {
		const dialogRef = this.dialog.open(DiagnosisCreationEditionComponent, {
			width: '450px',
			data: {
				type: 'EDITION',
				diagnosis: this.diagnosis
			}
		});
	}

}
