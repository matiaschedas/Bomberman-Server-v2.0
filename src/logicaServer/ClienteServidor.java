package logicaServer;

import java.util.ArrayList;
import com.google.gson.Gson;

import logicaServer.Sala;
import hilosDelJuego.HiloFinDeRonda;
import logicaJuego.Mapa;

public class ClienteServidor {

	private static ClienteServidor cliente;
	private ArrayList<ClienteSocket> clientes;
	private ArrayList<RefrescarMapa> hilosRefrescar;
	private ArrayList<HiloFinDeRonda> hilosFinRonda;
	private int idBombas;
	
	
	

	
	
	public ClienteServidor() {
		clientes = new ArrayList<ClienteSocket>();
		hilosRefrescar = new ArrayList<RefrescarMapa>();
		hilosFinRonda=new ArrayList<HiloFinDeRonda>();
		
		idBombas=0;
	}	
	public void agregarCliente(ClienteSocket cliente) {
			this.clientes.add(cliente);	
	}

	public void sacarCliente(ClienteSocket cliente) {
		clientes.remove(cliente);
	}
	
	public static ClienteServidor sharedInstance() {
		if(cliente == null){
			cliente= new ClienteServidor();
		}
		return cliente;
	}
	public int obtenerIDBomba() {
		if(idBombas>1000)
			idBombas=0;
		return idBombas++;
	}
	
	public void distribuir(String mensaje) {
		for(int i=0;i<clientes.size();i++) {
			clientes.get(i).writeMessage(mensaje);
		}
	}
	
	public void distribuirASala(Sala sala,String msj) {
		Mensaje mensajes = new Mensaje();
		Gson gson = new Gson();
		mensajes.setTipo(msj);
		mensajes.setSala(sala);
		mensajes.setMsj(sala.obtenerJugadores());
		mensajes.setPropietarioMsj(sala.getNombreLider());

		String msj1 = gson.toJson(mensajes, Mensaje.class);
		ArrayList<String> jugadores = new ArrayList<>();
		jugadores=sala.getJugadores();
		for(int i=0; i<jugadores.size();i++) {
			for(int j=0; j<clientes.size();j++) {
				if(jugadores.get(i).equals(clientes.get(j).getNombre()))
					clientes.get(j).writeMessage(msj1);
			}
		}
		
		ArrayList<String> espectadores = new ArrayList<>();
		espectadores=sala.getEspectadores();
		for(int i=0; i<espectadores.size();i++) {
			for(int j=0; j<clientes.size();j++) {
				if(espectadores.get(i).equals(clientes.get(j).getNombre()))
					clientes.get(j).writeMessage(msj1);
			}
		}
		
	}
	public void distribiurASala(Sala sala, String msj, String tipo) {
		Mensaje mensajes = new Mensaje();
		Gson gson = new Gson();
		mensajes.setTipo(tipo);
		mensajes.setSala(sala);
		mensajes.setMsj(msj);
		mensajes.setPropietarioMsj(sala.getNombreLider());

		String msj1 = gson.toJson(mensajes, Mensaje.class);
		ArrayList<String> jugadores = new ArrayList<>();
		jugadores=sala.getJugadores();
		for(int i=0; i<jugadores.size();i++) {
			for(int j=0; j<clientes.size();j++) {
				if(jugadores.get(i).equals(clientes.get(j).getNombre()))
					clientes.get(j).writeMessage(msj1);
			}
		}
		
		ArrayList<String> espectadores = new ArrayList<>();
		espectadores=sala.getEspectadores();
		for(int i=0; i<espectadores.size();i++) {
			for(int j=0; j<clientes.size();j++) {
				if(espectadores.get(i).equals(clientes.get(j).getNombre()))
					clientes.get(j).writeMessage(msj1);
			}
		}
	}
	public void distribuirMensajeCliente(ClienteSocket cliente, Sala sala, String msj, String tipo) {
		Mensaje mensajes = new Mensaje();
		Gson gson = new Gson();
		mensajes.setTipo(tipo);
		mensajes.setSala(sala);
		mensajes.setMsj(msj);
		mensajes.setPropietarioMsj(cliente.getNombre());
		
		
		String msj1 = gson.toJson(mensajes, Mensaje.class);	
		cliente.writeMessage(msj1);
			
	}
	
