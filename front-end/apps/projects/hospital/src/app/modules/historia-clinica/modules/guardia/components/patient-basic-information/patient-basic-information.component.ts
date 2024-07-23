import { Component, Input, OnInit } from '@angular/core';
import { BasicPatientDto, PersonPhotoDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { PatientSummary } from '@hsi-components/patient-summary/patient-summary.component';
import { Observable, forkJoin } from 'rxjs';

@Component({
    selector: 'app-patient-basic-information',
    templateUrl: './patient-basic-information.component.html',
    styleUrls: ['./patient-basic-information.component.scss']
})
export class PatientBasicInformationComponent implements OnInit {

	patientSummary: PatientSummary;

    @Input() patientId: number;
    @Input() patientDescription: string;

    constructor(
        private readonly patientService: PatientService,
		private readonly patientNameService: PatientNameService
    ) { }

    ngOnInit(): void {
		if (this.patientId) this.setPatientInfo();
    }

    setPatientInfo() {
		const patientBasicData$: Observable<BasicPatientDto> = this.patientService.getPatientBasicData(this.patientId);
		const patientPhoto$: Observable<PersonPhotoDto> = this.patientService.getPatientPhoto(this.patientId);
		forkJoin([patientBasicData$, patientPhoto$]).subscribe(([patientBasicData, patientPhoto]) => {
			this.patientSummary = toPatientSummary(patientBasicData, patientPhoto, this.patientNameService);
		});
	}

}

export function toPatientSummary(basicData: BasicPatientDto, photo: PersonPhotoDto, patientNameService: PatientNameService): PatientSummary {
	const { firstName, nameSelfDetermination, lastName, middleNames, otherLastNames } = basicData.person;
	return {
		fullName: patientNameService.completeName(firstName, nameSelfDetermination, lastName, middleNames, otherLastNames),
		...(basicData.identificationType && {
			identification: {
				type: basicData.identificationType,
				number: basicData.identificationNumber
			}
		}),
		id: basicData.id,
		gender: basicData.person.gender?.description || null,
		age: basicData.person.age || null,
		photo: photo.imageData
	}
}
