package br.com.casamovel.models;

import java.io.Serializable;

public class EventoUsuarioID implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long evento_id;
	
	private Long usuario_id;
	
	public EventoUsuarioID() {
		 
	}
	
	
	public Long getEvento_id() {
		return evento_id;
	}


	public void setEvento_id(Long evento_id) {
		this.evento_id = evento_id;
	}


	public Long getUsuario_id() {
		return usuario_id;
	}


	public void setUsuario_id(Long usuario_id) {
		this.usuario_id = usuario_id;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((evento_id == null) ? 0 : evento_id.hashCode());
		result = prime * result + ((usuario_id == null) ? 0 : usuario_id.hashCode());
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
		EventoUsuarioID other = (EventoUsuarioID) obj;
		if (evento_id == null) {
			if (other.evento_id != null)
				return false;
		} else if (!evento_id.equals(other.evento_id))
			return false;
		if (usuario_id == null) {
			if (other.usuario_id != null)
				return false;
		} else if (!usuario_id.equals(other.usuario_id))
			return false;
		return true;
	}
	
	
}