	public void distribuirMensajeCliente(String nombre, Sala sala, String msj) {
		Mensaje mensajes = new Mensaje();
		Gson gson = new Gson();
		mensajes.setTipo("mensajeChat");
		mensajes.setMsj(msj);
		mensajes.setPropietarioMsj(nombre);
		
		
		String msj1 = gson.toJson(mensajes, Mensaje.class);
		ArrayList<String> jugadores = new ArrayList<>();
		jugadores=sala.getJugadores();
		for(int i=0; i<jugadores.size();i++) {
			for(int j=0; j<clientes.size();j++) {
				if(jugadores.get(i).equals(clientes.get(j).getNombre()))
					clientes.get(j).writeMessage(msj1);
			}
		}
	}
	
	public void refrescarMapa(Sala sala) {
		ArrayList<String> jugadores = new ArrayList<>();
		jugadores=sala.getJugadores();
		for(int i=0; i<jugadores.size();i++) {
			for(int j=0; j<clientes.size();j++) {
				if(jugadores.get(i).equals(clientes.get(j).getNombre()))
					hilosRefrescar.add(new RefrescarMapa(sala,clientes.get(j)));
			}
		}			
		for(int i=0; i<hilosRefrescar.size();i++) {
			if(hilosRefrescar.get(i).getSala().getIdSala()==sala.getIdSala())
				if(hilosRefrescar.get(i)!=null)
					hilosRefrescar.get(i).start();
		}
	}
	public void distribuirMapaACliente (ClienteSocket cliente,Sala sala) {
		Mensaje mensajes = new Mensaje();
		Gson gson = new Gson();
		MensajeMapa mensajeMapa=new MensajeMapa();
		
		mensajes.setTipo("actualizarMapa");
		mensajes.setPropietarioMsj(sala.getNombreLider());
		
		mensajeMapa.cargarAtributos(sala.getMapa());
		mensajes.setMapa(mensajeMapa);
		
		String msj1 = gson.toJson(mensajes, Mensaje.class);
		cliente.writeMessage(msj1);
			
	}	
	
	public void distribuirMatrices (Sala sala,String tipo) {
		Mensaje mensajes = new Mensaje();
		Gson gson = new Gson();
		MensajeMapa mensajeMapa=new MensajeMapa();
		
		mensajes.setTipo(tipo);
		mensajes.setId(sala.getIdSala());
		
		Mapa mapa=sala.getMapa();
		mensajeMapa.cargarAtributos(mapa);
		mensajes.setMapa(mensajeMapa);
		mensajes.setPropietarioMsj(sala.getNombreLider());
		
		String msj1 = gson.toJson(mensajes, Mensaje.class);
		ArrayList<String> jugadores = new ArrayList<>();
		jugadores=sala.getJugadores();
		for(int i=0; i<jugadores.size();i++) {
			for(int j=0; j<clientes.size();j++) {
				if(jugadores.get(i).equals(clientes.get(j).getNombre()))
					clientes.get(j).writeMessage(msj1);
			}
		}
	}	
	
	public void setearBombermans(Sala sala) {
		
		ArrayList<String> jugadores = new ArrayList<>();
		jugadores=sala.getJugadores();
		for(int i=0; i<jugadores.size();i++) {
			for(int j=0; j<clientes.size();j++) {
				if(jugadores.get(i).equals(clientes.get(j).getNombre())) {
					clientes.get(j).setBomber(sala.getMapa().getJugadores().get(i));				
				}
			}
		}
	}

	public void messageRecived(int client, String message){
		System.out.println("Cliente: " + client + " mando: " + message);		
	}
	

	public ArrayList<ClienteSocket> getClientes() {
		return clientes;
	}

	public void iniciarHilosMov(Sala sala) {
		
		ArrayList<String> jugadores = new ArrayList<>();
		jugadores=sala.getJugadores();
		for(int i=0; i<jugadores.size();i++) {
			for(int j=0; j<clientes.size();j++) {
				if(jugadores.get(i).equals(clientes.get(j).getNombre())) {
					clientes.get(j).setHiloMov(new MovimientoServer(clientes.get(j).getBomber(), sala));
					clientes.get(j).getHiloMov().start();
				}
			}
		}
	}
	
