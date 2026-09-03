package pe.edu.utp.citasmedicas.model;

public class Medico {
    private Long id;
    private String nombre;
    private String especialidad;
    private String colegiatura;

    public Medico() {}
    public Medico(Long id, String nombre, String especialidad, String colegiatura) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.colegiatura = colegiatura;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public String getColegiatura() { return colegiatura; }
    public void setColegiatura(String colegiatura) { this.colegiatura = colegiatura; }
}
