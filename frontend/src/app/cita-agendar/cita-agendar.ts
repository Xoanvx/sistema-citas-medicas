import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-cita-agendar',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cita-agendar.html',
  styleUrl: './cita-agendar.css',
})
export class CitaAgendar {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/citas';

  mensaje = '';
  error = '';

  citaForm: FormGroup = this.fb.group({
    fecha: ['', [Validators.required]],
    hora: ['', [Validators.required]],
    medicoId: [null, [Validators.required, Validators.min(1)]],
    pacienteId: [null, [Validators.required, Validators.min(1)]],
  });

  onSubmit(): void {
    if (this.citaForm.invalid) {
      this.citaForm.markAllAsTouched();
      return;
    }

    this.mensaje = '';
    this.error = '';

    this.http.post(this.apiUrl, this.citaForm.value).subscribe({
      next: () => {
        this.mensaje = 'Cita médica agendada correctamente.';
        this.citaForm.reset();
      },
      error: () => {
        this.error = 'No se pudo agendar la cita médica.';
      },
    });
  }
}
