import { Component, Input } from '@angular/core';
import { CompleteDiaryDto, ProfessionalDto, ProfessionalPersonDto, SnomedDto } from '@api-rest/api-model';
import { Position } from '@presentation/components/identifier/identifier.component';
import { IDENTIFIER_CASES } from '../../../hsi-components/identifier-cases/identifier-cases.component';
import { AppointmentsFacadeService } from '@turnos/services/appointments-facade.service';
import { PatientNameService } from '@core/services/patient-name.service';

@Component({
	selector: 'app-diary-information',
	templateUrl: './diary-information.component.html',
	styleUrls: ['./diary-information.component.scss']
})
export class DiaryInformationComponent {

	identiferCases = IDENTIFIER_CASES;
	Position = Position;
	careLinesToShow = "";
	associatedProfessionalsToShow = "";
	practices: string[] = [];
	diaryDto: CompleteDiaryDto;

	@Input() set diary(dto: CompleteDiaryDto) {
		this.careLinesToShow = "";
		this.associatedProfessionalsToShow = "";
		if (dto) {
			this.diaryDto = dto;
			this.loadInformationToShow();
		}
	};

	constructor(
		public readonly appointmentFacade: AppointmentsFacadeService,
		private readonly patientNameService: PatientNameService,
	) { }

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
