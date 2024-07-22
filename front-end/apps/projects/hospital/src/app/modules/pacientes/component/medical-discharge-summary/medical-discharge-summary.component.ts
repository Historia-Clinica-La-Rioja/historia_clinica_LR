import { Component, Input, OnInit } from '@angular/core';
import { VMedicalDischargeDto } from '@api-rest/api-model';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { EmergencyCareEpisodeMedicalDischargeService } from '@api-rest/services/emergency-care-episode-medical-discharge.service';
import { TranslateService } from '@ngx-translate/core';
import { DischargeTypes } from '@api-rest/masterdata';
import { REGISTER_EDITOR_CASES, RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
import { EstadosEpisodio } from '@historia-clinica/modules/guardia/constants/masterdata';

@Component({
  selector: 'app-medical-discharge-summary',
  templateUrl: './medical-discharge-summary.component.html',
  styleUrls: ['./medical-discharge-summary.component.scss']
})
export class MedicalDischargeSummaryComponent implements OnInit {

	readonly STATES = EstadosEpisodio;

	@Input() episodeId: number;
	@Input() episodeState: EstadosEpisodio;

	medicalDischargeData: VMedicalDischargeDto;
	problemDescriptionText: string[];
	dischargeTypeDescriptionText: string;

	registerEditor: RegisterEditor;
	registerEditorCasesDateHour = REGISTER_EDITOR_CASES.DATE_HOUR;

	constructor(
		private readonly emergencyCareEpisodeMedicalDischargeService: EmergencyCareEpisodeMedicalDischargeService,
		private readonly translate: TranslateService,
	) { }

	ngOnInit() {
		this.loadPatientDischarge();
	}

	private loadPatientDischarge(){
		this.emergencyCareEpisodeMedicalDischargeService.getMedicalDischarge(this.episodeId)
			.subscribe((data) => {
				this.medicalDischargeData = data;
				this.problemDescriptionText = data.snomedPtProblems;
				this.dischargeTypeDescriptionText = this.getDischargeTypeDescriptionText(data);
				this.registerEditor = {
					createdBy: `${data.medicalDischargeProfessionalName} ${data.medicalDischargeProfessionalLastName}`,
					date: dateTimeDtoToDate(data.medicalDischargeOn),
				};
			}
		);
	}

	private getDischargeTypeDescriptionText(data: any): string {
		const { dischargeType, otherDischargeDescription, autopsy } = data;
		const { description, id } = dischargeType;

		if (id === DischargeTypes.OTRO) {
			const otherDischargeTranslation = this.translate.instant('ambulatoria.paciente.guardia.PATIENT_DISCHARGE.DISCHARGE_TYPE_OTHER');
			return `${otherDischargeDescription} ${otherDischargeTranslation}`;
		}

		if (id === DischargeTypes.DEFUNCION) {
			const deceasedTranslation = this.translate.instant('ambulatoria.paciente.guardia.PATIENT_DISCHARGE.DECEASED');
			const autopsyTranslation = autopsy
			? this.translate.instant('ambulatoria.paciente.guardia.PATIENT_DISCHARGE.AUTOPSY_REQUESTED')
			: this.translate.instant('ambulatoria.paciente.guardia.PATIENT_DISCHARGE.AUTOPSY_NOT_REQUESTED');

			return `${deceasedTranslation}${autopsyTranslation}`;
		}

		return description;
	}

}
