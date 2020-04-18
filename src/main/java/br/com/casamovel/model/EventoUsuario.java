package br.com.casamovel.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;

@Entity
@AllArgsConstructor
@IdClass(EventoUsuarioID.class)
public class EventoUsuario implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
        @JoinColumn(
		name = "fk_evento_id",
		referencedColumnName = "id",
		foreignKey = @ForeignKey(name="fk_evento_id")
	)
	private Evento evento_id;
	
	@Id
	@ManyToOne
	@JoinColumn(
		name = "fk_usuario_id",
		referencedColumnName = "id",
		foreignKey = @ForeignKey(name="fk_usuario_id")
	)
	private Usuario usuario_id;
	
	private boolean isSubscribed;
	
	private boolean isPresent;
	
	public EventoUsuario() {
		
	}

	public Evento getEvento() {
		return evento_id;
	}

	public void setEvento(Evento evento) {
		this.evento_id = evento;
	}

	public Usuario getUsuario() {
		return usuario_id;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario_id = usuario;
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
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.evento_id);
        hash = 17 * hash + Objects.hashCode(this.usuario_id);
        hash = 17 * hash + (this.isSubscribed ? 1 : 0);
        hash = 17 * hash + (this.isPresent ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EventoUsuario other = (EventoUsuario) obj;
        if (this.isSubscribed != other.isSubscribed) {
            return false;
        }
        if (this.isPresent != other.isPresent) {
            return false;
        }
        if (!Objects.equals(this.evento_id, other.evento_id)) {
            return false;
        }
        if (!Objects.equals(this.usuario_id, other.usuario_id)) {
            return false;
        }
        return true;
        
    }

    @Override
    public String toString() {
        return "EventoUsuario{" + "evento_id=" + evento_id + ", usuario_id=" + usuario_id + ", isSubscribed=" + isSubscribed + ", isPresent=" + isPresent + '}';
    }

    
	
}
