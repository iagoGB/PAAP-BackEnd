package br.com.casamovel.models;

import java.io.Serializable;


public class EventoPalestranteID implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long evento_id;
	
	private String nome_palestrante_id;
	
	
	public EventoPalestranteID() {
		
	}
	
	public String getPalestrante() {
		return nome_palestrante_id;
	}
	public void setPalestrante(String palestrante) {
		this.nome_palestrante_id = palestrante;
	}
	public Long getEvento_id() {
		return evento_id;
	}
	public void setEvento_id(Long evento_id) {
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
		EventoPalestranteID other = (EventoPalestranteID) obj;
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
	
	
	
	
}
