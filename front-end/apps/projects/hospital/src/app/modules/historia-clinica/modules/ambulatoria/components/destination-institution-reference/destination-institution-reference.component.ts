import { Component, Input, OnInit } from '@angular/core';
import { UntypedFormGroup } from '@angular/forms';
import { BehaviorSubject, combineLatest, debounceTime, distinctUntilChanged } from 'rxjs';
import { AddressDto, ClinicalSpecialtyDto, InstitutionBasicInfoDto } from '@api-rest/api-model';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { ContextInstitutionService } from '@api-rest/services/context-institution.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { ReferenceOriginInstitutionService } from '../../services/reference-origin-institution.service';
import { DiaryAvailableAppointmentsSearchService } from '@turnos/services/diary-available-appointments-search.service';

@Component({
	selector: 'app-destination-institution-reference',
	templateUrl: './destination-institution-reference.component.html',
	styleUrls: ['./destination-institution-reference.component.scss']
})
export class DestinationInstitutionReferenceComponent implements OnInit {
	@Input() formReference: UntypedFormGroup;
	@Input() submitForm: boolean;

	@Input() set updateFormFields(updateDepartamentsAndInstitution: boolean) {
		if (updateDepartamentsAndInstitution) {
			this.onSpecialtySelectionChange();
		}
	}

	ZERO = 0;
	departments: TypeaheadOption<any>[];
	institutions: TypeaheadOption<any>[];
	defaultDepartment: TypeaheadOption<any>;
	defaultInstitution: TypeaheadOption<any>;
	departmentDisable = true;
	institutionsDisable = true;
	institutionDestinationId: number;
	departmentId: number;
	originInstitutionInfo: AddressDto;
	specialtyIds: number[];
	institutionSelection = false;
	careLineId: number;
	appointment$ = new BehaviorSubject<number>(undefined);
	protectedAppointment$ = new BehaviorSubject<number>(undefined);
	practiceOrProcedure = false;
	practiceSnomedId;
	clinicalSpecialties: ClinicalSpecialtyDto[];

	constructor(
		private readonly contextInstitutionService: ContextInstitutionService,
		private readonly adressMasterData: AddressMasterDataService,
		private readonly referenceOriginInstitutionService: ReferenceOriginInstitutionService,
		private readonly diaryAvailableAppointmentsSearchService: DiaryAvailableAppointmentsSearchService
	) { }

	ngOnInit() {
		this.institutionSelection = false;

		this.referenceOriginInstitutionService.originInstitutionInfo$.subscribe(info => this.originInstitutionInfo = info);

		const practiceOrProcedure$ = this.formReference.controls.practiceOrProcedure.valueChanges.pipe(
			distinctUntilChanged()
		);

		const clinicalSpecialtyId$ = this.formReference.controls.clinicalSpecialties.valueChanges.pipe(
			debounceTime(300),
			distinctUntilChanged()
		);

		const careLine$ = this.formReference.controls.careLine.valueChanges.pipe(
			debounceTime(300),
			distinctUntilChanged()
		);

		combineLatest([practiceOrProcedure$, clinicalSpecialtyId$, careLine$]).subscribe(([practiceOrProcedure, clinicalSpecialties, careLine]) => {
			this.practiceOrProcedure = !!practiceOrProcedure;
			this.cleanFields();
			if (practiceOrProcedure) {
				this.adressMasterData.getDepartmentsByCareLineAndPracticesAndClinicalSpecialty(practiceOrProcedure.id, clinicalSpecialties?.length, careLine?.id).subscribe(data => {
					this.departments = this.toTypeaheadOptions(data, 'description');
					this.departmentDisable = false;
					this.practiceSnomedId = practiceOrProcedure.id;
					this.clinicalSpecialties = clinicalSpecialties;
				});
			} else {
				if (this.formReference.controls.practiceOrProcedure.value) this.practiceSnomedId = this.formReference.controls.practiceOrProcedure.value.id;
				else this.practiceSnomedId = null
			}
		});

		careLine$.subscribe(data => {
			this.specialtyIds = null;
			this.careLineId = data?.id;
			this.setDepartaments();
			this.onSpecialtySelectionChange();
		})

		clinicalSpecialtyId$.subscribe(data => {
			this.specialtyIds = null;
			this.specialtyIds = data?.map(specialty => specialty.id);
			this.setDepartaments();
		});

		this.formReference.controls.problems.valueChanges.subscribe((changes) => {
			this.defaultDepartment = null;
			this.defaultInstitution = null;
			this.onDepartmentSelectionChange(null);
			this.onInstitutionSelectionChange(null);
			this.departmentDisable = true;
			this.institutionsDisable = true;
		});
	}



