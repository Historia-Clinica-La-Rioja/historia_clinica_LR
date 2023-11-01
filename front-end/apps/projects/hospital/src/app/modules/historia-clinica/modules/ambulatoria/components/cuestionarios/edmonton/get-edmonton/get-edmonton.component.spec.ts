import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GetEdmontonComponent } from './get-edmonton.component';

describe('GetEdmontonComponent', () => {
  let component: GetEdmontonComponent;
  let fixture: ComponentFixture<GetEdmontonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GetEdmontonComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GetEdmontonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
