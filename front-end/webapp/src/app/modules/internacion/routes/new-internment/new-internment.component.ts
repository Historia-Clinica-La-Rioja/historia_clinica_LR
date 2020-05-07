import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { InternacionMasterDataService } from "@api-rest/services/internacion-master-data.service";
import { Observable } from "rxjs";
import { map, startWith } from "rxjs/operators";
import { SectorService } from "@api-rest/services/sector.service";
import { ClinicalSpecialtySectorService } from "@api-rest/services/clinical-specialty-sector.service";
import { RoomService } from "@api-rest/services/room.service";
import { HealthcareProfessionalService } from "@api-rest/services/healthcare-professional.service";
import {
	CompletePatientDto,
	HealthcareProfessionalDto,
	InternmentEpisodeDto,
	PersonalInformationDto
} from "@api-rest/api-model";
import { PatientService } from "@api-rest/services/patient.service";
import { PatientBasicData } from "@presentation/components/patient-card/patient-card.component";
import { PersonalInformation } from "@presentation/components/personal-information/personal-information.component";
import { PatientTypeData } from "@presentation/components/patient-type-logo/patient-type-logo.component";
import { MapperService } from "@presentation/services/mapper.service";
import { PersonService } from "@api-rest/services/person.service";
import { InternmentEpisodeService } from "@api-rest/services/internment-episode.service";

const INSTITUTION_ID = 10;

@Component({
	selector: 'app-new-internment',
	templateUrl: './new-internment.component.html',
	styleUrls: ['./new-internment.component.scss']
})
export class NewInternmentComponent implements OnInit {

	public form: FormGroup;
	public specialties;
	public sectors;
	public services;
	public rooms;
	public beds;
	public doctors: HealthcareProfessionalDto[];
	public patientId: number;

	public patientBasicData: PatientBasicData;
	public personalInformation: PersonalInformation;
	public patientTypeData: PatientTypeData;


	constructor(private formBuilder: FormBuilder,
				private router: Router,
				private internacionMasterDataService: InternacionMasterDataService,
				private sector: SectorService,
				private clinicalSpecialtySectorService: ClinicalSpecialtySectorService,
				private room: RoomService,
				private healthcareProfessionalService: HealthcareProfessionalService,
				private patientService: PatientService,
				private personService: PersonService,
				private mapperService: MapperService,
				private route: ActivatedRoute,
				private internmentEpisodeService: InternmentEpisodeService) {
	}

	ngOnInit(): void {

		this.route.queryParams.subscribe(params => {
			this.patientId = Number(params['patientId']);
			this.patientService.getPatientCompleteData<CompletePatientDto>(this.patientId)
				.subscribe(completeData => {
					this.patientTypeData = this.mapperService.toPatientTypeData(completeData.patientType);
					this.patientBasicData = this.mapperService.toPatientBasicData(completeData);
					this.personService.getPersonalInformation<PersonalInformationDto>(completeData.person.id)
						.subscribe(personInformationData => {
							this.personalInformation =
								this.mapperService.toPersonalInformationData(completeData, personInformationData);
						});
				});
		});

		this.form = this.formBuilder.group({
			specialtyId: [null, [Validators.required]],
			sectorId: [null, [Validators.required]],
			serviceId: [{value: null, disabled: true}, [Validators.required]],
			roomId: [{value: null, disabled: true}, [Validators.required]],
			bedId: [{value: null, disabled: true}, [Validators.required]],
			doctorId: [null, [Validators.required]],
		});

		this.internacionMasterDataService.getClinicalSpecialty().subscribe(data => {
			this.specialties = data;
		});

		this.sector.getAll().subscribe(data => {
			this.sectors = data
		});

		this.healthcareProfessionalService.getAllDoctors(INSTITUTION_ID).subscribe(data => {
			this.doctors = data
		});

	}

	getDescriptionText(option) {
		return option.description;
	}

	setService() {
		let sectorId: number = this.form.controls.sector.value;
		this.clinicalSpecialtySectorService.getClinicalSpecialty(sectorId).subscribe(data => {
			this.services = data
		});
		this.form.controls.service.enable();
	}

	setRoom() {
		let sectorId: number = this.form.controls.sector.value;
		let serviceId: number = this.form.controls.service.value;
		this.sector.getAllRoomsBySectorAndSpecialty(sectorId, serviceId).subscribe(data => {
			this.rooms = data
		});
		this.form.controls.room.enable();
	}

	setBeds() {
		let roomId: number = this.form.controls.room.value;
		this.room.getAllBedsByRoom(roomId).subscribe(data => {
			this.beds = data
		})
		this.form.controls.bed.enable();
	}

	save(): void {
		console.log(this.form.value);
		let intenmentEpisodeReq = this.mapToPersonInternmentEpisodeRequest();
		if (this.form.valid) {
			this.internmentEpisodeService.setNewInternmentEpisode(INSTITUTION_ID, intenmentEpisodeReq).subscribe();
		}
	}

	private mapToPersonInternmentEpisodeRequest() {
		return {
			patientId: this.patientId,
			bedId: this.form.controls.bedId.value,
			clinicalSpecialtyId: this.form.controls.bedId.value
		}
	}

}
