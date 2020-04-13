import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PacientesLayoutComponent } from './pacientes-layout.component';

describe('PacientesLayoutComponent', () => {
  let component: PacientesLayoutComponent;
  let fixture: ComponentFixture<PacientesLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PacientesLayoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PacientesLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
