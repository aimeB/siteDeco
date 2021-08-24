import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICategorieProduit, CategorieProduit } from '../categorie-produit.model';

import { CategorieProduitService } from './categorie-produit.service';

describe('Service Tests', () => {
  describe('CategorieProduit Service', () => {
    let service: CategorieProduitService;
    let httpMock: HttpTestingController;
    let elemDefault: ICategorieProduit;
    let expectedResult: ICategorieProduit | ICategorieProduit[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CategorieProduitService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nom: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a CategorieProduit', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CategorieProduit()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CategorieProduit', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CategorieProduit', () => {
        const patchObject = Object.assign(
          {
            nom: 'BBBBBB',
          },
          new CategorieProduit()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CategorieProduit', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
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

      it('should delete a CategorieProduit', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCategorieProduitToCollectionIfMissing', () => {
        it('should add a CategorieProduit to an empty array', () => {
          const categorieProduit: ICategorieProduit = { id: 123 };
          expectedResult = service.addCategorieProduitToCollectionIfMissing([], categorieProduit);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(categorieProduit);
        });

        it('should not add a CategorieProduit to an array that contains it', () => {
          const categorieProduit: ICategorieProduit = { id: 123 };
          const categorieProduitCollection: ICategorieProduit[] = [
            {
              ...categorieProduit,
            },
            { id: 456 },
          ];
          expectedResult = service.addCategorieProduitToCollectionIfMissing(categorieProduitCollection, categorieProduit);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CategorieProduit to an array that doesn't contain it", () => {
          const categorieProduit: ICategorieProduit = { id: 123 };
          const categorieProduitCollection: ICategorieProduit[] = [{ id: 456 }];
          expectedResult = service.addCategorieProduitToCollectionIfMissing(categorieProduitCollection, categorieProduit);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(categorieProduit);
        });

        it('should add only unique CategorieProduit to an array', () => {
          const categorieProduitArray: ICategorieProduit[] = [{ id: 123 }, { id: 456 }, { id: 30362 }];
          const categorieProduitCollection: ICategorieProduit[] = [{ id: 123 }];
          expectedResult = service.addCategorieProduitToCollectionIfMissing(categorieProduitCollection, ...categorieProduitArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const categorieProduit: ICategorieProduit = { id: 123 };
          const categorieProduit2: ICategorieProduit = { id: 456 };
          expectedResult = service.addCategorieProduitToCollectionIfMissing([], categorieProduit, categorieProduit2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(categorieProduit);
          expect(expectedResult).toContain(categorieProduit2);
        });

        it('should accept null and undefined values', () => {
          const categorieProduit: ICategorieProduit = { id: 123 };
          expectedResult = service.addCategorieProduitToCollectionIfMissing([], null, categorieProduit, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(categorieProduit);
        });

        it('should return initial array if no CategorieProduit is added', () => {
          const categorieProduitCollection: ICategorieProduit[] = [{ id: 123 }];
          expectedResult = service.addCategorieProduitToCollectionIfMissing(categorieProduitCollection, undefined, null);
          expect(expectedResult).toEqual(categorieProduitCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
