import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdministrativeTriageDialogComponent } from './administrative-triage-dialog.component';

describe('AdministrativeTriageDialogComponent', () => {
  let component: AdministrativeTriageDialogComponent;
  let fixture: ComponentFixture<AdministrativeTriageDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdministrativeTriageDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdministrativeTriageDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