	public void pararHilosSala(Sala sala) {
		ArrayList<RefrescarMapa> borrar = new ArrayList<>();
		ArrayList<String> jugadores = new ArrayList<>();
		jugadores=sala.getJugadores();
		for(int i=0; i<jugadores.size();i++) {
			for(int j=0; j<clientes.size();j++) {
				if(jugadores.get(i).equals(clientes.get(j).getNombre())) {
					if(clientes.get(j).getHiloMov()!=null)
						clientes.get(j).getHiloMov().finalizarHilo();
				}
			}
		}
		
		for(int i=0; i<hilosRefrescar.size();i++) {
			if(hilosRefrescar.get(i).getSala().getIdSala()==sala.getIdSala()) {
				if(hilosRefrescar.get(i)!=null) {
					hilosRefrescar.get(i).finalizarHilo();
					borrar.add(hilosRefrescar.get(i));
				}	
			}
		}
		hilosRefrescar.removeAll(borrar);
	}

	public void paraHilosCliente(String nombre) {
		boolean bandera = false;
		for (int j = 0; j < clientes.size() && bandera == false; j++) {
			if (clientes.get(j).getNombre().equals(nombre)) {
				this.pararHilosCliente(clientes.get(j));
				bandera = true;
			}
		}
	}

	public void pararHilosCliente(ClienteSocket cliente) {
		for(int i =0;i<clientes.size();i++) {
			if(clientes.get(i)==cliente) {
				if(clientes.get(i).getHiloMov()!=null)
					clientes.get(i).getHiloMov().finalizarHilo();
			}
		}
	
		for(int i =0;i<hilosRefrescar.size();i++) {
			if(hilosRefrescar.get(i).getCliente()==cliente) {
				if(hilosRefrescar.get(i)!=null)
					hilosRefrescar.get(i).finalizarHilo();
			}
		}
		
	}
	
	public void modoEspectador(Sala sala, ClienteSocket cliente) {
		hilosRefrescar.add(new RefrescarMapa(sala,cliente));
		hilosRefrescar.get(hilosRefrescar.size()-1).start();
	}

	public void iniciarHiloFinRonda(Sala sala) {
		HiloFinDeRonda hiloFinRonda =new HiloFinDeRonda(sala.getMapa(), sala);
		hilosFinRonda.add(hiloFinRonda);		
		hiloFinRonda.start();
	}
	
	public void reanudarHiloFinRonda(Sala sala) {
		for(int i=0;i<hilosFinRonda.size();i++) {
			if(hilosFinRonda.get(i).getSala().getIdSala()==sala.getIdSala()) {
				hilosFinRonda.get(i).finalizarHilo();
				hilosFinRonda.get(i).reanudarHilo(sala.getMapa());
			}
		}
	}
	
	public void finalizarJuego(Sala sala) {
		int pos=-1;
		for (int i = 0; i < hilosFinRonda.size(); i++) {
			if (hilosFinRonda.get(i).getSala().getIdSala() == sala.getIdSala()) {
				pos=i;
			}
		}
		hilosFinRonda.remove(pos);
		distribuirASala(sala, "finalizarPartida");
		pararHilosSala(sala);
		Servidor.sharedInstance().eliminarSala(sala);
		distribuirASala(sala, "eliminarSala");
		
	}

	public void reanudarEspectadores(Sala sala) {
		ArrayList<Integer> posiciones = new ArrayList<Integer>();
		ArrayList<String> espectadores = new ArrayList<>();
		espectadores=sala.getEspectadores();
		for(int i=0; i<espectadores.size();i++) {
			for(int j=0; j<clientes.size();j++) {
				if(espectadores.get(i).equals(clientes.get(j).getNombre())) {
					hilosRefrescar.add(new RefrescarMapa(sala,clientes.get(j)));
					posiciones.add(hilosRefrescar.size()-1);
				}
			}
		}
		for(int i = 0; i < posiciones.size() ; i++ ) {
			hilosRefrescar.get(posiciones.get(i)).start();
		}		
	}
	

}

