import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TablasPercentilosComponent } from './tablas-percentilos.component';

describe('TablasPercentilosComponent', () => {
  let component: TablasPercentilosComponent;
  let fixture: ComponentFixture<TablasPercentilosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TablasPercentilosComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TablasPercentilosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
