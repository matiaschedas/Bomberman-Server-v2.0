package logicaServer;

import java.io.Serializable;
import java.util.ArrayList;

import logicaServer.Sala;

public class Mensaje implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tipo;
	private ArrayList<Sala> salas;
	private Sala sala;
	private int id;
	private String msj;
	private MensajeMapa mapa;
	private String propietarioMsj;
	private boolean espectador;
	
	
	public boolean isEspectador() {
		return espectador;
	}
	public void setEspectador(boolean espectador) {
		this.espectador = espectador;
	}
	public ArrayList<Sala> getSalas() {
		return salas;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMsj() {
		return msj;
	}

	public void setMsj(String msj) {
		this.msj = msj;
	}

	public String getTipo() {
		return tipo;
	}

	public void setSalas(ArrayList<Sala> salas) {
		this.salas = salas;
	}

	public MensajeMapa getMapa() {
		return mapa;
	}

	public void setMapa(MensajeMapa mapa) {
		this.mapa = mapa;
	}
	public String getPropietarioMsj() {
		return propietarioMsj;
	}
	public void setPropietarioMsj(String propietarioMsj) {
		this.propietarioMsj = propietarioMsj;
	}

	
}
