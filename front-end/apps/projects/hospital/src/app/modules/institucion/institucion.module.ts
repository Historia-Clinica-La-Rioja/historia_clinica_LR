import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
// deps
import { ExtensionsModule } from '@extensions/extensions.module';
import { JitsiModule } from '../jitsi/jitsi.module';
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';
import { PresentationModule } from '@presentation/presentation.module';
// routing
import { InstitucionRoutingModule } from './institucion-routing.module';
import { HomeComponent } from './routes/home/home.component';
// components
import { BedDetailComponent } from './components/bed-detail/bed-detail.component';
import { BedFiltersComponent } from './components/bed-filters/bed-filters.component';
import { BedMappingComponent } from './components/bed-mapping/bed-mapping.component';
import { InstitucionComponent } from './institucion.component';
import { EntryCallComponent } from './components/entry-call/entry-call.component';
import { EntryCallRendererComponent } from './components/entry-call-renderer/entry-call-renderer.component';
import { RejectedCallComponent } from './components/rejected-call/rejected-call.component';


@NgModule({
	declarations: [
		// routing
		HomeComponent,
		// components
		BedDetailComponent,
		BedFiltersComponent,
		BedMappingComponent,
		EntryCallComponent,
		EntryCallRendererComponent,
		InstitucionComponent,
		RejectedCallComponent,
	],
	imports: [
		CommonModule,
		// routing
		InstitucionRoutingModule,
		// deps
		ExtensionsModule,
		LazyMaterialModule,
		PresentationModule,
		JitsiModule
	],
	exports: [
		BedDetailComponent,
		BedFiltersComponent,
		BedMappingComponent,
	]
})
export class InstitucionModule { }
