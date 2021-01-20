import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdultGynecologicalTriageDialogComponent } from './adult-gynecological-triage-dialog.component';

describe('AdultGynecologicalTriageDialogComponent', () => {
  let component: AdultGynecologicalTriageDialogComponent;
  let fixture: ComponentFixture<AdultGynecologicalTriageDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdultGynecologicalTriageDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdultGynecologicalTriageDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
