import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PowerattackTestModule } from '../../../test.module';
import { WeaponComponent } from 'app/entities/weapon/weapon.component';
import { WeaponService } from 'app/entities/weapon/weapon.service';
import { Weapon } from 'app/shared/model/weapon.model';

describe('Component Tests', () => {
  describe('Weapon Management Component', () => {
    let comp: WeaponComponent;
    let fixture: ComponentFixture<WeaponComponent>;
    let service: WeaponService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PowerattackTestModule],
        declarations: [WeaponComponent]
      })
        .overrideTemplate(WeaponComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WeaponComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WeaponService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Weapon(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.weapons && comp.weapons[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
