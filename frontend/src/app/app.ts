import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { PacienteRegistro } from './paciente-registro/paciente-registro';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, PacienteRegistro],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('frontend');
}
