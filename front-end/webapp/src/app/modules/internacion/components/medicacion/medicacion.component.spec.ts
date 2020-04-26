import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MedicacionComponent } from './medicacion.component';

describe('MedicacionComponent', () => {
  let component: MedicacionComponent;
  let fixture: ComponentFixture<MedicacionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MedicacionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MedicacionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
