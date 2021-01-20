import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PediatricTriageDialogComponent } from './pediatric-triage-dialog.component';

describe('PediatricTriageDialogComponent', () => {
  let component: PediatricTriageDialogComponent;
  let fixture: ComponentFixture<PediatricTriageDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PediatricTriageDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PediatricTriageDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
