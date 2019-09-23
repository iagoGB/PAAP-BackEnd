package br.com.casamovel.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name="categoria")
public class Categoria implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE )
	private long categoria_id;
	private String nome;
	@OneToMany(mappedBy="categoria")
	List<Evento> eventos = new ArrayList<Evento>();
	
	public void setCategoria_id(long categoria_id) {
		this.categoria_id = categoria_id;
	}

	public Categoria() {
	}
	
	public Categoria (String nome) {
		this.nome = nome;
	}
	
	public long getCategoria_id() {
		return categoria_id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public List<Evento> getEventos() {
		return eventos;
	}
	
	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (categoria_id ^ (categoria_id >>> 32));
		result = prime * result + ((eventos == null) ? 0 : eventos.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		Categoria other = (Categoria) obj;
		if (categoria_id != other.categoria_id)
			return false;
		if (eventos == null) {
			if (other.eventos != null)
				return false;
		} else if (!eventos.equals(other.eventos))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

    @Override
    public String toString() {
        return "Categoria{" + "categoria_id=" + categoria_id + ", nome=" + nome + '}';
    }

        
        
}
