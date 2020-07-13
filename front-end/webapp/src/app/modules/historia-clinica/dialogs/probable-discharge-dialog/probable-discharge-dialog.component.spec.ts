import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProbableDischargeDialogComponent } from './probable-discharge-dialog.component';

describe('ProbableDischargeDialogComponent', () => {
  let component: ProbableDischargeDialogComponent;
  let fixture: ComponentFixture<ProbableDischargeDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProbableDischargeDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProbableDischargeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
