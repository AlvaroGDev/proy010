package es.cic.curso2025.proy009.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


@Entity
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id",
    scope = Rama.class
)
public class Rama {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "longitud")
    private int longitud;
    @Column(name = "num_hojas")
    private int numHojas;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Arbol arbol;

    
    
    public Long getId() {
        return id;
    }

    public int getLongitud() {
        return longitud;
    }

    public int getNumHojas() {
        return numHojas;
    }

    public Arbol getArbol() {
        return arbol;
    }


    
    public void setId(Long id) {
        this.id = id;
    }

    public void setLongitud(int longitud) {
        this.longitud = longitud;
    }

    public void setNumHojas(int numHojas) {
        this.numHojas = numHojas;
    }

    public void setArbol(Arbol arbol) {
        this.arbol = arbol;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Rama other = (Rama) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Rama [id=" + id + ", longitud=" + longitud + ", numHojas=" + numHojas + "]";
    }

   

    
}
