package br.com.casamovel.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
@IdClass(EventoPalestranteID.class)
public class EventoPalestrante implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@ManyToOne
	@JoinColumn(
		name = "fk_nome_palestrante_id",
		referencedColumnName = "id",
		foreignKey = @ForeignKey(name="fk_nome_palestrante")
	)
	private Palestrante nome_palestrante_id;
	
	@Id
	@ManyToOne
	@JoinColumn(
			name = "fk_evento_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name="fk_evento_id")
	)
	private Evento evento_id;
	
	

	public EventoPalestrante() {
		
	}

	public EventoPalestrante(Palestrante nome_palestrante_id, Evento evento_id) {
		super();
		this.nome_palestrante_id = nome_palestrante_id;
		this.evento_id = evento_id;
	}

	public Palestrante getNome_palestrante_id() {
		return nome_palestrante_id;
	}

	public void setNome_palestrante_id(Palestrante nome_palestrante_id) {
		this.nome_palestrante_id = nome_palestrante_id;
	}

	public Evento getEvento_id() {
		return evento_id;
	}

	public void setEvento_id(Evento evento_id) {
		this.evento_id = evento_id;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((evento_id == null) ? 0 : evento_id.hashCode());
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
		EventoPalestrante other = (EventoPalestrante) obj;
		if (evento_id == null) {
			if (other.evento_id != null)
				return false;
		} else if (!evento_id.equals(other.evento_id))
			return false;
		if (nome_palestrante_id == null) {
			if (other.nome_palestrante_id != null)
				return false;
		} else if (!nome_palestrante_id.equals(other.nome_palestrante_id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EventoPalestrante [nome_palestrante_id=" + nome_palestrante_id + ", evento_id=" + evento_id + "]";
	}
	
	
	
		
	
}
