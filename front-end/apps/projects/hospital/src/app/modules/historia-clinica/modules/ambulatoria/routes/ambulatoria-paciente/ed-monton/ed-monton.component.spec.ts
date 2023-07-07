import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EdMontonComponent } from './ed-monton.component';

describe('EdMontonComponent', () => {
  let component: EdMontonComponent;
  let fixture: ComponentFixture<EdMontonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EdMontonComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EdMontonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
