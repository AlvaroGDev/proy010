package es.cic.curso2025.proy009.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.cic.curso2025.proy009.model.Arbol;
import es.cic.curso2025.proy009.model.Rama;
import es.cic.curso2025.proy009.repository.ArbolRepository;
import es.cic.curso2025.proy009.repository.RamaRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ArbolControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArbolRepository arbolRepository;

    @Autowired
    private RamaRepository ramaRepository;

    @AfterEach
    void limpiarBaseDeDatos() {
        ramaRepository.deleteAll();
        arbolRepository.deleteAll();
    }

    // Tests de Arbol (los tuyos ya están bien, con alguna mejora opcional)
    @Test
    void testCreateArbol() throws Exception {
        Arbol arbol = new Arbol();
        arbol.setPais("Ucrania");
        arbol.setEdadAnios(422);
        arbol.setDescripcion("Un árbol muy chulo en Ucrania");

        String arbolJson = objectMapper.writeValueAsString(arbol);

        mockMvc.perform(post("/arboles")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(arbolJson))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Arbol creado = objectMapper.readValue(result.getResponse().getContentAsString(), Arbol.class);
                    assertNotNull(creado.getId());
                    Optional<Arbol> desdeBd = arbolRepository.findById(creado.getId());
                    assertTrue(desdeBd.isPresent());
                });
    }

    // Test de error al crear arbol con ID (modificación prohibida)
    @Test
    void testCreateArbolConIdDevuelveBadRequest() throws Exception {
        Arbol arbol = new Arbol();
        arbol.setId(100L);
        arbol.setPais("Francia");
        arbol.setEdadAnios(138);
        arbol.setDescripcion("Un árbol en París");

        String json = objectMapper.writeValueAsString(arbol);

        mockMvc.perform(post("/arboles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetArbolPorId() throws Exception {
        Arbol arbol = new Arbol();
        arbol.setPais("Italia");
        arbol.setEdadAnios(446);
        arbol.setDescripcion("Un árbol en Roma");
        arbol = arbolRepository.save(arbol);

        mockMvc.perform(get("/arboles/" + arbol.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Arbol recibido = objectMapper.readValue(result.getResponse().getContentAsString(), Arbol.class);
                    assertEquals("Italia", recibido.getPais());
                    assertEquals(446, recibido.getEdadAnios());
                    assertEquals("Un árbol en Roma", recibido.getDescripcion());
                });
    }

    @Test
    void testGetAllArboles() throws Exception {
        Arbol a1 = new Arbol();
        a1.setPais("Alemania");
        a1.setEdadAnios(333);
        a1.setDescripcion("Un árbol en Berlín");
        Arbol a2 = new Arbol();
        a2.setPais("Portugal");
        a2.setEdadAnios(336);
        a2.setDescripcion("Un árbol en Lisboa");
        arbolRepository.save(a1);
        arbolRepository.save(a2);

        mockMvc.perform(get("/arboles")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Arbol[] arboles = objectMapper.readValue(result.getResponse().getContentAsString(), Arbol[].class);
                    assertEquals(2, arboles.length);
                });
    }

    @Test
    void testUpdateArbol() throws Exception {
        Arbol arbol = new Arbol();
        arbol.setPais("Canadá");
        arbol.setEdadAnios(187);
        arbol.setDescripcion("Un árbol en Toronto");
        arbol = arbolRepository.save(arbol);

        Arbol arbolActualizado = new Arbol();
        arbolActualizado.setPais("Eslovaquia");
        arbolActualizado.setEdadAnios(587);
        arbolActualizado.setDescripcion("Un árbol en Bratislava");

        String json = objectMapper.writeValueAsString(arbolActualizado);

        mockMvc.perform(put("/arboles/" + arbol.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Arbol recibido = objectMapper.readValue(result.getResponse().getContentAsString(), Arbol.class);
                    assertEquals("Eslovaquia", recibido.getPais());
                });
    }

    @Test
    void testDeleteArbol() throws Exception {
        Arbol arbol = new Arbol();
        arbol.setPais("Colombia");
        arbol.setEdadAnios(189);
        arbol.setDescripcion("Un árbol en Cali");
        arbol = arbolRepository.save(arbol);

        mockMvc.perform(delete("/arboles/" + arbol.getId()))
                .andExpect(status().isOk());

        Optional<Arbol> eliminado = arbolRepository.findById(arbol.getId());
        assertTrue(eliminado.isEmpty());
    }

    // ------------------ Tests para ramas ------------------

    @Test
void testCreateRama() throws Exception {
    // Primero crea un árbol para asignar a la rama
    Arbol arbol = new Arbol();
    arbol.setPais("España");
    arbol.setEdadAnios(300);
    arbol.setDescripcion("Árbol para rama");
    Arbol arbolGuardado = arbolRepository.save(arbol);

    Rama rama = new Rama();
    rama.setLongitud(100);
    rama.setNumHojas(50);
    // Nota: no necesitas asignar el árbol a la rama aquí,
    // ya que el controlador lo asocia por el idArbol en la URL.

    String json = objectMapper.writeValueAsString(rama);

    mockMvc.perform(post("/arboles/" + arbolGuardado.getId() + "/nuevaRama")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk())
            .andExpect(result -> {
                Arbol arbolConRama = objectMapper.readValue(result.getResponse().getContentAsString(), Arbol.class);
                assertNotNull(arbolConRama.getRamas());
                boolean contieneRama = arbolConRama.getRamas().stream()
                    .anyMatch(r -> r.getLongitud() == 100 && r.getNumHojas() == 50);
                assertTrue(contieneRama, "El árbol debería contener la rama creada");
            });
}


    @Test
void testCreateRamaSinLongitudONumHojasDevuelveError() throws Exception {
    Arbol arbol = new Arbol();
    arbol.setPais("Test");
    arbol.setEdadAnios(100);
    arbol.setDescripcion("Árbol test");
    Arbol arbolGuardado = arbolRepository.save(arbol);

    Rama rama = new Rama();
    // No asignamos longitud ni numHojas (null)

    String json = objectMapper.writeValueAsString(rama);

    mockMvc.perform(post("/arboles/ramas" + arbolGuardado.getId() + "/nuevaRama")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest());
}



    @Test
    void testGetRamaPorId() throws Exception {
        Arbol arbol = new Arbol();
        arbol.setPais("México");
        arbol.setEdadAnios(200);
        arbol.setDescripcion("Árbol para rama");
        Arbol arbolGuardado = arbolRepository.save(arbol);

        Rama rama = new Rama();
        rama.setLongitud(55);
        rama.setNumHojas(11);
        rama.setArbol(arbolGuardado);
        rama = ramaRepository.save(rama);

        mockMvc.perform(get("/arboles/ramas/" + rama.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Rama recibido = objectMapper.readValue(result.getResponse().getContentAsString(), Rama.class);
                    assertEquals(55, recibido.getLongitud());
                    assertEquals(11, recibido.getNumHojas());
                    assertEquals(arbolGuardado.getId(), recibido.getArbol().getId());
                });
    }

    @Test
void testGetAllRamas() throws Exception {
    Arbol arbol = new Arbol();
    arbol.setPais("Argentina");
    arbol.setEdadAnios(250);
    arbol.setDescripcion("Árbol para ramas");

    Rama r1 = new Rama();
    r1.setLongitud(30);
    r1.setNumHojas(15);

    Rama r2 = new Rama();
    r2.setLongitud(40);
    r2.setNumHojas(25);

    // Añadimos las ramas al árbol (se asigna arbol a rama también)
    arbol.addRama(r1);
    arbol.addRama(r2);

    // Guardamos el árbol (cascade persist)
    arbol = arbolRepository.save(arbol);

    // Guardamos explícitamente las ramas para evitar problemas
    ramaRepository.save(r1);
    ramaRepository.save(r2);

    mockMvc.perform(get("/arboles/ramas")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(result -> {
                Rama[] ramas = objectMapper.readValue(result.getResponse().getContentAsString(), Rama[].class);
                assertEquals(2, ramas.length);
            });
}


    @Test
    void testUpdateRama() throws Exception {
        Arbol arbol = new Arbol();
        arbol.setPais("Chile");
        arbol.setEdadAnios(100);
        arbol.setDescripcion("Árbol para rama actualizar");
        Arbol arbolGuardado = arbolRepository.save(arbol);

        Rama rama = new Rama();
        rama.setLongitud(60);
        rama.setNumHojas(20);
        rama.setArbol(arbolGuardado);
        rama = ramaRepository.save(rama);

        Rama ramaActualizada = new Rama();
        ramaActualizada.setLongitud(90);
        ramaActualizada.setNumHojas(45);
        ramaActualizada.setArbol(arbolGuardado);

        String json = objectMapper.writeValueAsString(ramaActualizada);

        mockMvc.perform(put("/arboles/ramas/" + rama.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Rama recibido = objectMapper.readValue(result.getResponse().getContentAsString(), Rama.class);
                    assertEquals(90, recibido.getLongitud());
                    assertEquals(45, recibido.getNumHojas());
                    assertEquals(arbolGuardado.getId(), recibido.getArbol().getId());
                });
    }

    @Test
    void testDeleteRama() throws Exception {
        Arbol arbol = new Arbol();
        arbol.setPais("Perú");
        arbol.setEdadAnios(120);
        arbol.setDescripcion("Árbol para rama borrar");
        arbol = arbolRepository.save(arbol);

        Rama rama = new Rama();
        rama.setLongitud(70);
        rama.setNumHojas(35);
        rama.setArbol(arbol);
        rama = ramaRepository.save(rama);

        mockMvc.perform(delete("/arboles/ramas/" + rama.getId()))
                .andExpect(status().isOk());

        Optional<Rama> eliminado = ramaRepository.findById(rama.getId());
        assertTrue(eliminado.isEmpty());
    }
}
