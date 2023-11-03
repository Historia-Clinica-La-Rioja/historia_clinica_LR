import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { JitsiCallComponent } from './components/jitsi-call/jitsi-call.component';
import { JitsiMeetExternalAPIComponent } from './components/jitsi-meet-external-api/jitsi-meet-external-api.component';
import { PresentationModule } from '@presentation/presentation.module';
import { NewJitsiCallComponent } from './components/new-jitsi-call/new-jitsi-call.component';




@NgModule({
  declarations: [
	JitsiCallComponent,
	JitsiMeetExternalAPIComponent,
	NewJitsiCallComponent
  ],
  imports: [
    CommonModule,
	PresentationModule,

  ],
  exports: [
	JitsiCallComponent
  ]
})
export class JitsiModule { }
