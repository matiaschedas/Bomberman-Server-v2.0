package logicaServer;

import java.io.Serializable;
import java.util.ArrayList;

import hilosDelJuego.HiloBuffos;
import logicaJuego.Bomberman;
import logicaJuego.Mapa;
import logicaServer.ClienteServidor;

public class Sala implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombre;
	private String nombreLider;
	private ArrayList<String> jugadores;
	private int puntajes[];
	private int idSala;
	private int cantJugadores;
	private Mapa mapa;
	private boolean partidaComenzada;
	private String pass;
	private ArrayList<String>espectadores;


	public Sala(String n, int id,String nombreLider, String pass) {
		nombre=n;
		idSala=id;
		cantJugadores=1;
		this.nombreLider=nombreLider;
		jugadores=new ArrayList<>();
		mapa=new Mapa();
		jugadores.add(nombreLider);
		partidaComenzada=false;
		this.pass=pass;		
		this.puntajes = new int[4];
		this.espectadores=new ArrayList<String>();
	}

	public void eliminarJugador(String nombre) {
		boolean eliminado = jugadores.remove(nombre);
		espectadores.remove(nombre);
		if(eliminado)
			cantJugadores--;
		if (partidaComenzada)
			ClienteServidor.sharedInstance().paraHilosCliente(nombre);
	}
	
	@Override
	public String toString() {
		if (partidaComenzada) {
			if (pass.equals(""))
				return String.format("|%-20s|", nombre) + String.format("id: |%-5d|", idSala)
						+ String.format("jugadores: |%-5d|", cantJugadores) + " Comenzada";
			else
				return String.format("|%-20s|", nombre) + String.format("id: |%-5d|", idSala)
						+ String.format("jugadores: |%-5d|", cantJugadores) + " Privada" + " Comenzada";
		} else {

			if (pass.equals(""))
				return String.format("|%-20s|", nombre) + String.format("id: |%-5d|", idSala)
						+ String.format("jugadores: |%-5d|", cantJugadores);
			else
				return String.format("|%-20s|", nombre) + String.format("id: |%-5d|", idSala)
						+ String.format("jugadores: |%-5d|", cantJugadores) + " Privada";
		}
	}
	
	public String obtenerJugadores() {
		String jugadores = new String();
		
		if(this.jugadores.size()>0) {			
			jugadores =  jugadores.concat(this.jugadores.get(0)+" (Lider)"+"\n");
		}
		
		for(int i = 1; i < this.jugadores.size();i++) {
			jugadores = jugadores.concat(this.jugadores.get(i)+"\n");
		}
		return jugadores;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public int getIdSala() {
		return idSala;
	}
	
	public int getCantJugadores() {
		return cantJugadores;
	}
	
	public void agregarJugador(String jugador) {
		if(!partidaComenzada) {
			this.jugadores.add(jugador);
			cantJugadores++;
		}
		else 
			this.espectadores.add(jugador);
	}

	public ArrayList<String> getJugadores() {
		return jugadores;
	}
	public String getNombreLider() {
		return nombreLider;
	}

	public void setNombreLider(String nombreLider) {
		this.nombreLider = nombreLider;
	}

	public Mapa getMapa() {
		return mapa;
	}

	public void setMapa(Mapa mapa) {
		this.mapa = mapa;
	}

	public void iniciarPartida() {
		for(int i=0 ; i < jugadores.size();i++) {
			mapa.creaBomberman(jugadores.get(i));
			HiloBuffos hiloBuff = new HiloBuffos (mapa.getJugadores().get(i));
			hiloBuff.start();			
		}
		
		
		this.partidaComenzada=true;
	}
	
	public boolean isPartidaComenzada() {
		return partidaComenzada;
	}

	public void setPartidaComenzada(boolean partidaComenzada) {
		this.partidaComenzada = partidaComenzada;
	}

	public Bomberman obtenerBomber(String nombre) {
		int pos = -1;
		for (int i = 0; i < jugadores.size(); i++) {
			if (nombre.equals(jugadores.get(i)))
				pos = i;
		}
		if (pos != -1)
			return mapa.getJugadores().get(pos);
		return null;
	}

	public void finalizarRonda() {
		
		boolean terminoPartida=false;
		
		for(int i =0;i<mapa.getJugadores().size();i++) {
			if(mapa.getJugadores().get(i).getCantMuertes()<3)
				mapa.getJugadores().get(i).setPuntaje();
		}
		for(int i =0;i<mapa.getJugadores().size();i++) {
			if(mapa.getJugadores().get(i).getPuntaje()==3) {
				terminoPartida=true;
			}
		}
		if(terminoPartida) {
			for(int i = 0 ; i < jugadores.size(); i++) {
				for(int j = 0 ; j < mapa.getJugadores().size();j++) {
					if(mapa.getJugadores().get(j).getNombre().equals(jugadores.get(i))){						
						puntajes[i]=mapa.getJugadores().get(j).getPuntaje();
					}
					
				}				
			}
			
			for(int i = 0 ; i < mapa.getJugadores().size();i++) {
				mapa.getJugadores().get(i).setPuntajes(puntajes[i]);
			}
			ClienteServidor.sharedInstance().finalizarJuego(this);
		}
		else {
			for(int i = 0 ; i < jugadores.size(); i++) {
				for(int j = 0 ; j < mapa.getJugadores().size();j++) {
					if(mapa.getJugadores().get(j).getNombre().equals(jugadores.get(i))){						
						puntajes[i]=mapa.getJugadores().get(j).getPuntaje();
					}
					
				}				
			}
			mapa=new Mapa();
			
			
			this.iniciarPartida();
			ClienteServidor.sharedInstance().pararHilosSala(this);
			ClienteServidor.sharedInstance().setearBombermans(this);
			
			for(int i = 0 ; i < mapa.getJugadores().size();i++) {
				mapa.getJugadores().get(i).setPuntajes(puntajes[i]);
			}
			
			ClienteServidor.sharedInstance().iniciarHilosMov(this);
			ClienteServidor.sharedInstance().refrescarMapa(this);			
			ClienteServidor.sharedInstance().reanudarHiloFinRonda(this);
			
			ClienteServidor.sharedInstance().reanudarEspectadores(this);
			
		}
	}

	public void finalizarRondaTiempo() {
		int menor = 5;
		boolean terminoPartida=false;
		
		for (int i = 0; i < mapa.getJugadores().size(); i++) {
			if (mapa.getJugadores().get(i).getCantMuertes() < menor) 
				menor = mapa.getJugadores().get(i).getCantMuertes();
		}
		for (int i = 0; i < mapa.getJugadores().size(); i++) {
			if (mapa.getJugadores().get(i).getCantMuertes() == menor) 
				mapa.getJugadores().get(i).setPuntaje();
		}
		
		for(int i =0;i<mapa.getJugadores().size();i++) {
			if(mapa.getJugadores().get(i).getPuntaje()==3) {
				terminoPartida=true;
			}
		}
		if(terminoPartida) {
			for(int i = 0 ; i < jugadores.size(); i++) {
				for(int j = 0 ; j < mapa.getJugadores().size();j++) {
					if(mapa.getJugadores().get(j).getNombre().equals(jugadores.get(i))){						
						puntajes[i]=mapa.getJugadores().get(j).getPuntaje();
					}
					
				}				
			}
			
			for(int i = 0 ; i < mapa.getJugadores().size();i++) {
				mapa.getJugadores().get(i).setPuntajes(puntajes[i]);
			}
			ClienteServidor.sharedInstance().finalizarJuego(this);
		}
		else {
				for(int i = 0 ; i < jugadores.size(); i++) {
					for(int j = 0 ; j < mapa.getJugadores().size();j++) {
						if(mapa.getJugadores().get(j).getNombre().equals(jugadores.get(i))){						
							puntajes[i]=mapa.getJugadores().get(j).getPuntaje();
						}
						
					}				
				}
				mapa=new Mapa();
				
				
				this.iniciarPartida();
				ClienteServidor.sharedInstance().pararHilosSala(this);
				ClienteServidor.sharedInstance().setearBombermans(this);
				
				for(int i = 0 ; i < mapa.getJugadores().size();i++) {
					mapa.getJugadores().get(i).setPuntajes(puntajes[i]);
				}
				
				ClienteServidor.sharedInstance().iniciarHilosMov(this);
				ClienteServidor.sharedInstance().refrescarMapa(this);			
				ClienteServidor.sharedInstance().reanudarHiloFinRonda(this);
				
				ClienteServidor.sharedInstance().reanudarEspectadores(this);
				
			}
	}

	
	

	public ArrayList<String> getEspectadores() {
		return espectadores;
	}

	public void setEspectadores(ArrayList<String> espectadores) {
		this.espectadores = espectadores;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public int[] getPuntajes() {
		return puntajes;
	}

	public void setPuntajes(int[] puntajes) {
		this.puntajes = puntajes;
	}

	public void borrarJugadores() {
		this.jugadores=new ArrayList<>();
		this.espectadores=new ArrayList<>();
	}

	
}