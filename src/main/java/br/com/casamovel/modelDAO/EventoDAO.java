package br.com.casamovel.modelDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.casamovel.connection.ConnectionFactory;
import br.com.casamovel.model.Evento;

public class EventoDAO {
	
	public ArrayList<Evento> getEvento() {
		Connection con = ConnectionFactory.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Evento> listaEvento = new ArrayList<Evento>(); 
		
		try {
			stmt = con.prepareStatement("SELECT * FROM evento");
			rs= stmt.executeQuery();
			while (rs.next()) {
				Evento evento = new Evento();
				evento.setId(rs.getInt("id"));
				evento.setTitulo(rs.getString("titulo"));
				evento.setCargaHoraria(rs.getDouble("carga"));
				listaEvento.add(evento);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao conectar",e);
		}
		return listaEvento;	
	}
}
