import { NgModule } from '@angular/core';

import { PacientesRoutingModule } from './pacientes-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from "@core/core.module";


@NgModule({
	declarations: [HomeComponent],
	imports: [
		CoreModule,
		PacientesRoutingModule
	]
})
export class PacientesModule {
}
