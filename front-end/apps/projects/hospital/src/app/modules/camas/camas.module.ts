import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
// deps
import { InstitucionModule } from '../institucion/institucion.module';
import { PresentationModule } from '@presentation/presentation.module';
// routing
import { CamasRoutingModule } from './camas-routing.module';
import { HomeComponent } from './routes/home/home.component';

@NgModule({
	declarations: [
		// routing
		HomeComponent,
	],
	imports: [
		FormsModule,
		ReactiveFormsModule,
		// routing
		CamasRoutingModule,
		// deps
		InstitucionModule,
		PresentationModule,
	]
})
export class CamasModule {
}
