import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JitsiMeetExternalAPIComponent } from './jitsi-meet-external-api.component';

describe('JitsiMeetExternalAPIComponent', () => {
  let component: JitsiMeetExternalAPIComponent;
  let fixture: ComponentFixture<JitsiMeetExternalAPIComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ JitsiMeetExternalAPIComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JitsiMeetExternalAPIComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
