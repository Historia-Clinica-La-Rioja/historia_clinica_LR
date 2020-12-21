import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GuardiaRoutingModule } from './guardia-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from '@core/core.module';
import { PresentationModule } from '@presentation/presentation.module';
import { TriageComponent } from './component/triage/triage.component';
import { AdministrativeTriageComponent } from './component/administrative-triage/administrative-triage.component';
import { NewEpisodeAdminTriageComponent } from './routes/new-episode-admin-triage/new-episode-admin-triage.component';


@NgModule({
	declarations: [HomeComponent, TriageComponent, AdministrativeTriageComponent, NewEpisodeAdminTriageComponent],
	imports: [
		CommonModule,
		GuardiaRoutingModule,
		CoreModule,
		PresentationModule
	]
})
export class GuardiaModule {
}
