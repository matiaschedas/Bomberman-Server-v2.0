package logicaJuego;

import java.io.Serializable;
 
public class Bomba extends Entidad implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int rango;
	private int tiempoDeEjecucion;
	private boolean explotada;
	private int id;
	
	public Bomba(int x, int y, int rango) {
		super(x, y);
		this.rango = rango;
		this.tiempoDeEjecucion = 3;
		this.explotada=false;
	}

	public Bomba explotar(Mapa mapa) {
		this.explotada = true;
		Bomba encontrada = null;

		int deltas[][] = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };
		boolean validas[] = { false, false, false, false };
		int dx, dy;
		Bloque transit = new Bloque("transitable", this.getX(), this.getY());
		mapa.setMatrizCosas(transit, this.getX(), this.getY());// saco la bomba de la interfaz
		Bloque fuegardo = new Bloque("fuego", this.getX(), this.getY());
		fuegardo.setId(this.id);

		mapa.setMatrizMapa(fuegardo, this.getX(), this.getY());
		for (int i = 0; i < mapa.getJugadores().size(); i++) {// este for mata bomber si esta en las coordenadas de la
																// bomba
			if (this.getX() == mapa.getJugadores().get(i).getPosX()
					&& this.getY() == mapa.getJugadores().get(i).getPosY()) {
				Bloque fuego = new Bloque("fuego", this.getX(), this.getY());
				fuego.setId(this.id);
				mapa.setMatrizMapa(fuego, this.getX(), this.getY());
				mapa.getJugadores().get(i).muere(mapa);
			}
		}

		for (int i = 0; i < this.rango; i++) {
			for (int j = 0; j < deltas.length; j++) {
				dx = this.getX() + (deltas[j][0]);
				dy = this.getY() + (deltas[j][1]);
				Entidad matriz[][] = (Entidad[][]) mapa.getMatrizMapa();

				if (dx > 0 && dy > 0 && dx < matriz.length && dy < matriz[0].length) {
					if (validas[j] == false && mapa.getPosicionMapa(dx, dy).esBloque()) {
						if (((Bloque) mapa.getPosicionMapa(dx, dy)).queTipo().equals("pared")
								|| ((Bloque) mapa.getPosicionMapa(dx, dy)).queTipo().equals("obstaculo")) {
							validas[j] = true;
						} else if (((Bloque) mapa.getPosicionMapa(dx, dy)).queTipo().equals("piedra")) {
							Bloque fuegoPiedra = new Bloque("fuegoPiedra", dx, dy);
							fuegoPiedra.setId(this.id);
							mapa.setMatrizMapa(fuegoPiedra, dx, dy);
							validas[j] = true;
						} else if (((Bloque) mapa.getPosicionMapa(dx, dy)).queTipo().equals("transitable")) {
							Bloque fuego = new Bloque("fuego", dx, dy);
							Bloque fuego2 = new Bloque("fuego", this.getX(), this.getY());
							fuego2.setId(this.id);
							fuego.setId(this.id);
							mapa.setMatrizMapa(fuego2, this.getX(), this.getY());
							mapa.setMatrizMapa(fuego, dx, dy);
						} else if (((Bloque) mapa.getPosicionMapa(dx, dy)).queTipo().equals("velocidad")
								|| ((Bloque) mapa.getPosicionMapa(dx, dy)).queTipo().equals("rango")
								|| ((Bloque) mapa.getPosicionMapa(dx, dy)).queTipo().equals("masBombas")) {
							Bloque fuego = new Bloque("fuego", dx, dy);
							fuego.setId(this.id);
							mapa.setMatrizMapa(fuego, dx, dy);
						}
					}
					if (validas[j] == false) {
						for (int k = 0; k < mapa.getJugadores().size(); k++) {
							if (dx == mapa.getJugadores().get(k).getPosX()
									&& dy == mapa.getJugadores().get(k).getPosY()) {
								Bloque fuego = new Bloque("fuego", dx, dy);
								fuego.setId(this.id);
								mapa.setMatrizMapa(fuego, dx, dy);
								mapa.getJugadores().get(k).muere(mapa);
							}
						}
					}
					if (validas[j] == false && mapa.getPosicionMatrizCosas(dx, dy).esBomba()) {
						encontrada = ((Bomba) mapa.getPosicionMatrizCosas(dx, dy));
						encontrada.setTiempoDeEjecucion(0);
						validas[j] = true;
					}
				}
			}
			deltas[0][0] += 1;
			deltas[1][1] += 1;
			deltas[2][0] -= 1;
			deltas[3][1] -= 1;
		}
		return encontrada;
	}

	public void sacarFuego(Mapa map) {
		
		int deltas[][] = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };
		int dx, dy;
		Entidad matriz[][] = (Entidad[][])map.getMatrizMapa();
			map.setMatrizMapa(new Bloque("transitable", this.getX(),this.getY()),this.getX(),this.getY());
		for (int i = 0; i <= this.rango; i++) {
			for (int j = 0; j < deltas.length; j++) {
				dx = this.getX() + (deltas[j][0]);
				dy = this.getY() + (deltas[j][1]);
				if (dx >= 0 && dy >= 0 && dx < matriz.length && dy < matriz[0].length)
					if (map.getPosicionMapa(dx, dy).esBloque()) {
						if (((Bloque) map.getPosicionMapa(dx, dy)).queTipo().equals("fuego"))
							map.setMatrizMapa(new Bloque("transitable", dx, dy), dx, dy);
						else if(((Bloque) map.getPosicionMapa(dx, dy)).queTipo().equals("fuegoPiedra")) {
							map.crearBuff(dx, dy);
						}
					}
			}
			deltas[0][0] += 1;
			deltas[1][1] += 1;
			deltas[2][0] -= 1;
			deltas[3][1] -= 1;
		}
	}


	public int getRango() {
		return rango;
	}

	public void setRango(int rango) {
		this.rango = rango;
	}

	public int getTiempoDeEjecucion() {
		return tiempoDeEjecucion;
	}

	public void setTiempoDeEjecucion(int tiempoDeEjecucion) {
		this.tiempoDeEjecucion = tiempoDeEjecucion;
	}

	@Override
	public String toString() {
		return "Bomba";
	}

	@Override
	public boolean esBloque() {
		return false;
	}

	@Override
	public boolean esBomberman() {
		return false;
	}

	@Override
	public boolean esBomba() {
		return true;
	}
	
	public boolean isExplotada() {
		return explotada;
	}

	public void setExplotada(boolean explotada) {
		this.explotada = explotada;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}