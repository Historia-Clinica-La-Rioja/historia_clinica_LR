import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CamasRoutingModule } from './camas-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from '@core/core.module';
import { CamaDetailComponent } from './components/cama-detail/cama-detail.component';
import { FiltrosCamasComponent } from './components/filtros-camas/filtros-camas.component';
import { PresentationModule } from '../presentation/presentation.module';

@NgModule({
	declarations: [
		HomeComponent,
		CamaDetailComponent,
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
