package es.cic.curso2025.proy009.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.cic.curso2025.proy009.model.Arbol;
import es.cic.curso2025.proy009.repository.ArbolRepository;
import es.cic.curso2025.proy009.model.Rama;
import es.cic.curso2025.proy009.repository.RamaRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class ArbolService {
    @Autowired
    private ArbolRepository arbolRepository;
    @Autowired
    private RamaRepository ramaRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ArbolService.class);

    @Transactional(readOnly = true)
    public Optional<Arbol> getArbol(Long id) {

        LOGGER.info(String.format("Leído el arbol con id %s", id));

        Optional<Arbol> arbol = arbolRepository.findById(id);

        return arbol;
    }

    @Transactional(readOnly = true)
    public Optional<Rama> getRama(Long id) {

        LOGGER.info(String.format("Leído la rama con id %s", id));

        Optional<Rama> rama = ramaRepository.findById(id);

        return rama;
    }

    @Transactional(readOnly = true)
    public List<Arbol> getAllArbol() {

        LOGGER.info("Obteniendo todos los árboles desde la base de datos");
        return arbolRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Rama> getAllRama() {

        LOGGER.info("Obteniendo todas las ramas desde la base de datos");
        return ramaRepository.findAll();
    }

    public Arbol createArbol(Arbol arbol) {

        LOGGER.info(String.format("Creación de árbol"));

        return arbolRepository.save(arbol);

    }

    @Transactional
    public Rama createRama(Rama rama) {
        LOGGER.info("Creación de rama");

        if (rama.getArbol() == null || rama.getArbol().getId() == null) {
            throw new IllegalArgumentException("La rama debe tener un árbol con ID");
        }

        // Recuperar el árbol desde la base de datos
        Arbol arbol = arbolRepository.findById(rama.getArbol().getId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Árbol no encontrado con ID: " + rama.getArbol().getId()));

        // Asociar el árbol gestionado por Hibernate
        rama.setArbol(arbol);

        return ramaRepository.save(rama);
    }

    public Arbol updateArbol(Long id, Arbol arbolActualizado) {
        LOGGER.info(String.format("Actualizando el árbol con id %d", id));

        Arbol existente = arbolRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Árbol no encontrado con ID: " + id));

        // Actualiza campos simples
        existente.setPais(arbolActualizado.getPais());
        existente.setEdadAnios(arbolActualizado.getEdadAnios());
        existente.setDescripcion(arbolActualizado.getDescripcion());

        // Gestionar sincronización de ramas

        // Primero, eliminar ramas que ya no están en el arbolActualizado
        for (Rama ramaExistente : new ArrayList<>(existente.getRamas())) {
            if (!arbolActualizado.getRamas().contains(ramaExistente)) {
                existente.removeRama(ramaExistente);
            }
        }

        // Añadir o actualizar las ramas nuevas o existentes
        for (Rama ramaNueva : arbolActualizado.getRamas()) {
            if (!existente.getRamas().contains(ramaNueva)) {
                existente.addRama(ramaNueva);
            } else {
                // Opcional: actualizar atributos de la rama existente
                int index = existente.getRamas().indexOf(ramaNueva);
                Rama ramaExistente = existente.getRamas().get(index);
                ramaExistente.setLongitud(ramaNueva.getLongitud());
                ramaExistente.setNumHojas(ramaNueva.getNumHojas());
            }
        }

        return arbolRepository.save(existente);
    }

    public Rama updateRama(Long id, Rama ramaActualizada) {
        LOGGER.info(String.format("Actualizando la rama con id %d", id));

        Rama existente = ramaRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Rama no encontrada con ID: " + id));

        // Actualiza los campos simples
        existente.setLongitud(ramaActualizada.getLongitud());
        existente.setNumHojas(ramaActualizada.getNumHojas());

        // Gestiona la sincronización de la relación Arbol-Rama
        Arbol arbolActual = existente.getArbol();
        Arbol arbolNuevo = ramaActualizada.getArbol();

        if (arbolActual != null && !arbolActual.equals(arbolNuevo)) {
            // Quitar esta rama del árbol antiguo
            arbolActual.removeRama(existente);
        }

        if (arbolNuevo != null && !arbolNuevo.equals(arbolActual)) {
            // Añadir esta rama al árbol nuevo (esto también hace existente.setArbol)
            arbolNuevo.addRama(existente);
        }

        // Si arbolNuevo es null, significa que la rama queda sin árbol (desvinculada)
        if (arbolNuevo == null) {
            existente.setArbol(null);
        }

        return ramaRepository.save(existente);
    }

    public void deleteArbol(Long id) {

        LOGGER.info(String.format("Eliminación del árbol con id %s", id));

        if (!arbolRepository.existsById(id)) {
            throw new EntidadNoEncontradaException("No se puede eliminar: árbol no encontrado con ID: " + id);
        }

        arbolRepository.deleteById(id);

    }

    public void deleteRama(Long id) {

        LOGGER.info(String.format("Eliminación de la rama con id %s", id));

        if (!ramaRepository.existsById(id)) {
            throw new EntidadNoEncontradaException("No se puede eliminar: rama no encontrada con ID: " + id);
        }

        ramaRepository.deleteById(id);

    }

}
