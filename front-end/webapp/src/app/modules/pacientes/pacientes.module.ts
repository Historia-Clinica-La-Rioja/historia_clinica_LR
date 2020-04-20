import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PacientesRoutingModule } from './pacientes-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from "@core/core.module";
import { AppMaterialModule } from "../../app.material.module";
import { PacientesTableComponent } from './component/pacientes-table/pacientes-table.component';
import { SearchCreateComponent } from './component/search-create/search-create.component';
import { SearchComponent } from './routes/search/search.component';
import { PartialMatchesTableComponent } from './component/partial-matches-table/partial-matches-table.component';

import { NewPatientComponent } from './routes/new-patient/new-patient.component';

@NgModule({
	declarations: [
		HomeComponent,
        PacientesTableComponent,
        PartialMatchesTableComponent,
        SearchCreateComponent,
        SearchComponent,
		NewPatientComponent,
	],
	imports: [
		AppMaterialModule,
		CoreModule,
		FormsModule,
		PacientesRoutingModule,
        ReactiveFormsModule,
	]
})
export class PacientesModule {
}
