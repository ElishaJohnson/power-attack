import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { WeaponService } from 'app/entities/weapon/weapon.service';
import { IWeapon, Weapon } from 'app/shared/model/weapon.model';

describe('Service Tests', () => {
  describe('Weapon Service', () => {
    let injector: TestBed;
    let service: WeaponService;
    let httpMock: HttpTestingController;
    let elemDefault: IWeapon;
    let expectedResult: IWeapon | IWeapon[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(WeaponService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Weapon(0, 'AAAAAAA', 0, 0, 0, 0, 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Weapon', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Weapon()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Weapon', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            attackModifier: 1,
            critChance: 1,
            critDamage: 1,
            dieValue: 1,
            diceCount: 1,
            damageBonus: 1
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Weapon', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            attackModifier: 1,
            critChance: 1,
            critDamage: 1,
            dieValue: 1,
            diceCount: 1,
            damageBonus: 1
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Weapon', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
