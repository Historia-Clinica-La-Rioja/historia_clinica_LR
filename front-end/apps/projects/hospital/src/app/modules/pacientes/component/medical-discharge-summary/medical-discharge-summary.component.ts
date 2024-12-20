import { Component, Input } from '@angular/core';
import { VMedicalDischargeDto } from '@api-rest/api-model';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
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
export class MedicalDischargeSummaryComponent {

	readonly STATES = EstadosEpisodio;

	@Input() set episodeId(value: number) {
		if (value) {
			this.loadPatientDischarge(value);
		}
	}
	@Input() episodeState: EstadosEpisodio;

	medicalDischargeData: VMedicalDischargeDto;
	problemDescriptionText: string[];
	dischargeTypeDescriptionText: string;

	registerEditor: RegisterEditor;
	readonly REGISTER_EDITOR_CASES_DATE_HOUR = REGISTER_EDITOR_CASES.DATE_HOUR;

	constructor(
		private readonly emergencyCareEpisodeMedicalDischargeService: EmergencyCareEpisodeMedicalDischargeService,
		private readonly translate: TranslateService,
	) { }

	private loadPatientDischarge(episodeId: number){
		this.emergencyCareEpisodeMedicalDischargeService.getMedicalDischarge(episodeId)
			.subscribe((data) => {
				this.medicalDischargeData = data;
				this.problemDescriptionText = data.snomedPtProblems;
				this.dischargeTypeDescriptionText = this.getDischargeTypeDescriptionText(data);
				this.registerEditor = {
					createdBy: `${data.medicalDischargeProfessionalName} ${data.medicalDischargeProfessionalLastName}`,
					date: dateTimeDtotoLocalDate(data.medicalDischargeOn),
				};
			}
		);
	}

	private getDischargeTypeDescriptionText(data: any): string {
		const { dischargeType, otherDischargeDescription, autopsy } = data;
		const { description, id } = dischargeType;

		const dischargeTypeMessages = {
		  [DischargeTypes.OTRO]: () => {
			const otherDischargeTranslation = this.translate.instant('ambulatoria.paciente.guardia.PATIENT_DISCHARGE.DISCHARGE_TYPE_OTHER');
			return `${otherDischargeDescription} ${otherDischargeTranslation}`;
		  },
		  [DischargeTypes.DEFUNCION]: () => {
			const deceasedTranslation = this.translate.instant('ambulatoria.paciente.guardia.PATIENT_DISCHARGE.DECEASED');
			const autopsyTranslation = autopsy
				? this.translate.instant('ambulatoria.paciente.guardia.PATIENT_DISCHARGE.AUTOPSY_REQUESTED')
				: this.translate.instant('ambulatoria.paciente.guardia.PATIENT_DISCHARGE.AUTOPSY_NOT_REQUESTED');
			return `${deceasedTranslation} ${autopsyTranslation}`;
		  }
		};

		return dischargeTypeMessages[id] ? dischargeTypeMessages[id]() : description;
	}

}
