import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BasicPatientDto, DateDto, PersonPhotoDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { AdditionalInfo } from '@pacientes/pacientes.model';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { MapperService } from '@presentation/services/mapper.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { momentToDateDto } from '@core/utils/moment.utils';
import * as moment from 'moment';
import { EncounterTypes } from '../../constants/print-ambulatoria-masterdata';

@Component({
	selector: 'app-print-ambulatoria',
	templateUrl: './print-ambulatoria.component.html',
	styleUrls: ['./print-ambulatoria.component.scss']
})
export class PrintAmbulatoriaComponent implements OnInit {

	patient: PatientBasicData;
	patientId: number;
	personInformation: AdditionalInfo[] = [];
	personPhoto: PersonPhotoDto;

	dateRange: {
		start: DateDto,
		end: DateDto,
	}

	maxDate = moment();

	dateRangeForm = new FormGroup({
		start: new FormControl(null, Validators.required),
		end: new FormControl(null, Validators.required),
	});

	encounterTypeForm: FormGroup;
	encounterTypes = EncounterTypes;

	constructor(
		private readonly route: ActivatedRoute,
		private readonly patientService: PatientService,
		private readonly mapperService: MapperService,
		private readonly formBuilder: FormBuilder,
	) {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				this.patientService.getPatientBasicData<BasicPatientDto>(this.patientId).subscribe(
					patient => {
						this.personInformation.push({ description: patient.person.identificationType, data: patient.person.identificationNumber });
						this.patient = this.mapperService.toPatientBasicData(patient);
					}
				);
				this.patientService.getPatientPhoto(this.patientId)
					.subscribe((personPhotoDto: PersonPhotoDto) => { this.personPhoto = personPhotoDto; });
			}
		);
	}

	ngOnInit(): void {
		this.dateRangeForm.valueChanges.subscribe(range => {
			if (range.start <= range.end)
				this.dateRangeChange(range)
		});

		const encounterTypeControls = {};
		this.encounterTypes.forEach(encounterType => {
		  encounterTypeControls[encounterType.value] = this.formBuilder.control(true);
		});

		this.encounterTypeForm = this.formBuilder.group(encounterTypeControls,{ validators: this.atLeastOneChecked });
	}

	dateRangeChange(range): void {
		this.dateRange = {
			start: momentToDateDto(range.start),
			end: momentToDateDto(range.end)
		}
	}

	private atLeastOneChecked(formGroup: FormGroup) {
		const values = Object.values(formGroup.value);
		const isChecked = values.some((value) => value);
		return isChecked ? null : { atLeastOneChecked: true };
	}
}
