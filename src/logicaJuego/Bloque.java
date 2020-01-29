package logicaJuego;

import java.io.Serializable;

public class Bloque extends Entidad implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//tipos de bloques
	private String tipoBloque;
	private int id;


	public Bloque(String tipo,int x, int y) {
		super(x,y);
		this.tipoBloque=tipo;		
	}

	@Override
	public String toString() {
		return tipoBloque;
	}
	
	public String queTipo() {
		return this.tipoBloque;
	}
	
	public void explotarPiedra() {
		this.tipoBloque="transitable";
	}
	
	@Override
	public boolean esBloque() {
		return true;
	}
	@Override 
	public boolean esBomberman() {
		return false;
	}
	@Override 
	public boolean esBomba() {
		return false;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
}