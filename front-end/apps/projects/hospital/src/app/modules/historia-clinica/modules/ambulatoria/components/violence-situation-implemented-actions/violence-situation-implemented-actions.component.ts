import { Component, EventEmitter, Input, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { hasError, updateControlValidator } from '@core/utils/form.utils';
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

	areaOther = EHealthSystemOrganization.OTHERS;
	articulationEstablishmentOther = EHealthInstitutionOrganization.OTHERS;
	institutionComplaintsOrganizationsOther = EInstitutionReportPlace.OTHER;

	basicOptions = BasicTwoOptions;
	hasError = hasError;

	constructor() { }

	ngOnInit(): void {
		this.form = new FormGroup({
			articulation: new FormControl(null, Validators.required),
			healthSystemArticulation: new FormControl(false),
			area: new FormControl([]),
			otherArea: new FormControl(null),
			articulationEstablishment: new FormControl(false),
			articulationEstablishmentList: new FormControl([]),
			otherArticulationEstablishment: new FormControl(null),
			internmentIndication: new FormControl(null),
			devices: new FormControl([],),
			municipalDevices: new FormControl([]),
			provincialDevices: new FormControl([]),
			nationalDevices: new FormControl([]),
			personComplaint: new FormControl(false, Validators.required),
			agencyComplaint: new FormControl([]),
			isInstitutionComplaint: new FormControl(false, Validators.required),
			institutionComplaints: new FormControl([]),
			institutionComplaintsOrganizations: new FormControl([]),
			autorityName: new FormControl(null),
			isSexualViolence: new FormControl(false, Validators.required),
			implementedActions: new FormControl([]),
		});
	}

	ngOnChanges(changes: SimpleChanges) {
		if (!changes.confirmForm.isFirstChange()) {
			if (this.form.valid) {
				this.implementedActionsInfo.emit(this.mapImplementedActionsDto());
			}else{
				this.form.markAllAsTouched();
			}
		}
	}

	mapImplementedActionsDto(): ViolenceReportImplementedActionsDto {
		return {
			healthCoordination: {
				coordinationInsideHealthSector: {
					healthInstitutionOrganization: {
						organizations: this.form.value.articulationEstablishmentList,
						other: this.form.value.otherArticulationEstablishment,
						within: this.form.value.articulationEstablishment,
					},
					healthSystemOrganization: {
						organizations: this.form.value.area,
						other: this.form.value.otherArea,
						within: this.form.value.healthSystemArticulation,
					},
					wereInternmentIndicated: this.form.value.internmentIndication

				},
				coordinationOutsideHealthSector: {
					municipalGovernmentDevices: this.selectedMunicipalDevices,
					nationalGovernmentDevices: this.selectedNationalDevices,
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

	updateValidationArea() {
		if (this.form.value.healthSystemArticulation) {
			updateControlValidator(this.form, 'area', Validators.required);
		} else {
			updateControlValidator(this.form, 'area', []);
		}
	}

	updateValidationArticulationEstablishment() {
		if (this.form.value.articulationEstablishment) {
			updateControlValidator(this.form, 'articulationEstablishmentList', Validators.required);
		} else {
			updateControlValidator(this.form, 'articulationEstablishmentList', []);
		}
	}

	updateValidationAgencyComplaint() {
		if (this.form.value.personComplaint) {
			updateControlValidator(this.form, 'agencyComplaint', Validators.required);
		} else {
			updateControlValidator(this.form, 'agencyComplaint', []);
		}
	}

	updateValidationInstitutionComplaint() {
		if (this.form.value.isInstitutionComplaint) {
			updateControlValidator(this.form, 'institutionComplaintsOrganizations', Validators.required);
			updateControlValidator(this.form, 'institutionComplaints', Validators.required);
		} else {
			updateControlValidator(this.form, 'institutionComplaintsOrganizations', []);
			updateControlValidator(this.form, 'institutionComplaints', []);
		}
	}

	updateValidationOtherArea() {
		if (this.form.value.area.includes(this.areaOther)) {
			updateControlValidator(this.form, 'otherArea', Validators.required);
		} else {
			updateControlValidator(this.form, 'otherArea', []);
		}
	}

	updateValidationArticulationEstablishmentOther() {
		if (this.articulationEstablishmentList.includes(this.articulationEstablishmentOther)) {
			updateControlValidator(this.form, 'otherArticulationEstablishment', Validators.required);
		} else {
			updateControlValidator(this.form, 'otherArticulationEstablishment', []);
		}
	}

	updateValidationAutorityName() {
		if (this.institutionComplaintsOrganizations.includes(this.institutionComplaintsOrganizationsOther)) {
			updateControlValidator(this.form, 'autorityName', Validators.required);
		} else {
			updateControlValidator(this.form, 'autorityName', []);
		}
	}

	updateValidationArticulations() {
		if (this.form.value.articulation === this.articulationEnum.IN) {
			updateControlValidator(this.form, 'healthSystemArticulation', Validators.required);
			updateControlValidator(this.form, 'articulationEstablishment', Validators.required);
			updateControlValidator(this.form, 'internmentIndication', Validators.required);
			updateControlValidator(this.form, 'devices', []);
			updateControlValidator(this.form, 'municipalDevices', []);
			updateControlValidator(this.form, 'provincialDevices', []);
			updateControlValidator(this.form, 'nationalDevices', []);
		} else {
			updateControlValidator(this.form, 'devices', Validators.required);
			updateControlValidator(this.form, 'healthSystemArticulation', []);
			updateControlValidator(this.form, 'articulationEstablishment', []);
			updateControlValidator(this.form, 'internmentIndication', []);
		}
	}

	updateValidationSexualViolence() {
		if (this.form.value.isSexualViolence) {
			updateControlValidator(this.form, 'implementedActions', Validators.required);
		} else {
			updateControlValidator(this.form, 'implementedActions', []);
		}
	}
}

