package br.com.casamovel.models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Palestrante implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String nome_palestrante_id;
	
	@OneToMany(mappedBy = "evento_id")
	private List<EventoPalestrante> eventos;
	
	
	
	public Palestrante() {
		
	}

	public Palestrante(String nome_palestrante) {
		super();
		this.nome_palestrante_id = nome_palestrante;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome_palestrante_id == null) ? 0 : nome_palestrante_id.hashCode());
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
		if (nome_palestrante_id == null) {
			if (other.nome_palestrante_id != null)
				return false;
		} else if (!nome_palestrante_id.equals(other.nome_palestrante_id))
			return false;
		return true;
	}

	public String getNome_palestrante() {
		return nome_palestrante_id;
	}

	public void setNome_palestrante(String nome_palestrante) {
		this.nome_palestrante_id = nome_palestrante;
	}
	
	
	
}
