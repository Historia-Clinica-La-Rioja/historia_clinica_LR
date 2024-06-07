import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './routes/home/home.component';
import { HeaderAttentionDetailComponent } from '../../../hsi-components/header-attention-detail/header-attention-detail.component';
import { JointSignatureRoutingModule } from './joint-signature-routing.module';
import { PresentationModule } from '@presentation/presentation.module';
import { ShowMoreConceptsPipe } from '@presentation/pipes/show-more-concepts.pipe';
import { JointSignatureDocumentsCardComponent } from './components/joint-signature-documents-card/joint-signature-documents-card.component';
import { RejectSignatureComponent } from './dialogs/reject-signature/reject-signature.component';
import { JointSignatureDocumentsComponent } from './components/joint-signature-documents/joint-signature-documents.component';
import { JointSignatureDocumentsFiltersComponent } from './components/joint-signature-documents-filters/joint-signature-documents-filters.component';

@NgModule({
	declarations: [
		HomeComponent,
		JointSignatureDocumentsCardComponent,
		JointSignatureDocumentsComponent,
		JointSignatureDocumentsFiltersComponent,
		RejectSignatureComponent,
	],
	imports: [
		CommonModule,
		HeaderAttentionDetailComponent,
		JointSignatureRoutingModule,
		PresentationModule,
	],
	providers: [
		ShowMoreConceptsPipe
	]
})
export class JointSignatureModule { }

