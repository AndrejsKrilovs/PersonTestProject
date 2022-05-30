import { Component } from '@angular/core'
import { ApiService, IPerson } from './api.service'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  tableHeaders = ['Personal code', 'First name', 'Last name', 'Gender', 'Birth date'];
  persons: Array<IPerson> = []
  errorMessage = ''

  personForSearch = {
    id: '',
    bd: ''
  }

  constructor(private apiService: ApiService) {
    apiService.personCollection().subscribe(result => this.persons = result)
  }

  onPersonalIdInput(event: any): void {
    this.personForSearch.id = event.target.value
  }

  onPersonalBdInput(event: any): void {
    this.personForSearch.bd = event.target.value
  }

  findPerson(): void {
    const correctUserId: RegExp = /[0-9]{6}-[0-9]{4,}/
    const isUserIdValid = correctUserId.test(this.personForSearch.id)

    const correctBirthDate: RegExp =/\d{4}-([0]\d|1[0-2])-([0-2]\d|3[01])/
    const isDateValid = correctBirthDate.test(this.personForSearch.bd)

    this.errorMessage = ''
    this.persons.splice(0)

    if(this.personForSearch.bd.length == 0 && this.personForSearch.id.length == 0) {
      this.apiService.personCollection().subscribe(result => this.persons = result)
    }

    if(isDateValid && !isUserIdValid){
      this.apiService.findByBirthDate(this.personForSearch.bd).subscribe(result => this.persons = result)

      if(this.persons.length < 0) {
        this.errorMessage = 'Person with date ' + this.personForSearch.bd + ' not found'
      }
    }
      
    if(isUserIdValid && !isDateValid){
      this.apiService.findById(this.personForSearch.id).subscribe(
        (result) => {this.persons.push(result)},
        (error) => {this.errorMessage = 'Person with id ' + this.personForSearch.id + ' not found'}
      )
    }

    if(isDateValid && isUserIdValid) {
      this.apiService.findByIdAndBirthDate(this.personForSearch.id, this.personForSearch.bd)
        .subscribe(
          result => {
            this.persons.splice(0) 
            this.persons.push(result)
          }
        )

        if(this.persons.length < 0) {
          this.errorMessage = 'Person with date ' + this.personForSearch.bd + ' and id ' + this.personForSearch.id + ' not found'
        }
    }

    if(!isDateValid && !isUserIdValid) {
      this.errorMessage = 'Incorrect person id and date. Input * and click Find to show all person list'
    }
  }

  onEdit(person: IPerson): void {
    
  }

  onDelete(person: IPerson): void {
    const arrayElement = this.persons.indexOf(person)
    this.apiService.deletePerson(person).subscribe(item => this.persons.splice(arrayElement, 1))
  }
}
