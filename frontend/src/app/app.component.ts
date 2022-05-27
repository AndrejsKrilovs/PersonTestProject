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
  
  constructor(private apiService: ApiService) {
    apiService.personCollection()
              .subscribe(result => this.persons = result)
  }
}
