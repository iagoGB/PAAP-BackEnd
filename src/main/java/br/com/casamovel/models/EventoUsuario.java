package br.com.casamovel.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@IdClass(EventoUsuarioID.class)
public class EventoUsuario implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	//@JoinColumn(name = "evento_id")
	private Evento evento;
	
	@Id
	@ManyToOne
	//@JoinColumn(name = "usuario_id")
	private Usuario usuario;
	
	private boolean isSubscribed;
	
	private boolean isPresent;
	
	public EventoUsuario() {
		
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isSubscribed() {
		return isSubscribed;
	}

	public void setSubscribed(boolean isSubscribed) {
		this.isSubscribed = isSubscribed;
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean isPresent) {
		this.isPresent = isPresent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((evento == null) ? 0 : evento.hashCode());
		result = prime * result + (isPresent ? 1231 : 1237);
		result = prime * result + (isSubscribed ? 1231 : 1237);
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
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
		EventoUsuario other = (EventoUsuario) obj;
		if (evento == null) {
			if (other.evento != null)
				return false;
		} else if (!evento.equals(other.evento))
			return false;
		if (isPresent != other.isPresent)
			return false;
		if (isSubscribed != other.isSubscribed)
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EventoUsuario [evento=" + evento + ", usuario=" + usuario + ", isSubscribed=" + isSubscribed
				+ ", isPresent=" + isPresent + "]";
	}
	
	
	
	
	
	
}
