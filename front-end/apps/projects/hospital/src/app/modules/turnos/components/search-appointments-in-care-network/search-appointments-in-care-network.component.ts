import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AddressDto, CareLineDto, ClinicalSpecialtyDto, DepartmentDto, InstitutionDto } from '@api-rest/api-model';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { CareLineService } from '@api-rest/services/care-line.service';
import { InstitutionService } from '@api-rest/services/institution.service';
import { SpecialtyService } from '@api-rest/services/specialty.service';
import { ContextService } from '@core/services/context.service';
import { datePlusDays } from '@core/utils/date.utils';
import { DEFAULT_COUNTRY_ID } from '@core/utils/form.utils';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { Moment } from 'moment';

const PERIOD_DAYS = 7
@Component({
  selector: 'app-search-appointments-in-care-network',
  templateUrl: './search-appointments-in-care-network.component.html',
  styleUrls: ['./search-appointments-in-care-network.component.scss']
})
export class SearchAppointmentsInCareNetworkComponent implements OnInit {

  searchForm: FormGroup;
  provinces = [];
  departments: DepartmentDto[] = [];
  institutions: InstitutionShortInfo[] = [];
  careLines: CareLineDto[] = [];
  specialties: ClinicalSpecialtyDto[] = [];
  allSpecialties: ClinicalSpecialtyDto[] = [];
  readonly today = new Date();

  careLineTypeaheadOptions: TypeaheadOption<CareLineDto>[] = [];
  specialtyTypeaheadOptions: TypeaheadOption<ClinicalSpecialtyDto>[] = [];
  departmentTypeaheadOptions: TypeaheadOption<DepartmentDto>[] = [];
  institutionTypeaheadOptions: TypeaheadOption<InstitutionDto>[] = [];
  provinceTypeaheadOptions: TypeaheadOption<InstitutionDto>[] = [];



  constructor(
    private readonly formBuilder: FormBuilder,
    private institutionService: InstitutionService,
    private contextService: ContextService,
    private addressMasterDataService: AddressMasterDataService,
    private careLineService: CareLineService,
    private specialtyService: SpecialtyService,
  ) {
    this.specialtyService.getAll().subscribe(
      (specialties: ClinicalSpecialtyDto[]) => {
        this.allSpecialties = specialties;
      }
    );
  }

  ngOnInit(): void {

    const endDate = datePlusDays(this.today, PERIOD_DAYS);
    this.searchForm = this.formBuilder.group({
      careLine: [null],
      specialty: [null, Validators.required],
      state: [null],
      department: [null, Validators.required],
      institution: [null],
      startDate: [this.today, Validators.required],
      endDate: [{ value: endDate, disabled: true }, Validators.required]
    });

    this.institutionService.getInstitutionAddress(this.contextService.institutionId).subscribe(
      (institutionAddres: AddressDto) => {
        this.searchForm.controls.state.setValue(institutionAddres.province);

        this.addressMasterDataService.getDepartmentsByProvince(institutionAddres.province.id).subscribe(
          departments => {
            const foundDepartment = departments.find((department) => { department.id === institutionAddres.departmentId });
            this.searchForm.controls.department.setValue(foundDepartment);
          }
        );
      }
    );

    this.institutionService.getInstitutions([this.contextService.institutionId]).subscribe(
      (institutions: InstitutionDto[]) => {
        const institutionInfo: InstitutionShortInfo = { id: institutions[0].id, name: institutions[0].name }
        this.searchForm.controls.institution.setValue(institutionInfo);
      }
    );

    this.careLineService.getCareLinesAttachedToInstitution(this.contextService.institutionId).subscribe(
      (careLines: CareLineDto[]) => {
        this.careLines = careLines;
        this.loadCareLineTypeaheadOptions();
      }
    );

    this.addressMasterDataService.getByCountry(DEFAULT_COUNTRY_ID).subscribe(
      provinces => {
        this.provinces = provinces;
        this.loadProvinceTypeaheadOptions();
      }
    );

    this.searchForm.get("state").valueChanges.subscribe(
      province => {
        this.searchForm.controls.department.reset();
        delete this.departments;
        delete this.departmentTypeaheadOptions;
        if (province) {
          this.addressMasterDataService.getDepartmentsByProvince(province.id).subscribe(
            departments => {
              this.departments = departments;
              this.loadDepartmentTypeaheadOptions();
            }
          );
        }
      }
    );

    this.searchForm.get("department").valueChanges.subscribe(
      department => {
        this.searchForm.controls.institution.reset();
        delete this.institutions;
        delete this.institutionTypeaheadOptions;
        if (department) {
          // To do .. connect BE to get institutions by department - load this.institutions
          this.institutions = [];
          this.loadInstitutionTypeaheadOptions();
        }
      }
    );

    this.searchForm.get("institution").valueChanges.subscribe(
      (institution: InstitutionShortInfo) => {
        this.searchForm.controls.careLine.reset();
        delete this.careLines;
        delete this.careLineTypeaheadOptions;
        if (institution) {
          this.careLineService.getCareLinesAttachedToInstitution(institution.id).subscribe(
            (careLines: CareLineDto[]) => {
              this.careLines = careLines;
              this.loadCareLineTypeaheadOptions();
            }
          );
        }
      }
    );

    this.searchForm.get("careLine").valueChanges.subscribe(
      (careLine: CareLineDto) => {
        this.searchForm.controls.specialty.reset();
        if (careLine?.clinicalSpecialties?.length) {
          this.specialties = careLine.clinicalSpecialties;
        }
        else {
          this.specialties = this.allSpecialties;
        }
        this.loadSpecialtyTypeaheadOptions();
      }
    );

  }

