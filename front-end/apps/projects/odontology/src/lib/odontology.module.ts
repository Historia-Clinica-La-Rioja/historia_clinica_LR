import { NgModule } from '@angular/core';
import { OdontologyComponent } from './routes/odontology.component';
import { OdontologyRoutingModule } from './odontology-routing.module';
import { CommonModule } from '@angular/common';

@NgModule({
	declarations: [OdontologyComponent],
	imports: [
		OdontologyRoutingModule,
		CommonModule
	],
	exports: [OdontologyComponent]
})
export class OdontologyModule {
}
