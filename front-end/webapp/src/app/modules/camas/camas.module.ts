import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CamasRoutingModule } from './camas-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from '@core/core.module';
import { PresentationModule } from '../presentation/presentation.module';
import { InstitucionModule } from '../institucion/institucion.module';

@NgModule({
	declarations: [
		HomeComponent,
	],
	imports: [
CoreModule,
		FormsModule,
		CamasRoutingModule,
		ReactiveFormsModule,
		PresentationModule,
		InstitucionModule
	]
})
export class CamasModule {
}
