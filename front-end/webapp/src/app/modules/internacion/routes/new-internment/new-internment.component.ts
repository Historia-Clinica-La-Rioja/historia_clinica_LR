import { Component, ElementRef, OnInit } from '@angular/core';
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
import { scrollIntoError } from "@core/utils/form.utils";
import { ContextService } from "@core/services/context.service";

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
				private el: ElementRef,
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

		this.healthcareProfessionalService.getAllDoctors().subscribe(data => {
			this.doctors = data
		});

	}

	getDescriptionText(option) {
		return option.description;
	}

	setService() {
		let sectorId: number = this.form.controls.sectorId.value;
		this.clinicalSpecialtySectorService.getClinicalSpecialty(sectorId).subscribe(data => {
			console.log('getClinicalSpecialty', data);
			this.services = data;
		});
		this.form.controls.serviceId.enable();
	}

	setRoom() {
		let sectorId: number = this.form.controls.sectorId.value;
		let serviceId: number = this.form.controls.serviceId.value;
		this.sector.getAllRoomsBySectorAndSpecialty(sectorId, serviceId).subscribe(data => {
			this.rooms = data;
		});
		this.form.controls.roomId.enable();
	}

	setBeds() {
		let roomId: number = this.form.controls.roomId.value;
		this.room.getAllBedsByRoom(roomId).subscribe(data => {
			this.beds = data
		})
		this.form.controls.bedId.enable();
	}

	save(): void {
		let intenmentEpisodeReq = this.mapToPersonInternmentEpisodeRequest();
		if (this.form.valid) {
			this.internmentEpisodeService.setNewInternmentEpisode(intenmentEpisodeReq)
				.subscribe();
		} else {
			scrollIntoError(this.form, this.el);
		}
	}

	private mapToPersonInternmentEpisodeRequest() {
		return {
			patientId: this.patientId,
			bedId: this.form.controls.bedId.value,
			clinicalSpecialtyId: this.form.controls.specialtyId.value
		}
	}

}
