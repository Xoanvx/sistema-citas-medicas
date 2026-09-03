package pe.edu.utp.citasmedicas.model;

public class Consulta {
    private Long id;
    private Long citaId;
    private String diagnostico;
    private String tratamiento;

    public Consulta() {}
    public Consulta(Long id, Long citaId, String diagnostico, String tratamiento) {
        this.id = id;
        this.citaId = citaId;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCitaId() { return citaId; }
    public void setCitaId(Long citaId) { this.citaId = citaId; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }
}
