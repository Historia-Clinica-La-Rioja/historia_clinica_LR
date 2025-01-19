import { Component, Input } from '@angular/core';
import { AppFeature, CompleteDiaryDto, DiaryBookingRestrictionDto, EDiaryBookingRestrictionType, ProfessionalDto, ProfessionalPersonDto, SnomedDto } from '@api-rest/api-model';
import { Position } from '@presentation/components/identifier/identifier.component';
import { IDENTIFIER_CASES } from '../../../hsi-components/identifier-cases/identifier-cases.component';
import { AppointmentsFacadeService } from '@turnos/services/appointments-facade.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { TranslateService } from '@ngx-translate/core';
import { FeatureFlagService } from '@core/services/feature-flag.service';

@Component({
	selector: 'app-diary-information',
	templateUrl: './diary-information.component.html',
	styleUrls: ['./diary-information.component.scss']
})
export class DiaryInformationComponent {

	identiferCases = IDENTIFIER_CASES;
	Position = Position;
	careLinesToShow = "";
	diaryConfigurationDescription = "";
	associatedProfessionalsToShow = "";
	practices: string[] = [];
	diaryDto: CompleteDiaryDto;
	ConfigurationDescription = null
	isHabilitarConfiguracionAgendasOn = false;
	enableConfigurationDescription = false;

	@Input() set diary(diaryInput: CompleteDiaryDto) {
		this.careLinesToShow = "";
		this.associatedProfessionalsToShow = "";
		if (diaryInput) {
			this.diaryDto = diaryInput;
			this.loadInformationToShow();
			this.enableConfigurationDescription = this.isHabilitarConfiguracionAgendasOn && this.diaryDto.bookingRestriction && this.diaryDto.bookingRestriction.restrictionType !== EDiaryBookingRestrictionType.UNRESTRICTED
			if (this.enableConfigurationDescription ) {
				this.ConfigurationDescription = this.getDiaryConfigurationMonthDescription(this.diaryDto.bookingRestriction)
			}
		}
	};

	constructor(
		public readonly appointmentFacade: AppointmentsFacadeService,
		private readonly patientNameService: PatientNameService,
		private readonly translateService: TranslateService,
		private readonly featureFlagService: FeatureFlagService
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_RESTRICCION_CANTIDAD_DIAS_ASIG_TURNOS).subscribe(isOn =>
			this.isHabilitarConfiguracionAgendasOn = isOn
		);
	}

	private getDiaryConfigurationMonthDescription(restriction: DiaryBookingRestrictionDto): string {
		const textResult = restriction.restrictionType === EDiaryBookingRestrictionType.RESTRICTED_BY_CURRENT_MONTH ? this.translateService.instant('turnos.calendar-configuration.MONTH_RESTRICTION')
		: this.getDiaryConfigurationDays(restriction.days)
		return textResult
	}

	private getDiaryConfigurationDays(numberDays: number): string {
		const result = numberDays > 1 ? 'turnos.calendar-configuration.DAYS_RESTRICTION' : 'turnos.calendar-configuration.DAY_RESTRICTION'
		return this.translateService.instant(result, { dia: numberDays });
	}

	private loadInformationToShow() {
		this.practices = this.diaryDto.practicesInfo.map((p: SnomedDto) => p.pt);
		this.loadCareLinesToShow();
		this.loadAssociatedProfessionalsToShow();
	}

	private loadCareLinesToShow() {
		if (this.diaryDto.careLinesInfo.length)
			this.careLinesToShow = this.diaryDto.careLinesInfo.map(careLine => careLine.description).join(', ');
		else
			this.careLinesToShow = '';
	}

	private loadAssociatedProfessionalsToShow() {
		if (this.diaryDto.associatedProfessionalsInfo.length) {
			let professionalsToShow: string[] = [];

			this.appointmentFacade.professional$.subscribe(professional => {
				if (professional) {
					professionalsToShow.push(this.getProfessionalFullName(professional));

					const associatedProfessionalsNames = this.diaryDto.associatedProfessionalsInfo
						.map(associatedProfessional => this.getProfessionalFullName(associatedProfessional));

					professionalsToShow = professionalsToShow.concat(associatedProfessionalsNames);
					this.associatedProfessionalsToShow = professionalsToShow.join(', ');
				}
			});
		} else
			this.associatedProfessionalsToShow = '';
	}

	private getProfessionalFullName(professional: ProfessionalPersonDto | ProfessionalDto): string {
		return `${professional?.lastName} ${professional?.otherLastNames ? professional?.otherLastNames : ''} ${this.patientNameService.getFullName(professional?.firstName, professional?.nameSelfDetermination, professional?.middleNames)}`;
	}

}
