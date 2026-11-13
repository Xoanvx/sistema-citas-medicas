import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-paciente-registro',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './paciente-registro.html',
  styleUrl: './paciente-registro.css',
})
export class PacienteRegistro {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/pacientes';

  mensaje = '';
  error = '';

  pacienteForm: FormGroup = this.fb.group({
    nombre: ['', [Validators.required, Validators.minLength(2)]],
    apellidos: ['', [Validators.required, Validators.minLength(2)]],
    dni: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]],
    correo: ['', [Validators.required, Validators.email]],
  });

  onSubmit(): void {
    if (this.pacienteForm.invalid) {
      this.pacienteForm.markAllAsTouched();
      return;
    }

    this.mensaje = '';
    this.error = '';

    this.http.post(this.apiUrl, this.pacienteForm.value).subscribe({
      next: () => {
        this.mensaje = 'Paciente registrado correctamente.';
        this.pacienteForm.reset();
      },
      error: () => {
        this.error = 'No se pudo registrar al paciente.';
      },
    });
  }
}