  setCareLine(careLine: CareLineDto) {
    this.searchForm.controls.careLine.setValue(careLine);
  }

  setClinicalSpecialty(clinicalSpecialty: ClinicalSpecialtyDto) {
    this.searchForm.controls.specialty.setValue(clinicalSpecialty);
  }

  setDepartment(department: DepartmentDto) {
    this.searchForm.controls.department.setValue(department);
  }

  setInstitution(institution: InstitutionShortInfo) {
    this.searchForm.controls.institution.setValue(institution);
  }

  setProvince(province) {
    this.searchForm.controls.state.setValue(province);
  }

  updateEndDate(initialDate: Moment) {
    this.searchForm.controls.endDate.setValue(datePlusDays(initialDate.toDate(), PERIOD_DAYS));
  }

  searchAppointments() {

  }

  private loadSpecialtyTypeaheadOptions(): void {
    this.specialtyTypeaheadOptions = this.specialties?.map(toTypeaheadOption);

    function toTypeaheadOption(specialty: ClinicalSpecialtyDto): TypeaheadOption<ClinicalSpecialtyDto> {
      return {
        compareValue: specialty.name,
        value: specialty,
        viewValue: specialty.name
      };
    }
  }

  private loadCareLineTypeaheadOptions(): void {
    this.careLineTypeaheadOptions = this.careLines?.map(toTypeaheadOption);

    function toTypeaheadOption(careLine: CareLineDto): TypeaheadOption<CareLineDto> {
      return {
        compareValue: careLine.description,
        value: careLine,
        viewValue: careLine.description
      };
    }
  }

  private loadDepartmentTypeaheadOptions(): void {
    this.departmentTypeaheadOptions = this.departments?.map(toTypeaheadOption);

    function toTypeaheadOption(department: DepartmentDto): TypeaheadOption<DepartmentDto> {
      return {
        compareValue: department.description,
        value: department,
        viewValue: department.description
      };
    }
  }

  private loadInstitutionTypeaheadOptions(): void {
    this.institutionTypeaheadOptions = this.institutions?.map(toTypeaheadOption);

    function toTypeaheadOption(institution: InstitutionDto): TypeaheadOption<InstitutionDto> {
      return {
        compareValue: institution.name,
        value: institution,
        viewValue: institution.name
      };
    }
  }

  private loadProvinceTypeaheadOptions(): void {
    this.provinceTypeaheadOptions = this.provinces?.map(toTypeaheadOption);

    function toTypeaheadOption(province): TypeaheadOption<any> {
      return {
        compareValue: province.description,
        value: province,
        viewValue: province.description
      };
    }
  }

}


interface InstitutionShortInfo {
  id: number,
  name: string
}
