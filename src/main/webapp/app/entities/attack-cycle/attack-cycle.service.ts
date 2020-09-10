import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAttackCycle } from 'app/shared/model/attack-cycle.model';

type EntityResponseType = HttpResponse<IAttackCycle>;
type EntityArrayResponseType = HttpResponse<IAttackCycle[]>;

@Injectable({ providedIn: 'root' })
export class AttackCycleService {
  public resourceUrl = SERVER_API_URL + 'api/attack-cycles';

  constructor(protected http: HttpClient) {}

  create(attackCycle: IAttackCycle): Observable<EntityResponseType> {
    return this.http.post<IAttackCycle>(this.resourceUrl, attackCycle, { observe: 'response' });
  }

  update(attackCycle: IAttackCycle): Observable<EntityResponseType> {
    return this.http.put<IAttackCycle>(this.resourceUrl, attackCycle, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAttackCycle>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAttackCycle[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
