import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, AbstractControl } from '@angular/forms';
import { GestionCamaService, Sector, Speciality, Category } from '../../services/gestion-cama.service';
import { momentFormat, DateFormat } from '@core/utils/moment.utils';

@Component({
  selector: 'app-filtros-camas',
  templateUrl: './filtros-camas.component.html',
  styleUrls: ['./filtros-camas.component.scss']
})
export class FiltrosCamasComponent implements OnInit {

	public form: FormGroup;
	public sectors: Sector[] = [];
	public specialities: Speciality[] = [];
	public categories: Category[] = [];

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly gestionCamaService: GestionCamaService
  	) { }

  	ngOnInit(): void {
		this.form = this.formBuilder.group({
			sectors: [''],
			specialities: [''],
			categories: [''],
			probableDischargeDate: [''],
			filled: [true]
		});

		const filterOptions = this.gestionCamaService.getFilterOptions();
		this.sectors = filterOptions.sectors;
		this.specialities = filterOptions.specialities;
		this.categories = filterOptions.categories;
  	}

	public filterChange() {
		this.gestionCamaService.sendBedManagmentFilter(this.newBedManagmentFilter());
	}

	private newBedManagmentFilter() {
		return {
			sector: this.form.value.sectors ? this.form.value.sectors : null,
			speciality:  this.form.value.specialities ? this.form.value.specialities : null,
			category:  this.form.value.categories ? this.form.value.categories : null,
			probableDischargeDate: this.form.value.probableDischargeDate ? momentFormat(this.form.value.probableDischargeDate, DateFormat.API_DATE) : null,
			filled: this.form.value.filled
		};
	}

	clear(control: AbstractControl) {
		control.reset();
		this.filterChange();
	}

}

export class BedManagmentFilter {
	sector: number;
	speciality: number;
	category: number;
	probableDischargeDate: string;
	filled: boolean;
}
