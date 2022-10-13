import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CuadroDeDialogoComponent } from './cuadro-de-dialogo.component';

describe('CuadroDeDialogoComponent', () => {
  let component: CuadroDeDialogoComponent;
  let fixture: ComponentFixture<CuadroDeDialogoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CuadroDeDialogoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CuadroDeDialogoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
