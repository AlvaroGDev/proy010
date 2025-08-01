package es.cic.curso2025.proy009.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;


@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id",
    scope = Arbol.class
)

@Entity
public class Arbol {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // ATRIBUTOS
    private String pais;
    private int edadAnios;
    private String descripcion;

    @OneToMany(mappedBy = "arbol", cascade = { CascadeType.PERSIST, CascadeType.REMOVE,
            CascadeType.MERGE }, fetch = FetchType.LAZY, orphanRemoval = true)
            
    private List<Rama> ramas = new ArrayList<>();

    public void addRama(Rama rama) {
        if (ramas == null) {
            ramas = new ArrayList<>();
        }
        if (!ramas.contains(rama)) { // evita duplicados
            ramas.add(rama);
            rama.setArbol(this);
        }
    }

    public void removeRama(Rama rama) {
        ramas.remove(rama);
        rama.setArbol(null);
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public String getPais() {
        return pais;
    }

    public int getEdadAnios() {
        return edadAnios;
    }

    public List<Rama> getRamas() {
        return ramas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public void setEdadAnios(int edadAnios) {
        this.edadAnios = edadAnios;
    }

    @Override
    public String toString() {
        return "Arbol [id=" + id + ", pais=" + pais + ", edadAnios=" + edadAnios + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Arbol other = (Arbol) obj;
        if (id != other.getId())
            return false;
        return true;
    }

    public void setRamas(List<Rama> ramas) {
        this.ramas = ramas;
    }

}
