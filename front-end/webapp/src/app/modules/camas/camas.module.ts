import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CamasRoutingModule } from './camas-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from '@core/core.module';
import { BedDetailComponent } from './components/cama-detail/bed-detail.component';
import { FiltrosCamasComponent } from './components/filtros-camas/filtros-camas.component';
import { PresentationModule } from '../presentation/presentation.module';

@NgModule({
	declarations: [
		HomeComponent,
		BedDetailComponent,
		FiltrosCamasComponent,
	],
	imports: [
		CoreModule,
		FormsModule,
		CamasRoutingModule,
		ReactiveFormsModule,
		PresentationModule
	]
})
export class CamasModule {
}
