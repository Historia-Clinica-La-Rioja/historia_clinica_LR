import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from "@angular/forms";
import { Router } from "@angular/router";
import { InternacionMasterDataService } from "@api-rest/services/internacion-master-data.service";
import { Observable } from "rxjs";
import { map, startWith } from "rxjs/operators";
import { SectorService } from "@api-rest/services/sector.service";
import { ClinicalSpecialtySectorService } from "@api-rest/services/clinical-specialty-sector.service";
import { RoomService } from "@api-rest/services/room.service";
import { HealthcareProfessionalService } from "@api-rest/services/healthcare-professional.service";
import { HealthcareProfessionalDto } from "@api-rest/api-model";

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
	public professionals: HealthcareProfessionalDto[];

	constructor(private formBuilder: FormBuilder,
				private router: Router,
				private internacionMasterDataService: InternacionMasterDataService,
				private sector: SectorService,
				private clinicalSpecialtySectorService: ClinicalSpecialtySectorService,
				private room: RoomService,
				private healthcareProfessionalService: HealthcareProfessionalService) {
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			specialty:[],
			sector:[],
			service:[{ value: null, disabled: true }],
			room:[{ value: null, disabled: true }],
			bed:[{ value: null, disabled: true }],
			professional:[],
		});

		this.internacionMasterDataService.getClinicalSpecialty().subscribe(data => {
			this.specialties = data;
		});

		this.sector.getAll().subscribe(data => { this.sectors = data });

		this.healthcareProfessionalService.getAllDoctors(INSTITUTION_ID).subscribe(data => { this.professionals = data });

	}

	getDescriptionText(option) {
		return option.description;
	}

	setService(){
		let sectorId: number = this.form.controls.sector.value;
		this.clinicalSpecialtySectorService.getClinicalSpecialty(sectorId).subscribe(data => { this.services = data });
		this.form.controls.service.enable();
	}

	setRoom(){
		let sectorId: number = this.form.controls.sector.value;
		let serviceId: number = this.form.controls.service.value;
		this.sector.getAllRoomsBySectorAndSpecialty(sectorId, serviceId).subscribe(data => { this.rooms = data });
		this.form.controls.room.enable();
	}

	setBeds(){
		let roomId: number = this.form.controls.room.value;
		this.room.getAllBedsByRoom(roomId).subscribe(data => { this.beds = data })
		this.form.controls.bed.enable();
	}

	save(): void {
	}


}
