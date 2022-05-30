import { Component } from '@angular/core'
import { ApiService, IPerson } from '../api.service'

@Component({
  selector: 'app-add-window',
  templateUrl: './add-window.component.html',
  styleUrls: ['./add-window.component.css']
})
export class AddWindowComponent {
  errorMessages: Array<string> = []
  personForAdd :IPerson = {
    id: '',
    firstName: '',
    lastName: '',
    gender: '',
    dateOfBirth: ''
  }

  constructor(private apiService: ApiService) {
    this.errorMessages = []
  }

  validatePk(event: any): void {
    this.validateField(
      /[0-9]{6}-[0-9]{4,}/, 
      event.target.value, 
      'Invalid person id. Should be in format 000000-0000'
    )
  }

  validateBd(event: any): void {
    this.validateField(
      /\d{4}-([0]\d|1[0-2])-([0-2]\d|3[01])/, 
      event.target.value, 
      'Invalid person birth date. Should be in format yyyy-mm-dd'
    )
  }

  validateGender(event: any): void {
    this.validateField(
      /[M|F]{1}/, 
      event.target.value, 
      'Invalid gender. Should be M (for Male) or F (for Female)'
    )
  }

  validateName(event: any): void {
    this.validateField(
      /[A-Z\u0410-\u042F]{1}[a-z\u0430-\u044F]{1,}/, 
      event.target.value, 
      'Invalid name. Name should start from capital letter'
    )
  }

  validateSurname(event: any): void {
    this.validateField(
      /[A-Z\u0410-\u042F]{1}[a-z\u0430-\u044F]{1,}/, 
      event.target.value, 
      'Invalid surname. Surname should start from capital letter'
    )
  }

  savePerson(): void {
    if(this.personForAdd.id.length > 0 && 
      this.personForAdd.dateOfBirth.length > 0 && 
      this.personForAdd.gender.length > 0 && 
      this.personForAdd.firstName.length > 0 && 
      this.personForAdd.lastName.length > 0) {

        this.apiService.addPerson(this.personForAdd).subscribe(result => console.log(result))
    }
  }

  private validateField(pattern: RegExp, value: string, errorMessage: string): void {
    const isValidField = pattern.test(value)

    if(isValidField) {
      this.errorMessages.splice(this.errorMessages.indexOf(errorMessage), 1)
      if(pattern === /[0-9]{6}-[0-9]{4,}/) {
        this.personForAdd.id = value
      }

      if(pattern === /\d{4}-([0]\d|1[0-2])-([0-2]\d|3[01])/) {
        this.personForAdd.dateOfBirth = value
      }

      if(pattern === /[M|F]{1}/) {
        this.personForAdd.gender = value
      }

      if(pattern ===  /[A-Z\u0410-\u042F]{1}[a-z\u0430-\u044F]{1,}/ && errorMessage.indexOf('name') != -1) {
        this.personForAdd.firstName = value
      }

      if(pattern ===  /[A-Z\u0410-\u042F]{1}[a-z\u0430-\u044F]{1,}/ && errorMessage.indexOf('surname') != -1) {
        this.personForAdd.firstName = value
      }
    } else {
      if(this.errorMessages.indexOf(errorMessage) === -1) {
        this.personForAdd.firstName = ''
        this.errorMessages.push(errorMessage)
      }
    }
  }
}
