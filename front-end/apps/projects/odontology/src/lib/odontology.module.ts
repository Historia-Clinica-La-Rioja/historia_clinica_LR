import { NgModule } from '@angular/core';
import { OdontologyComponent } from './routes/odontology.component';
import { OdontologyRoutingModule } from './odontology-routing.module';

@NgModule({
	declarations: [OdontologyComponent],
	imports: [
		OdontologyRoutingModule
	],
	exports: [OdontologyComponent]
})
export class OdontologyModule {
}
