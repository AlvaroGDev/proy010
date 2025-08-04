package es.cic.curso2025.proy009.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.cic.curso2025.proy009.model.Arbol;
import es.cic.curso2025.proy009.model.Rama;
import es.cic.curso2025.proy009.repository.ArbolRepository;
import es.cic.curso2025.proy009.repository.RamaRepository;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ArbolServiceIntegrationTest {

    @Autowired
    private ArbolService arbolService;

    @Autowired
    private ArbolRepository arbolRepository;

    @Autowired
    private RamaRepository ramaRepository;

    @AfterEach
    void limpiarBaseDeDatos() {
        arbolRepository.deleteAll();
        ramaRepository.deleteAll();
    }

    @Test
    void testCreateArbol() {

        Arbol arbol = new Arbol();
        arbol.setPais("Argentina");
        arbol.setEdadAnios(128);
        arbol.setDescripcion("Un árbol muy chulo en la Patagonia");

        Arbol arbolGuardado = arbolService.createArbol(arbol);

        assertNotNull(arbolGuardado.getId(), "El ID no debe ser null tras guardar");
        assertEquals("Argentina", arbolGuardado.getPais());
        assertEquals(128, arbolGuardado.getEdadAnios());
        assertEquals("Un árbol muy chulo en la Patagonia", arbolGuardado.getDescripcion());

        Optional<Arbol> desdeBD = arbolRepository.findById(arbolGuardado.getId());
        assertTrue(desdeBD.isPresent(), "El árbol debe existir en la BBDD");
    }

    @Test 
void testCreateRama() {
    // Crear y guardar un árbol para asociar a la rama
    Arbol arbol = new Arbol();
    arbol.setPais("Italia");
    Arbol arbolGuardado = arbolService.createArbol(arbol);

    // Crear la rama y asignar el árbol
    Rama rama = new Rama();
    rama.setLongitud(524);
    rama.setNumHojas(12);
    rama.setArbol(arbolGuardado);  // Asociar árbol

    // Crear la rama con el servicio
    Rama ramaGuardada = arbolService.createRama(rama);

    // Comprobar que se guardó correctamente
    assertNotNull(ramaGuardada.getId(), "El ID no debe ser null tras guardar");
    assertEquals(524, ramaGuardada.getLongitud());
    assertEquals(12, ramaGuardada.getNumHojas());

    Optional<Rama> desdeBD = ramaRepository.findById(ramaGuardada.getId());
    assertTrue(desdeBD.isPresent(), "La rama debe existir en la BBDD");
}


    @Test
    void testGetArbol() {

        Arbol arbol = new Arbol();
        arbol.setPais("Marruecos");
        arbol.setEdadAnios(112);
        arbol.setDescripcion("Un arbol muy chulo en Marrakech");

        Arbol arbolGuardado = arbolService.createArbol(arbol);

        Optional<Arbol> arbolObtenido = arbolService.getArbol(arbolGuardado.getId());

        assertTrue(arbolObtenido.isPresent(), "El árbol debería estar presente");
        assertEquals("Marruecos", arbolObtenido.get().getPais());
        assertEquals(112, arbolObtenido.get().getEdadAnios());
        assertEquals("Un arbol muy chulo en Marrakech", arbolObtenido.get().getDescripcion());
    }

    @Test
void testGetRama() {
    // Crear y guardar el árbol
    Arbol arbol = new Arbol();
    arbol.setPais("Árbol de prueba");
    Arbol arbolGuardado = arbolService.createArbol(arbol);

    // Crear la rama y asociarla al árbol
    Rama rama = new Rama();
    rama.setLongitud(324);
    rama.setNumHojas(25);
    rama.setArbol(arbolGuardado); // Asociar el árbol

    // Guardar la rama
    Rama ramaGuardada = arbolService.createRama(rama);

    // Obtener la rama por ID
    Optional<Rama> ramaObtenida = arbolService.getRama(ramaGuardada.getId());

    // Comprobar que existe y tiene los datos esperados
    assertTrue(ramaObtenida.isPresent(), "La rama debería estar presente");
    assertEquals(324, ramaObtenida.get().getLongitud());
    assertEquals(25, ramaObtenida.get().getNumHojas());
}


    @Test
    void testGetAllArbol() {
        Arbol arbol1 = new Arbol();
        arbol1.setPais("Kenia");
        Arbol arbol2 = new Arbol();
        arbol2.setPais("Polonia");

        arbolService.createArbol(arbol1);
        arbolService.createArbol(arbol2);

        List<Arbol> lista = arbolService.getAllArbol();

        assertEquals(2, lista.size());
    }

    @Test
void testGetAllRama() {
    // Crear un árbol al que se asociarán las ramas
    Arbol arbol = new Arbol();
    arbol.setPais("Árbol de prueba");
    Arbol arbolGuardado = arbolService.createArbol(arbol);

    // Crear dos ramas asociadas al árbol
    Rama rama1 = new Rama();
    rama1.setNumHojas(85);
    rama1.setArbol(arbolGuardado); // Asociar al árbol

    Rama rama2 = new Rama();
    rama2.setNumHojas(37);
    rama2.setArbol(arbolGuardado); // Asociar al mismo árbol

    arbolService.createRama(rama1);
    arbolService.createRama(rama2);

    List<Rama> lista = arbolService.getAllRama();

    assertEquals(2, lista.size());
}


    @Test
    void testUpdateArbol() {
        Arbol arbol = new Arbol();
        arbol.setPais("Camerún");
        arbol.setEdadAnios(327);
        arbol.setDescripcion("Un arbol muy chulo en Camerún");
        Arbol arbolGuardado = arbolService.createArbol(arbol);

        Arbol arbolActualizado = new Arbol();
        arbolActualizado.setPais("Togo");
        arbolActualizado.setEdadAnios(314);
        arbolActualizado.setDescripcion("Un arbol muy chulo en Togo");

        Arbol resultado = arbolService.updateArbol(arbolGuardado.getId(), arbolActualizado);

        assertEquals("Togo", resultado.getPais());
        assertEquals(314, resultado.getEdadAnios());
        assertEquals("Un arbol muy chulo en Togo", resultado.getDescripcion());
    }

    @Test
void testUpdateRama() {
    // Creamos y guardamos un árbol
    Arbol arbol = new Arbol();
    arbol.setPais("Árbol de prueba");
    Arbol arbolGuardado = arbolService.createArbol(arbol);

    // Creamos y guardamos una rama asociada al árbol
    Rama rama = new Rama();
    rama.setLongitud(124);
    rama.setNumHojas(8);
    rama.setArbol(arbolGuardado);  // ← Importante
    Rama ramaGuardada = arbolService.createRama(rama);

    // Creamos una nueva instancia con los datos actualizados
    Rama ramaActualizada = new Rama();
    ramaActualizada.setLongitud(132);
    ramaActualizada.setNumHojas(17);
    ramaActualizada.setArbol(arbolGuardado);  // ← También importante

    // Ejecutamos la actualización
    Rama resultado = arbolService.updateRama(ramaGuardada.getId(), ramaActualizada);

    // Verificamos que se hayan actualizado los valores
    assertEquals(132, resultado.getLongitud());
    assertEquals(17, resultado.getNumHojas());
}

    @Test
    void testUpdateNoExisteArbol() {
        Arbol arbolActualizado = new Arbol();
        arbolActualizado.setPais("Ruanda");

        assertThrows(EntidadNoEncontradaException.class, () -> {
            arbolService.updateArbol(999L, arbolActualizado);
        });
    }

    @Test
    void testUpdateNoExisteRama() {
        Rama ramaActualizada = new Rama();
        ramaActualizada.setLongitud(33);

        assertThrows(EntidadNoEncontradaException.class, () -> {
            arbolService.updateRama(999L, ramaActualizada);
        });
    }

    @Test
    void testDeleteArbol() {
        Arbol arbol = new Arbol();
        arbol.setPais("Madagascar");
        Arbol arbolGuardado = arbolService.createArbol(arbol);

        arbolService.deleteArbol(arbolGuardado.getId());

        Optional<Arbol> eliminado = arbolService.getArbol(arbolGuardado.getId());

        assertFalse(eliminado.isPresent());
    }

    @Test
void testDeleteRama() {
    // Creamos un árbol y lo guardamos
    Arbol arbol = new Arbol();
    arbol.setPais("Árbol de prueba");
    Arbol arbolGuardado = arbolService.createArbol(arbol);

    // Creamos la rama y le asociamos el árbol
    Rama rama = new Rama();
    rama.setNumHojas(84);
    rama.setArbol(arbolGuardado);  // ← ASOCIACIÓN OBLIGATORIA
    Rama ramaGuardada = arbolService.createRama(rama);

    // Eliminamos la rama
    arbolService.deleteRama(ramaGuardada.getId());

    // Verificamos que ya no existe
    Optional<Rama> eliminado = arbolService.getRama(ramaGuardada.getId());
    assertFalse(eliminado.isPresent());
}


    @Test
    void testDeleteNoExisteArbol() {
        assertThrows(EntidadNoEncontradaException.class, () -> {
            arbolService.deleteArbol(999L);
        });
    }

    @Test
    void testDeleteNoExisteRama() {
        assertThrows(EntidadNoEncontradaException.class, () -> {
            arbolService.deleteRama(999L);
        });
    }

    @Test
    @Transactional
    void testAñadirRamaALaListaDeUnArbolYPersistir() {
        // Crear y guardar árbol
        Arbol arbol = new Arbol();
        arbol.setPais("España");
        arbol.setEdadAnios(100);
        arbol.setDescripcion("Un árbol en España");
        Arbol arbolGuardado = arbolService.createArbol(arbol);

        // Crear rama y añadir al árbol con método addRama
        Rama rama = new Rama();
        rama.setLongitud(10);
        rama.setNumHojas(5);
        arbolGuardado.addRama(rama);

        // Guardar la rama y actualizar el árbol
        ramaRepository.save(rama);
        arbolService.updateArbol(arbolGuardado.getId(), arbolGuardado);

        // Obtener árbol de BD y verificar relación
        Optional<Arbol> desdeBD = arbolService.getArbol(arbolGuardado.getId());
        assertTrue(desdeBD.isPresent());

        List<Rama> ramas = desdeBD.get().getRamas();
        assertFalse(ramas.isEmpty());
        assertEquals(1, ramas.size());
        assertEquals(10, ramas.get(0).getLongitud());
    }

    @Test
    @Transactional
    void testEliminarRamaDeUnArbolYPersistirCambio() {
        // Crear árbol y rama, establecer relación
        Arbol arbol = new Arbol();
        arbol.setPais("Portugal");
        arbol.setEdadAnios(80);
        arbol.setDescripcion("Un árbol en Portugal");

        Rama rama = new Rama();
        rama.setLongitud(15);
        rama.setNumHojas(6);

        arbol.addRama(rama);

        // Guardar árbol y rama
        arbolService.createArbol(arbol);
        ramaRepository.save(rama);

        // Ahora eliminar la rama de la lista y actualizar
        arbol.removeRama(rama);
        arbolService.updateArbol(arbol.getId(), arbol);

        // Comprobar que rama ya no está en el árbol
        Optional<Arbol> desdeBD = arbolService.getArbol(arbol.getId());
        assertTrue(desdeBD.isPresent());
        List<Rama> ramas = desdeBD.get().getRamas();
        assertTrue(ramas.isEmpty());

        // La rama sigue existiendo en BD, puedes eliminarla si quieres
        assertTrue(ramaRepository.findById(rama.getId()).isPresent());
    }

    @Test
    @Transactional
    void testCargaPerezosaFetchTypeLAZY() {
        // Crear árbol con ramas
        Arbol arbol = new Arbol();
        arbol.setPais("Italia");
        arbol.setEdadAnios(200);
        arbol.setDescripcion("Un árbol en Italia");

        Rama rama = new Rama();
        rama.setLongitud(30);
        rama.setNumHojas(15);

        arbol.addRama(rama);

        arbolService.createArbol(arbol);
        ramaRepository.save(rama);

        // Ahora cargar solo el árbol desde BD (fetch perezoso no carga las ramas aún)
        Optional<Arbol> desdeBD = arbolRepository.findById(arbol.getId());
        assertTrue(desdeBD.isPresent());

        Arbol arbolCargado = desdeBD.get();

        // No accedemos a ramas todavía, deberían no cargarse aún (esto no se puede
        // verificar directamente sin herramientas, pero la siguiente línea fuerza la
        // carga)
        List<Rama> ramas = arbolCargado.getRamas();

        // Al acceder a getRamas(), la colección debería cargarse sin error
        assertNotNull(ramas);
        assertFalse(ramas.isEmpty());
        assertEquals(1, ramas.size());
    }


    

}
