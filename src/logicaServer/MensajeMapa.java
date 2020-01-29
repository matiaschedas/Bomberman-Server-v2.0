package logicaServer;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import logicaJuego.Bloque;
import logicaJuego.Bomba;
import logicaJuego.Bomberman;
import logicaJuego.Entidad;
import logicaJuego.Mapa;

public class MensajeMapa implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<Bloque> bloques;
	private ArrayList<Bomba> bombas;

	private ArrayList<Bloque> bloquesCosas;
	private ArrayList<Bomba> bombasCosas;
	
	private ArrayList<Bomberman> bombers;
	
	private ArrayList<ImageIcon>bomberImagen;
	
	private int col;
	private int fil;

	public MensajeMapa() {
		bloques = new ArrayList<>();
		bombas = new ArrayList<>();
		bloquesCosas = new ArrayList<>();
		bombasCosas = new ArrayList<>();
		bombers = new ArrayList<>();
		bomberImagen=new ArrayList<>();
		col=15;
		fil=11;
	}

	public void cargarAtributos(Mapa mapa) {
		for (int i = 0; i < mapa.getFIL(); i++) {
			for (int j = 0; j < mapa.getCOL(); j++) {
				if ((mapa.getPosicionMapa(i, j)).esBloque())
					bloques.add((Bloque) mapa.getPosicionMapa(i, j));
				if ((mapa.getPosicionMapa(i, j)).esBomba())
					bombas.add((Bomba) mapa.getPosicionMapa(i, j));
			}
		}

		for (int i = 0; i < mapa.getFIL(); i++) {
			for (int j = 0; j < mapa.getCOL(); j++) {
				if ((mapa.getPosicionMatrizCosas(i, j)).esBloque())
					bloquesCosas.add((Bloque) (mapa.getPosicionMatrizCosas(i, j)));
				if ((mapa.getPosicionMatrizCosas(i, j)).esBomba())
					bombasCosas.add((Bomba) mapa.getPosicionMatrizCosas(i, j));
			}
		}
		for(int i=0 ;i<mapa.getJugadores().size();i++) {
			bombers.add(mapa.getJugadores().get(i));
			bomberImagen.add(mapa.getJugadores().get(i).getBomberman());
			
		}
		
	}
	
	public Entidad[][] descargarMatrizMapa() {
		Entidad[][] matrizMapa = new Entidad[fil][col]; 
		int posX;
		int posY;
		for(int i=0; i<bloques.size();i++) {
			posX = bloques.get(i).getX();
			posY = bloques.get(i).getY();
			matrizMapa[posX][posY]=bloques.get(i);
		}
		for(int j=0; j<bombas.size();j++) {
			posX = bombas.get(j).getX();
			posY = bombas.get(j).getY();
			matrizMapa[posX][posY]=bombas.get(j);
		}
		return matrizMapa;
	}
	
	public Entidad[][] descargarMatrizCosas() {
		Entidad[][] matrizCosas = new Entidad[fil][col]; 
		int posX;
		int posY;
		for(int i=0; i<bloquesCosas.size();i++) {
			posX = bloquesCosas.get(i).getX();
			posY = bloquesCosas.get(i).getY();
			matrizCosas[posX][posY]=bloquesCosas.get(i);
		}
		for(int j=0; j<bombasCosas.size();j++) {
			posX = bombasCosas.get(j).getX();
			posY = bombasCosas.get(j).getY();
			matrizCosas[posX][posY]=bombasCosas.get(j);
		}
		return matrizCosas;
	}
	
	public ArrayList<Bomberman> descargarBombers(){
		return bombers;
	}
	
	public ArrayList<ImageIcon> descargarImageIcons(){
		return bomberImagen;
	}
}
