import { Component, EventEmitter, Input, OnDestroy, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { hasError, updateControlValidator } from '@core/utils/form.utils';
import { Observable, Subscription } from 'rxjs';
import { FormOption, Areas, Establishments, InternmentIndication, MunicipalDevices, ProvincialDevices, NationalDevices, OrganizationsExtended, Organizations, Complaints, Devices, ImplementedActions, BasicTwoOptions } from '../../constants/violence-masterdata';
import { EHealthInstitutionOrganization, EHealthSystemOrganization, EInstitutionReportPlace, EInstitutionReportReason, EIntermentIndicationStatus, EMunicipalGovernmentDevice, ENationalGovernmentDevice, EProvincialGovernmentDevice, ESexualViolenceAction, ViolenceReportDto, ViolenceReportImplementedActionsDto } from '@api-rest/api-model';
import { ViolenceReportFacadeService } from '@api-rest/services/violence-report-facade.service';

export enum Articulation {
	IN = 'Articulación con otras áreas/organismos del sector salud',
	OUT = 'Articulación con otros organismos fuera del sector salud'
}
@Component({
	selector: 'app-violence-situation-implemented-actions',
	templateUrl: './violence-situation-implemented-actions.component.html',
	styleUrls: ['./violence-situation-implemented-actions.component.scss']
})

export class ViolenceSituationImplementedActionsComponent implements OnInit, OnDestroy {
	@Input() confirmForm: Observable<boolean>;
	@Output() implementedActionsInfo = new EventEmitter<any>();

	form: FormGroup<{
		articulationIn: FormControl<boolean>,
		articulationOut: FormControl<boolean>,
		healthSystemArticulation: FormControl<boolean>,
		area: FormControl<any[]>,
		otherArea: FormControl<string>,
		articulationEstablishment: FormControl<boolean>,
		articulationEstablishmentList: FormControl<any[]>,
		otherArticulationEstablishment: FormControl<string>
		internmentIndication: FormControl<EIntermentIndicationStatus>,
		devices: FormControl<string[]>,
		municipalDevices: FormControl<EMunicipalGovernmentDevice[]>,
		provincialDevices: FormControl<EProvincialGovernmentDevice[]>,
		nationalDevices: FormControl<ENationalGovernmentDevice[]>
		personComplaint: FormControl<boolean>,
		agencyComplaint: FormControl<any[]>,
		isInstitutionComplaint: FormControl<boolean>,
		institutionComplaints: FormControl<string[]>
		institutionComplaintsOrganizations: FormControl<any[]>
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

	selectedImplementedActions: ESexualViolenceAction[] = [];

	establishments = Establishments;

	articulations = Articulation;

	articulationEnum = Articulation;

	intermentIndicationOptions = InternmentIndication;

	establishmentsEnum = Establishments;

	formOption = FormOption;

	areasOptions = Areas;

	areaOther = EHealthSystemOrganization.OTHERS;
	articulationEstablishmentOther = EHealthInstitutionOrganization.OTHERS;
	institutionComplaintsOrganizationsOther = EInstitutionReportPlace.OTHER;

	basicOptions = BasicTwoOptions;
	hasError = hasError;
	violenceSituationSub: Subscription;

	constructor(private readonly violenceSituationFacadeService: ViolenceReportFacadeService) { }

	ngOnInit(): void {
		this.setViolenceSituation();
		this.form = new FormGroup({
			articulationIn: new FormControl(null, Validators.required),
			articulationOut: new FormControl(null),
			healthSystemArticulation: new FormControl(false),
			area: new FormControl([]),
			otherArea: new FormControl(null),
			articulationEstablishment: new FormControl(false),
			articulationEstablishmentList: new FormControl([]),
			otherArticulationEstablishment: new FormControl(null),
			internmentIndication: new FormControl(null),
			devices: new FormControl([]),
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
			} else {
				this.implementedActionsInfo.emit(null);
				this.form.markAllAsTouched();
			}
		}
	}

	ngOnDestroy(): void {
		this.violenceSituationSub.unsubscribe();
	}

	mapImplementedActionsDto(): ViolenceReportImplementedActionsDto {
		return {
			healthCoordination: {
				coordinationInsideHealthSector: this.form.value.articulationIn ? {
					healthInstitutionOrganization: {
						organizations: this.form.value.articulationEstablishmentList?.map(articulation=> articulation.value),
						other: this.form.value.otherArticulationEstablishment,
						within: this.form.value.articulationEstablishment,
					},
					healthSystemOrganization: {
						organizations: this.form.value.area?.map(a => a.value),
						other: this.form.value.otherArea,
						within: this.form.value.healthSystemArticulation,
					},
					wereInternmentIndicated: this.form.value.internmentIndication

				} : null,
				coordinationOutsideHealthSector: this.form.value.articulationOut ? {
					municipalGovernmentDevices: this.form.value.municipalDevices,
					nationalGovernmentDevices: this.form.value.nationalDevices,
					provincialGovernmentDevices: this.form.value.provincialDevices,
					withOtherSocialOrganizations: this.selectedDevices.includes(this.devicesEnum.SOCIAL_ORGANIZATION),
				} : null
			},
			institutionReport: {
				institutionReportPlaces: this.form.value.institutionComplaintsOrganizations?.map(institution=> institution.value),
				otherInstitutionReportPlace: this.form.value.autorityName,
				reportReasons: this.selectedComplains,
				reportWasDoneByInstitution: this.form.value.isInstitutionComplaint,
			},
			sexualViolence: {
				implementedActions: this.selectedImplementedActions,
				wasSexualViolence: this.form.value.isSexualViolence,
			},
			victimKeeperReport: {
				reportPlaces: this.form.value.agencyComplaint?.map(agency => agency.value),
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

	setMunicipalDevices(mdevs: [string]) {
		this.updateDevicesForm(
			mdevs,
			this.form.controls.municipalDevices
		);
	}

	setProvincialDevices(pdevs: [string]) {
		this.updateDevicesForm(
			pdevs,
			this.form.controls.provincialDevices
		);
	}

	setNationalDevices(ndevs: [string]) {
		this.updateDevicesForm(
			ndevs,
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

	private updateDevicesForm(newDevices: [string], formControl: FormControl) {
		newDevices.map(value => {
			this.setDevicesValidators(value, true);
		})
		formControl.setValue(newDevices);
	}

	private pushOrRemove(value: string, array: string[], formControl: FormControl) {
		if (array.includes(value)) {
			array.splice(array.indexOf(value), 1);
			this.setDevicesValidators(value, false);
		}
		else {
			array.push(value);
			this.setDevicesValidators(value, true);
		}
		
		formControl.setValue(array);
	}

	private setDevicesValidators(value: string, required: boolean) {
		if (value === Devices.MUNICIPAL_DEVICES) {
			if (required) {
				updateControlValidator(this.form, 'municipalDevices', Validators.required);
			} else {
				this.form.controls.municipalDevices.reset();
				updateControlValidator(this.form, 'municipalDevices', []);
			}
		}

		if (value === Devices.PROVINCIAL_DEVICES) {
			if (required) {
				updateControlValidator(this.form, 'provincialDevices', Validators.required);

			} else {
				this.form.controls.provincialDevices.reset();
				updateControlValidator(this.form, 'provincialDevices', []);

			}
		}

		if (value === Devices.NATIONAL_DEVICES) {
			if (required) {
				updateControlValidator(this.form, 'nationalDevices', Validators.required);
			} else {
				this.form.controls.nationalDevices.reset();
				updateControlValidator(this.form, 'nationalDevices', []);
			}
		}
	}



	get area() {
		return this.form.value.area?.map(a => a.value);
	}

	get healthSystemArticulation() {
		return this.form.value.healthSystemArticulation;
	}

	get articulationEstablishment() {
		return this.form.value.articulationEstablishment;
	}

	get articulationEstablishmentList() {
		return this.form.value.articulationEstablishmentList?.map(articulation=> articulation.value);
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
		return this.form.value.institutionComplaintsOrganizations?.map(institution=> institution.value);
	}

	get isSexualViolence() {
		return this.form.value.isSexualViolence;
	}

	updateValidationArea() {
		if (this.form.value.healthSystemArticulation) {
			updateControlValidator(this.form, 'area', Validators.required);
		} else {
			updateControlValidator(this.form, 'area', []);
			this.form.controls.area.reset();
		}
	}

	updateValidationArticulationEstablishment() {
		if (this.form.value.articulationEstablishment) {
			updateControlValidator(this.form, 'articulationEstablishmentList', Validators.required);
		} else {
			updateControlValidator(this.form, 'articulationEstablishmentList', []);
			this.form.controls.articulationEstablishmentList.reset();
		}
	}

	updateValidationAgencyComplaint() {
		if (this.form.value.personComplaint) {
			updateControlValidator(this.form, 'agencyComplaint', Validators.required);
		} else {
			updateControlValidator(this.form, 'agencyComplaint', []);
			this.form.controls.agencyComplaint.reset();
		}
	}

	updateValidationInstitutionComplaint() {
		if (this.form.value.isInstitutionComplaint) {
			updateControlValidator(this.form, 'institutionComplaintsOrganizations', Validators.required);
			updateControlValidator(this.form, 'institutionComplaints', Validators.required);
		} else {
			updateControlValidator(this.form, 'institutionComplaintsOrganizations', []);
			updateControlValidator(this.form, 'institutionComplaints', []);
			updateControlValidator(this.form, 'autorityName', []);
			this.form.controls.institutionComplaintsOrganizations.reset();
			this.form.controls.institutionComplaints.reset();
			this.form.controls.autorityName.reset();
			this.selectedComplains = [];
		}
	}

	updateValidationOtherArea() {
		if (this.form.controls.area.value.map(a => a.value).includes(this.areaOther)) {
			updateControlValidator(this.form, 'otherArea', Validators.required);
		} else {
			updateControlValidator(this.form, 'otherArea', []);
			this.form.controls.otherArea.reset();
		}
	}

	updateValidationArticulationEstablishmentOther() {
		if (this.form.value.articulationEstablishmentList.map(articulation=> articulation.value).includes(this.articulationEstablishmentOther)) {
			updateControlValidator(this.form, 'otherArticulationEstablishment', Validators.required);
		} else {
			updateControlValidator(this.form, 'otherArticulationEstablishment', []);
			this.form.controls.otherArticulationEstablishment.reset();
		}
	}

	updateValidationAutorityName() {
		if (this.form.value.institutionComplaintsOrganizations.map(institution=> institution.value).includes(this.institutionComplaintsOrganizationsOther)) {
			updateControlValidator(this.form, 'autorityName', [Validators.required, Validators.maxLength(20)]);
		} else {
			updateControlValidator(this.form, 'autorityName', []);
		}
	}

	updateValidationArticulations() {
		if (this.form.value.articulationIn) {
			updateControlValidator(this.form, 'articulationIn',Validators.required);
			updateControlValidator(this.form, 'healthSystemArticulation', Validators.required);
			updateControlValidator(this.form, 'articulationEstablishment', Validators.required);
		}else{
			this.resetFirstArticulation();
			updateControlValidator(this.form, 'articulationIn', []);
		}
		if(this.form.value.articulationOut){
			updateControlValidator(this.form, 'articulationOut',Validators.required);
			updateControlValidator(this.form, 'devices', Validators.required);
		} else {
			this.resetDevices();
			updateControlValidator(this.form, 'articulationOut', []);
		}
		this.updateValidationArea();
	}

 	private resetFirstArticulation() {
		this.form.controls.healthSystemArticulation.setValue(false);
		this.form.controls.articulationEstablishment.setValue(false);
		this.form.controls.internmentIndication.reset();
		this.form.controls.area.reset();
		this.form.controls.otherArea.reset();
		this.form.controls.otherArticulationEstablishment.reset();
		this.form.controls.articulationEstablishmentList.reset();
	} 

 	private resetDevices() {
		this.selectedDevices = [];
		this.form.controls.devices.setValue(this.selectedDevices);
		this.form.controls.municipalDevices.setValue([]);
		this.form.controls.nationalDevices.setValue([]);
		this.form.controls.provincialDevices.setValue([]);
	} 

	updateValidationSexualViolence() {
		if (this.form.value.isSexualViolence) {
			updateControlValidator(this.form, 'implementedActions', Validators.required);
		} else {
			updateControlValidator(this.form, 'implementedActions', []);
			this.selectedImplementedActions = [];
			this.form.controls.implementedActions.reset();
		}
	}

	private setViolenceSituation() {
		this.violenceSituationSub = this.violenceSituationFacadeService.violenceSituation$
			.subscribe((result: ViolenceReportDto) => {
				const {implementedActions} = result;
				if (implementedActions.healthCoordination?.coordinationInsideHealthSector)
					this.setCoordinationInsideHealthSector(implementedActions);

				if (implementedActions.healthCoordination?.coordinationOutsideHealthSector)
					this.setCoordinationOutsideHealthSector(implementedActions);

				this.form.controls.personComplaint.setValue(implementedActions.victimKeeperReport.werePreviousEpisodesWithVictimOrKeeper);
				const places = implementedActions.victimKeeperReport.reportPlaces?.flatMap(org => Organizations.find(aopt => aopt.value === org));
				this.form.controls.agencyComplaint.setValue(places);
				this.updateValidationAgencyComplaint();
				this.form.controls.isInstitutionComplaint.setValue(implementedActions.institutionReport.reportWasDoneByInstitution);
				this.form.controls.institutionComplaints.setValue(implementedActions.institutionReport.reportReasons?.length ? implementedActions.institutionReport.reportReasons: []);
				this.selectedComplains = implementedActions.institutionReport.reportReasons?.length ? implementedActions.institutionReport.reportReasons: [];
				const institutions = implementedActions.institutionReport.institutionReportPlaces?.flatMap(ins => OrganizationsExtended.find(org => org.value === ins));
				this.form.controls.institutionComplaintsOrganizations.setValue(institutions);
				this.updateValidationInstitutionComplaint();
				this.form.controls.autorityName.setValue(implementedActions.institutionReport.otherInstitutionReportPlace);
				this.form.controls.isSexualViolence.setValue(implementedActions.sexualViolence.wasSexualViolence);
				this.form.controls.implementedActions.setValue(implementedActions.sexualViolence.implementedActions);
				this.selectedImplementedActions = implementedActions.sexualViolence.implementedActions?.length ? implementedActions.sexualViolence.implementedActions: [];
				this.updateValidationArticulations();
			});
	}

	private setCoordinationInsideHealthSector(implementedActions: ViolenceReportImplementedActionsDto) {
		const {coordinationInsideHealthSector} = implementedActions.healthCoordination;
		this.form.controls.articulationIn.setValue(true);
		this.form.controls.healthSystemArticulation.setValue(coordinationInsideHealthSector.healthSystemOrganization.within);
		if (coordinationInsideHealthSector.healthSystemOrganization.organizations?.length) {
			const orgs = coordinationInsideHealthSector.healthSystemOrganization.organizations.flatMap(org => Areas.find(aopt => aopt.value === org));
			this.form.controls.area.setValue(orgs);
			this.updateValidationArea();
			this.form.controls.otherArea.setValue(coordinationInsideHealthSector.healthSystemOrganization.other);
		}

		if (coordinationInsideHealthSector.healthInstitutionOrganization.organizations?.length) {
			this.form.controls.articulationEstablishment.setValue(coordinationInsideHealthSector.healthInstitutionOrganization.within);
			const est = coordinationInsideHealthSector.healthInstitutionOrganization.organizations.flatMap(e => Establishments.find(es => es.value === e));
			this.form.controls.articulationEstablishmentList.setValue(est);
			this.updateValidationArticulationEstablishment();
			this.form.controls.otherArticulationEstablishment.setValue(coordinationInsideHealthSector.healthInstitutionOrganization.other);
		}
		this.form.controls.internmentIndication.setValue(coordinationInsideHealthSector.wereInternmentIndicated);
	}

	private setCoordinationOutsideHealthSector(implementedActions: ViolenceReportImplementedActionsDto) {
		const {municipalGovernmentDevices} = implementedActions.healthCoordination.coordinationOutsideHealthSector;
		const {provincialGovernmentDevices} = implementedActions.healthCoordination.coordinationOutsideHealthSector;
		const {nationalGovernmentDevices} = implementedActions.healthCoordination.coordinationOutsideHealthSector;
		this.form.controls.articulationOut.setValue(true);
		this.form.controls.municipalDevices.setValue(municipalGovernmentDevices);
		this.prechargeMunicipalDevices(municipalGovernmentDevices);
		this.prechargeProvincialDevices(provincialGovernmentDevices);
		this.prechargeNationalDevices(nationalGovernmentDevices);
		if (implementedActions.healthCoordination.coordinationOutsideHealthSector.withOtherSocialOrganizations)
			this.selectedDevices.push(this.devicesEnum.SOCIAL_ORGANIZATION);
	}

	private prechargeMunicipalDevices(municipalGovernmentDevices: EMunicipalGovernmentDevice[]) {
		this.form.controls.municipalDevices.setValue(municipalGovernmentDevices);
		if (municipalGovernmentDevices?.length) {
			this.devices.push(this.devicesEnum.MUNICIPAL_DEVICES);
			this.selectedDevices.push(this.devicesEnum.MUNICIPAL_DEVICES);
			this.form.controls.municipalDevices.setValue(municipalGovernmentDevices);
		}
	}

	private prechargeProvincialDevices(provincialGovernmentDevices: EProvincialGovernmentDevice[]) {
		this.form.controls.provincialDevices.setValue(provincialGovernmentDevices);
		if (provincialGovernmentDevices?.length) {
			this.devices.push(this.devicesEnum.PROVINCIAL_DEVICES);
			this.selectedDevices.push(this.devicesEnum.PROVINCIAL_DEVICES);
			this.form.controls.provincialDevices.setValue(provincialGovernmentDevices);
		}
	}

	private prechargeNationalDevices(nationalGovernmentDevices: ENationalGovernmentDevice[]) {
		this.form.controls.nationalDevices.setValue(nationalGovernmentDevices);
		if (nationalGovernmentDevices?.length) {
			this.devices.push(this.devicesEnum.NATIONAL_DEVICES);
			this.selectedDevices.push(this.devicesEnum.NATIONAL_DEVICES);
			this.form.controls.nationalDevices.setValue(nationalGovernmentDevices);
		}
	}
}