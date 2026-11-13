import { Component, Input, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-cita-reprogramar',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cita-reprogramar.html',
  styleUrl: './cita-reprogramar.css',
})
export class CitaReprogramar implements OnInit {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/citas';

  @Input() citaId?: number;
  mensaje = '';
  error = '';

  reprogramarForm: FormGroup = this.fb.group({
    citaId: [null, [Validators.required, Validators.min(1)]],
    fecha: ['', [Validators.required]],
    hora: ['', [Validators.required]],
  });

  ngOnInit(): void {
    if (this.citaId) {
      this.reprogramarForm.patchValue({ citaId: this.citaId });
    }
  }

  onSubmit(): void {
    if (this.reprogramarForm.invalid) {
      this.reprogramarForm.markAllAsTouched();
      return;
    }

    const { citaId, fecha, hora } = this.reprogramarForm.value;
    this.mensaje = '';
    this.error = '';

    this.http.put(`${this.apiUrl}/${citaId}`, { fecha, hora }).subscribe({
      next: () => {
        this.mensaje = 'Cita reprogramada correctamente.';
      },
      error: () => {
        this.error = 'No se pudo reprogramar la cita.';
      },
    });
  }
}
