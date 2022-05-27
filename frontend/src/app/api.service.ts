import { Injectable, Component } from '@angular/core'
import { HttpClient, HttpParams } from '@angular/common/http'
import { Observable } from 'rxjs'

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private personUrl = `//localhost:8080/person.svc`

  constructor(private httpClient: HttpClient) { }

  public personCollection(): Observable<IPerson[]> {
    return this.httpClient.get<IPerson[]>(this.personUrl + `/Persons`)
  }
}

export interface IPerson {
  id: string,
  firstName: string,
  lastName: string,
  gender: string,
  dateOfBirth: string
}