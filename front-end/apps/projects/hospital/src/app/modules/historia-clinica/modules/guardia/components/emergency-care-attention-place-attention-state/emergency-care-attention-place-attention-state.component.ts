import { Component, Input, OnInit } from '@angular/core';
import { DateTimeDto, MasterDataDto, ProfessionalPersonDto } from '@api-rest/api-model';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { PatientNameService } from '@core/services/patient-name.service';
import { EmergencyCareStatusLabels } from '@hsi-components/emergency-care-status-labels/emergency-care-status-labels.component';
import { REGISTER_EDITOR_CASES, RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';

@Component({
	selector: 'app-emergency-care-attention-place-attention-state',
	templateUrl: './emergency-care-attention-place-attention-state.component.html',
	styleUrls: ['./emergency-care-attention-place-attention-state.component.scss']
})
export class EmergencyCareAttentionPlaceAttentionStateComponent implements OnInit {

	readonly REGISTER_EDITOR_CASES = REGISTER_EDITOR_CASES;

	registerEditorInfo: RegisterEditor | null = null;
	statusLabel: EmergencyCareStatusLabels;

	private _professional: ProfessionalPersonDto;
	private _date: DateTimeDto;

	@Input() set state(state: MasterDataDto) {
		if (state) {
			this.statusLabel = { stateId: state.id, description: state.description };
		}
	}

	@Input() set professional(professional: ProfessionalPersonDto) {
		if (professional) {
			this._professional = professional;
		}
	}

	@Input() set date(date: DateTimeDto) {
		if (date) {
			this._date = date;
		}
	}

	constructor(private readonly patientNameService: PatientNameService) { }

	ngOnInit() {
		this.updateRegisterEditorInfo();
	}

	private updateRegisterEditorInfo() {
		if (this._professional && this._date) {
			this.registerEditorInfo = {
				createdBy: this.patientNameService.completeName(
					this._professional.firstName,
					this._professional.nameSelfDetermination,
					this._professional.lastName,
					this._professional.middleNames,
					this._professional.otherLastNames
				),
				date: dateTimeDtotoLocalDate(this._date)
			};
		}
	}
}
