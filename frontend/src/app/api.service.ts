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

  public findById(id: string): Observable<IPerson> {
    return this.httpClient.get<IPerson>(this.personUrl + `/Person(k=`+ id +`)`)
  }

  public findByBirthDate(birthDate: string): Observable<IPerson[]> {
    return this.httpClient.get<IPerson[]>(this.personUrl + `/Person(dt=`+ birthDate +`)`)
  }
 
  public findByIdAndBirthDate(id: string, birthDate: string): Observable<IPerson> {
    const params = new HttpParams().set(`id`,id).set(`dt`, birthDate)
    return this.httpClient.get<IPerson>(this.personUrl + `/Person()`, {params})
  }

  public deletePerson(id: IPerson): Observable<any> {
    return this.httpClient.delete<any>(this.personUrl + `/Person(k=`+ id.id +`)`)
  }

  public editPerson(personFromDB: IPerson, updatedPerson: IPerson): Observable<IPerson> {
    return this.httpClient.put<IPerson>(this.personUrl + `/Person(k=`+ personFromDB.id +`)`, updatedPerson)
  }

  public addPerson(newPerson: IPerson): Observable<IPerson> {
    return this.httpClient.post<IPerson>(this.personUrl, newPerson)
  }
}

export interface IPerson {
  id: string,
  firstName: string,
  lastName: string,
  gender: string,
  dateOfBirth: string
}