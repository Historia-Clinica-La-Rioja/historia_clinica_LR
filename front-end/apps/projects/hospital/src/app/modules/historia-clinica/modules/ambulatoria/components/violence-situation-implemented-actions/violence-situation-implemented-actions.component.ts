import { Component, EventEmitter, Input, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { hasError } from '@core/utils/form.utils';
import { Observable } from 'rxjs';
import { FormOption, Areas, Establishments, InternmentIndication, MunicipalDevices, ProvincialDevices, NationalDevices, OrganizationsExtended, Organizations, Complaints, Devices, ImplementedActions, BasicTwoOptions } from '../../constants/violence-masterdata';
import { EHealthInstitutionOrganization, EHealthSystemOrganization, EInstitutionReportPlace, EInstitutionReportReason, EIntermentIndicationStatus, EMunicipalGovernmentDevice, ENationalGovernmentDevice, EProvincialGovernmentDevice, ESexualViolenceAction, EVictimKeeperReportPlace, ViolenceReportImplementedActionsDto } from '@api-rest/api-model';

enum Articulation {
    IN = 'Articulación con otras áreas/organismos del sector salud',
    OUT = 'Articulación con otros organismos fuera del sector salud'
}
@Component({
	selector: 'app-violence-situation-implemented-actions',
	templateUrl: './violence-situation-implemented-actions.component.html',
  	styleUrls: ['./violence-situation-implemented-actions.component.scss']
})

export class ViolenceSituationImplementedActionsComponent implements OnInit {
	@Input() confirmForm: Observable<boolean>;
	@Output() implementedActionsInfo = new EventEmitter<any>();
	
	form: FormGroup<{
		articulation: FormControl<string>,
		healthSystemArticulation: FormControl<boolean>,
		area: FormControl<EHealthSystemOrganization[]>,
		otherArea: FormControl<string>,
		articulationEstablishment: FormControl<boolean>,
		articulationEstablishmentList: FormControl<EHealthInstitutionOrganization[]>,
		otherArticulationEstablishment: FormControl<string>
		internmentIndication: FormControl<EIntermentIndicationStatus>,
		devices: FormControl<string[]>,
		municipalDevices: FormControl<string[]>,
		provincialDevices: FormControl<string[]>,
		nationalDevices: FormControl<string[]>
		personComplaint: FormControl<boolean>,
		agencyComplaint: FormControl<EVictimKeeperReportPlace[]>,
		isInstitutionComplaint: FormControl<boolean>,
		institutionComplaints: FormControl<string[]>
		institutionComplaintsOrganizations: FormControl<EInstitutionReportPlace[]>
		autorityName: FormControl<string>,
		isSexualViolence: FormControl<boolean>,
		implementedActions: FormControl<string[]>
	}>;

	organizationsExtendedEnum = OrganizationsExtended;

	devicesEnum = Devices;

	selectedComplains: EInstitutionReportReason[] = [];

	implementedActions = ImplementedActions;

	municipalDevicesList = MunicipalDevices; 

	provincialDevicesList = ProvincialDevices;
	
	nationalDevicesList = NationalDevices;

	organizations = Organizations;

	organizationsExtended = OrganizationsExtended;

	complaints = Complaints;

	selectedDevices: Devices[] = [];

	selectedMunicipalDevices: EMunicipalGovernmentDevice[] = [];

	selectedProvincialDevices: EProvincialGovernmentDevice[] = [];

	selectedNationalDevices: ENationalGovernmentDevice[] = [];

	selectedImplementedActions: ESexualViolenceAction[] = [];

	establishments = Establishments;

	articulations: string[] = [Articulation.IN, Articulation.OUT];

	articulationEnum = Articulation;

	intermentIndicationOptions = InternmentIndication;

	establishmentsEnum = Establishments;

	formOption = FormOption;

	areas = Areas;

	areaOther =  EHealthSystemOrganization.OTHERS;
	articulationEstablishmentOther =EHealthSystemOrganization.OTHERS;
	institutionComplaintsOrganizationsOther	=EInstitutionReportPlace.OTHER;

	basicOptions = BasicTwoOptions;
	hasError = hasError;

	constructor() { }

	ngOnInit(): void {
		this.form = new FormGroup({
			articulation: new FormControl(null, Validators.required),
			healthSystemArticulation: new FormControl(false, Validators.required),
			area: new FormControl([], Validators.required),
			otherArea: new FormControl(null, Validators.required),
			articulationEstablishment: new FormControl(false, Validators.required),
			articulationEstablishmentList: new FormControl([], Validators.required),
			otherArticulationEstablishment: new FormControl(null, Validators.required),
			internmentIndication: new FormControl(null, Validators.required),
			devices: new FormControl([], Validators.required),
			municipalDevices: new FormControl([], Validators.required),
			provincialDevices: new FormControl([], Validators.required),
			nationalDevices: new FormControl([], Validators.required),
			personComplaint: new FormControl(false, Validators.required),
			agencyComplaint: new FormControl([], Validators.required),
			isInstitutionComplaint: new FormControl(false, Validators.required),
			institutionComplaints: new FormControl([], Validators.required),
			institutionComplaintsOrganizations: new FormControl([], Validators.required),
			autorityName: new FormControl(null, Validators.required),
			isSexualViolence: new FormControl(false, Validators.required),
			implementedActions: new FormControl([], Validators.required),
		});
	}

	ngOnChanges(changes: SimpleChanges) {
		if(!changes.confirmForm.isFirstChange()){
			this.implementedActionsInfo.emit(this.mapImplementedActionsDto());
		}
	}
	
	mapImplementedActionsDto():ViolenceReportImplementedActionsDto{
		return {
			healthCoordination: {
				coordinationInsideHealthSector:{
					healthInstitutionOrganization:{ 
						organizations: this.form.value.articulationEstablishmentList,
						other: this.form.value.otherArticulationEstablishment ,
						within: this.form.value.articulationEstablishment ,
					},
					healthSystemOrganization:{
						organizations: this.form.value.area ,
						other: this.form.value.otherArea  ,
						within: this.form.value.healthSystemArticulation,
					},
					wereInternmentIndicated: this.form.value.internmentIndication
			
				},
				coordinationOutsideHealthSector:{
					municipalGovernmentDevices: this.selectedMunicipalDevices,
					nationalGovernmentDevices : this.selectedNationalDevices,
					provincialGovernmentDevices: this.selectedProvincialDevices,
					withOtherSocialOrganizations: this.selectedDevices.includes(this.devicesEnum.SOCIAL_ORGANIZATION),
				}
			},
			institutionReport: {
				institutionReportPlaces: this.form.value.institutionComplaintsOrganizations,
				otherInstitutionReportPlace: this.form.value.autorityName,
				reportReasons: this.selectedComplains,
				reportWasDoneByInstitution: this.form.value.isInstitutionComplaint,
			},
			sexualViolence: {
				implementedActions: this.selectedImplementedActions,
				wasSexualViolence: this.form.value.isSexualViolence,
			},
			victimKeeperReport: {
				reportPlaces: this.form.value.agencyComplaint,
				werePreviousEpisodesWithVictimOrKeeper: this.form.value.personComplaint,
			}
		}
	}
	setImplementedActions(iac: string) {
		this.pushOrRemove(
			iac, 
			this.selectedImplementedActions, 
			this.form.controls.implementedActions
		);
	}

	setComplaints(com: string) {
		this.pushOrRemove(
			com, 
			this.selectedComplains, 
			this.form.controls.institutionComplaints
		);
	}

	setMunicipalDevices(mdev: string) {
		this.pushOrRemove(
				mdev, 
				this.selectedMunicipalDevices, 
				this.form.controls.municipalDevices
			);
	}

	setProvincialDevices(pdev: string) {
		this.pushOrRemove(
				pdev, 
				this.selectedProvincialDevices, 
				this.form.controls.provincialDevices
			);
	}

	setNationalDevices(ndev: string) {
		this.pushOrRemove(
				ndev, 
				this.selectedNationalDevices, 
				this.form.controls.nationalDevices
			);
	}

	setDevice(device: string) {
		this.pushOrRemove(
			device, 
			this.selectedDevices, 
			this.form.controls.devices
		);
	}

	private pushOrRemove(value: string, array: string[], formControl: FormControl) {
		if (array.includes(value)) 
			array.splice(array.indexOf(value), 1);
		else 
			array.push(value);
		
		formControl.setValue(array);
	}

	get articulation() {
		return this.form.value.articulation;
	}

	get area() {
		return this.form.value.area;
	}

	get healthSystemArticulation() {
		return this.form.value.healthSystemArticulation;
	}

	get articulationEstablishment() {
		return this.form.value.articulationEstablishment;
	}

	get articulationEstablishmentList() {
		return this.form.value.articulationEstablishmentList;
	}

	get personComplaint() {
		return this.form.value.personComplaint
	}

	get devices() {
		return this.form.value.devices;
	}

	get isInstitutionComplaint() {
		return this.form.value.isInstitutionComplaint;
	}

	get institutionComplaintsOrganizations() {
		return this.form.value.institutionComplaintsOrganizations;
	}

	get isSexualViolence() {
		return this.form.value.isSexualViolence;
	}

}
