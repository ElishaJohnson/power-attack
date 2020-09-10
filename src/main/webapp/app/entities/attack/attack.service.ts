import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAttack } from 'app/shared/model/attack.model';

type EntityResponseType = HttpResponse<IAttack>;
type EntityArrayResponseType = HttpResponse<IAttack[]>;

@Injectable({ providedIn: 'root' })
export class AttackService {
  public resourceUrl = SERVER_API_URL + 'api/attacks';

  constructor(protected http: HttpClient) {}

  create(attack: IAttack): Observable<EntityResponseType> {
    return this.http.post<IAttack>(this.resourceUrl, attack, { observe: 'response' });
  }

  update(attack: IAttack): Observable<EntityResponseType> {
    return this.http.put<IAttack>(this.resourceUrl, attack, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAttack>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAttack[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
