package br.com.casamovel.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Palestrante implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	private String foto; 
	
	@OneToMany(mappedBy = "evento_id")
	private List<EventoPalestrante> eventos = new ArrayList<>();
	
	public Palestrante() {
		
	}

	public Palestrante(String id, String foto) {
		super();
		this.id = id;
		this.foto = foto;
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
		Palestrante other = (Palestrante) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getNome_palestrante() {
		return id;
	}

	public void setNome_palestrante(String nome_palestrante) {
		this.id = nome_palestrante;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public List<EventoPalestrante> getEventos() {
		return eventos;
	}

	public void setEventos(List<EventoPalestrante> eventos) {
		this.eventos = eventos;
	}
	
	
	
	
	
}
