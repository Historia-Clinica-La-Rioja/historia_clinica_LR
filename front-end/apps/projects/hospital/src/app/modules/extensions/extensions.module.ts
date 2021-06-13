import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SystemExtensionComponent, InstitutionExtensionComponent } from './routes/extension/extension.component';
import { ApiRestModule } from '@api-rest/api-rest.module';
import { CoreModule } from '@core/core.module';
import { PresentationModule } from '@presentation/presentation.module';



@NgModule({
  declarations: [
	SystemExtensionComponent,
	InstitutionExtensionComponent,
  ],
  imports: [
	ApiRestModule,
	CommonModule,
	CoreModule,
	PresentationModule,

  ]
})
export class ExtensionsModule { }
