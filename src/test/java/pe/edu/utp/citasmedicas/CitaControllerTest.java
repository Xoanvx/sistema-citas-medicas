package pe.edu.utp.citasmedicas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.utp.citasmedicas.model.Cita;
import pe.edu.utp.citasmedicas.repository.CitaRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
public class CitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CitaRepository citaRepository;

    private Long idCita1;
    private Long idCita2;

    @BeforeEach
    void setUp() {
        citaRepository.deleteAll();
        Cita c1 = citaRepository.save(new Cita(null, 1L, 1L, "PENDIENTE"));
        Cita c2 = citaRepository.save(new Cita(null, 2L, 2L, "CONFIRMADA"));
        idCita1 = c1.getId();
        idCita2 = c2.getId();
    }

    // --- GET /citas ---
    @Test
    void testListarTodas_Exitoso() throws Exception {
        mockMvc.perform(get("/citas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testListarTodas_ValidaContenido() throws Exception {
        mockMvc.perform(get("/citas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"));
    }

    @Test
    void testListarTodas_MetodoInvalido_Fallido() throws Exception {
        mockMvc.perform(patch("/citas"))
                .andExpect(status().isMethodNotAllowed());
    }

    // --- GET /citas/{id} ---
    @Test
    void testBuscarPorId_Exitoso() throws Exception {
        mockMvc.perform(get("/citas/" + idCita1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pacienteId").value(1))
                .andExpect(jsonPath("$.medicoId").value(1));
    }

    @Test
    void testBuscarPorId_SegundaCita_Exitoso() throws Exception {
        mockMvc.perform(get("/citas/" + idCita2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CONFIRMADA"));
    }

    @Test
    void testBuscarPorId_NoExiste_Fallido() throws Exception {
        mockMvc.perform(get("/citas/999999"))
                .andExpect(status().isNotFound());
    }

    // --- POST /citas ---
    @Test
    void testCrear_Exitoso() throws Exception {
        String json = "{\"pacienteId\":1,\"medicoId\":2,\"estado\":\"PENDIENTE\"}";

        mockMvc.perform(post("/citas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void testCrear_ValidarPacienteYMedico_Exitoso() throws Exception {
        String json = "{\"pacienteId\":2,\"medicoId\":1,\"estado\":\"CONFIRMADA\"}";

        mockMvc.perform(post("/citas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pacienteId").value(2))
                .andExpect(jsonPath("$.medicoId").value(1));
    }

    @Test
    void testCrear_PacienteVacio_Fallido() throws Exception {
        String jsonInvalido = "{\"pacienteId\":null,\"medicoId\":1,\"estado\":\"PENDIENTE\"}";

        mockMvc.perform(post("/citas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCrear_MedicoVacio_Fallido() throws Exception {
        String jsonInvalido = "{\"pacienteId\":1,\"medicoId\":null,\"estado\":\"PENDIENTE\"}";

        mockMvc.perform(post("/citas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }

    // --- PUT /citas/{id}/estado ---
    @Test
    void testActualizarEstado_Exitoso() throws Exception {
        mockMvc.perform(put("/citas/" + idCita1 + "/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content("CANCELADA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CANCELADA"));
    }

    @Test
    void testActualizarEstado_NoExiste_Fallido() throws Exception {
        mockMvc.perform(put("/citas/999999/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content("CANCELADA"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarEstado_MetodoInvalido_Fallido() throws Exception {
        mockMvc.perform(post("/citas/" + idCita1 + "/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content("ATENDIDA"))
                .andExpect(status().isMethodNotAllowed());
    }

    // --- DELETE /citas/{id} ---
    @Test
    void testEliminar_Exitoso() throws Exception {
        mockMvc.perform(delete("/citas/" + idCita1))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminar_NoExiste_Fallido() throws Exception {
        mockMvc.perform(delete("/citas/999999"))
                .andExpect(status().isNotFound());
    }
}