	onSpecialtySelectionChange() {
		this.defaultDepartment = this.clearTypeahead();
		this.defaultInstitution = this.clearTypeahead();
		this.formReference.controls.departmentId.setValue(null);
		this.formReference.controls.institutionDestinationId.setValue(null);
	}

	onDepartmentSelectionChange(departmentId: number) {
		this.defaultInstitution = this.clearTypeahead();
		if (departmentId) {
			if (this.practiceOrProcedure) {
				this.contextInstitutionService.getInstitutionsByReferenceByPracticeFilter
					(this.formReference.controls.practiceOrProcedure.value.id, departmentId, this.careLineId, this.clinicalSpecialties?.map(clinicalSpecialty => clinicalSpecialty.id)).subscribe((institutions: InstitutionBasicInfoDto[]) => {
						this.institutions = this.toTypeaheadOptions(institutions, 'name');
						this.institutionsDisable = false;
					});
			}
			else {
				this.contextInstitutionService.getInstitutionsByReferenceByClinicalSpecialtyFilter(departmentId, this.specialtyIds, this.careLineId).subscribe((institutions: InstitutionBasicInfoDto[]) => {
					this.institutions = this.toTypeaheadOptions(institutions, 'name');
					this.institutionsDisable = false;
				});
			}
		}
		this.departmentId = departmentId;
		this.formReference.controls.departmentId.setValue(departmentId);
	}

	onInstitutionSelectionChange(institutionDestinationId: number) {
		if (institutionDestinationId) {
			this.setAppointments(institutionDestinationId);
			this.institutionSelection = true;
		} else {
			this.institutionSelection = false;
		}
		this.institutionDestinationId = institutionDestinationId;
		this.formReference.controls.institutionDestinationId.setValue(institutionDestinationId);
	}

	setAppointments(institutionDestinationId: number) {
		if (this.careLineId) {
			this.diaryAvailableAppointmentsSearchService.getAvailableProtectedAppointmentsQuantity(institutionDestinationId, this.specialtyIds, this.departmentId, this.careLineId, this.practiceSnomedId).subscribe(rs =>
				this.protectedAppointment$.next(rs));
			this.diaryAvailableAppointmentsSearchService.getAvailableAppiuntmentsQuantityByCarelineDiaries(institutionDestinationId, this.careLineId, this.practiceSnomedId, this.specialtyIds).subscribe(rs =>
				this.appointment$.next(rs));
		}
		else
			this.diaryAvailableAppointmentsSearchService.getAvailableAppointmentsQuantity(institutionDestinationId, this.specialtyIds, this.practiceSnomedId).subscribe(rs =>
				this.appointment$.next(rs));
	}

	private setDepartaments() {
		this.cleanFields();
		if (this.formReference.value.consultation && this.specialtyIds?.length) {
			this.adressMasterData.getDeparmentsByCareLineAndClinicalSpecialty(this.specialtyIds, this.careLineId).subscribe(e => {
				this.departments = this.toTypeaheadOptions(e, 'description');
				this.departmentDisable = false;
			});
		}
	}

	private cleanFields() {
		this.defaultInstitution = this.clearTypeahead();
		this.defaultDepartment = this.clearTypeahead();
		this.departmentDisable = true;
		this.institutionsDisable = true;
		this.institutionSelection = false;
	}

	private toTypeaheadOptions(response: any[], attribute: string): TypeaheadOption<any>[] {
		return response.map(r => {
			return {
				value: r.id,
				compareValue: r[attribute],
				viewValue: r[attribute]
			}
		})
	}

	private clearTypeahead() {
		return { value: null, viewValue: null, compareValue: null }
	}
}
