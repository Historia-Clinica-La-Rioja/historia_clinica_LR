import { Component, Input } from '@angular/core';
import { Position } from '@presentation/components/identifier/identifier.component';
import { REGISTER_EDITOR_CASES, RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
import { IDENTIFIER_CASES } from 'projects/hospital/src/app/modules/hsi-components/identifier-cases/identifier-cases.component';
import { PrescriptionStatus } from '../reference-request-data/reference-request-data.component';

const observations = 'ambulatoria.paciente.ordenes_prescripciones.complete-study-information.OBSERVATIONS';

@Component({
	selector: 'app-complete-study-information',
	templateUrl: './complete-study-information.component.html',
	styleUrls: ['./complete-study-information.component.scss']
})
export class CompleteStudyInformationComponent {
	observations = observations;
	registerEditorCasesDateHour = REGISTER_EDITOR_CASES.DATE_HOUR;
	position = Position;
	identiferCases = IDENTIFIER_CASES;
	@Input() order: number;
	@Input() descriptionState: string;
	@Input() titleContent: string;
	@Input() registerEditor: RegisterEditor;
	@Input() status: PrescriptionStatus;

}

export class StudyData {

	prescriptionStatus: string;
	prescriptionPt: string;
	problemPt: string;
	registerEditor: RegisterEditor;
    observations?: string;
}
