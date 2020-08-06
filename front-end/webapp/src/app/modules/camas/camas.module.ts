import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CamasRoutingModule } from './camas-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from '@core/core.module';
import { BedDetailComponent } from './components/bed-detail/bed-detail.component';
import { PresentationModule } from '../presentation/presentation.module';
import { BedManagmentService } from './services/bed-managment.service';
import { BedFiltersComponent } from './components/bed-filters/bed-filters.component';

@NgModule({
	declarations: [
		HomeComponent,
		BedDetailComponent,
		BedFiltersComponent,
	],
	imports: [
		CoreModule,
		FormsModule,
		CamasRoutingModule,
		ReactiveFormsModule,
		PresentationModule
	],
	providers: [ BedManagmentService ]
})
export class CamasModule {
}
