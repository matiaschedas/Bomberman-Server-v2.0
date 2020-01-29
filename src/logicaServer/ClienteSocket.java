package logicaServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;

import logicaServer.Sala;
import logicaJuego.Bomberman;

public class ClienteSocket extends Thread implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Socket socket;
	private String nombre;
	private int nroCliente;
	private boolean logueado;
	private boolean flag;
	private Sala sala;
	private Bomberman bomber;
	private MovimientoServer hiloMov;
	private int puntaje;

	

	public ClienteSocket(Socket socket, int nro){
		this.socket = socket;
		this.nroCliente = nro;
		this.logueado = false;
		flag=true;	
		puntaje=0;
	}

	/*
	 * Permito asociar el cliente a un nombre
	 * el nombre es post login por lo cual no lo inicializo con el
	 */
	public void setNombre(String nombre){
		this.nombre = nombre;
	}
	
	/*
	 * Permite setear el estado si esta o no logueado
	 */
	public void setLogueado(boolean log){
		this.logueado = log;
	}
	
	/*
	 * Devuelve true si el usuario se ha logueado
	 * y false si no lo esta
	 */
	public boolean estaLogueado(){
		return this.logueado;
	}
	
	/*
	 * Permito acceder al socket cuando se requiera
	 *  una comunicacion particular
	 */
	public Socket getSocket(){
		return this.socket;
	}
	
	/*
	 * Permito acceder al nombre cuando se
	 * lo busque para listarlo por su nombre
	 */
	public String getNombre(){
		return this.nombre;
	}
	
	/*
	 * Permito el acceso a su identificador principal
	 */
	public int getNumeroCliente(){
		return this.nroCliente;
	}
	
	/*
	 * Thread que escucha al cliente
	 * recibe el mensaje y se lo pasa al serviceManager
	 * quien se encargar‡ de determinar que tipo de mensaje es y que hacer con el
	 * 
	 */
	public void run() {
		
		while (true) {
			try {
				ArrayList <Sala> salas = Servidor.sharedInstance().devolverSalas();
				DataInputStream f = new DataInputStream(this.socket.getInputStream());
				String message = f.readUTF();
				String m[];
				m=message.split("/");
				String msj1;
				Gson gson = new Gson();
				Mensaje mensajes = new Mensaje();
				
				if(flag==true) {
					this.nombre=message;
					flag=false;
				}
				else {
					switch(m[0]) {
					case "crearSala":
						if(m.length>2)
							sala =Servidor.sharedInstance().agregarSala(m[1],m[2],this.nombre);
						else
							sala =Servidor.sharedInstance().agregarSala(m[1],this.nombre);
						int id = sala.getIdSala();
						mensajes.setTipo("crearSala");
						mensajes.setId(id);
						
						msj1 = gson.toJson(mensajes, Mensaje.class);
						this.writeMessage(msj1);
						break;
						
					case "unirseSala":
						boolean encontrado = false;
						boolean puedeEntrar = false;
						
						
						
						int k = 0;
						for (; k < salas.size() && encontrado == false; k++) {
							if (salas.get(k).getIdSala() == Integer.parseInt(m[1])) {
								encontrado = true;
								sala = salas.get(k);
							}
						}
						if (encontrado == true) {
							
							if (sala.getPass().equals("")) {
								puedeEntrar = true;
							} else if (m.length > 2) {
								if (sala.getPass().equals(m[2])) {
									puedeEntrar = true;
								}
							}
							if (sala.getCantJugadores() < 4 && !sala.isPartidaComenzada()) {
								if (puedeEntrar) {
									sala.agregarJugador(this.nombre);
									if (sala.isPartidaComenzada() != true) {
										mensajes.setTipo("unirseSala");
										mensajes.setSala(sala);

										msj1 = gson.toJson(mensajes, Mensaje.class);
										this.writeMessage(msj1);

										ClienteServidor.sharedInstance().distribuirASala(sala, "actualizarSala");
									} else {
										this.bomber = null;
										ClienteServidor.sharedInstance().modoEspectador(sala, this);
									}
								}
							}
							else if(sala.isPartidaComenzada()) {
									this.bomber = null;
									ClienteServidor.sharedInstance().modoEspectador(sala, this);
							}
							else
								ClienteServidor.sharedInstance().distribuirMensajeCliente(this, sala,"", "salaLlena");
						} 
						if(encontrado==false || puedeEntrar==false) {
							mensajes.setTipo("errorUnirse");
							mensajes.setMsj(m[1]);
							msj1 = gson.toJson(mensajes, Mensaje.class);
							this.writeMessage(msj1);
						}
						break;
					case "listarSalas":
						
						if(salas.size()>0) {
							msj1=new String(salas.get(0).toString());
							msj1=msj1.concat("\n");
							for(int i =1 ; i<salas.size();i++) {
								msj1=msj1.concat(salas.get(i).toString());
								msj1=msj1.concat("\n");
							}
							mensajes.setTipo("listarSalas");
							mensajes.setMsj(msj1);
							
							
							msj1= gson.toJson(mensajes, Mensaje.class);
							this.writeMessage(msj1);
						}
						else {
							mensajes.setTipo("listarSalas");
							mensajes.setMsj("");								
							msj1 = gson.toJson(mensajes, Mensaje.class);
							this.writeMessage(msj1);
						}
						break;
					case "eliminarSala":
						int i=0;
						int idRecibido = Integer.parseInt(m[1]);
						
						while(i<salas.size() && salas.get(i).getIdSala()!=idRecibido ) {
							i++;
						}
						if(i<salas.size() && salas.get(i).getIdSala()==idRecibido) {
							Sala salita=salas.get(i);
							ClienteServidor.sharedInstance().pararHilosSala(salita);
							ClienteServidor.sharedInstance().distribuirASala(salita, "eliminarSala");
							salita.borrarJugadores();
							Servidor.sharedInstance().eliminarSala(salita);
						}
						break;
						
					case "iniciarPartida":
						sala.iniciarPartida();
						ClienteServidor.sharedInstance().setearBombermans(sala);
						ClienteServidor.sharedInstance().distribuirMatrices(sala,"iniciarPartida");					
						ClienteServidor.sharedInstance().iniciarHilosMov(sala);
						ClienteServidor.sharedInstance().refrescarMapa(sala);
						ClienteServidor.sharedInstance().iniciarHiloFinRonda(sala);
						
						break;
						
					case "salirSala":
						sala.eliminarJugador(this.nombre);

						ClienteServidor.sharedInstance().pararHilosCliente(this);
						ClienteServidor.sharedInstance().distribuirASala(sala, "actualizarSala"); 
						break;
				
					case "empiezaMoverseDerecha":
						if(hiloMov!=null) 
							hiloMov.setFuncion("derecha");
							
		
						break;
					case "empiezaMoverseIzquierda":
						if(hiloMov!=null) 
							hiloMov.setFuncion("izquierda");

						break;
					case "empiezaMoverseAbajo":
						if(hiloMov!=null) 
							hiloMov.setFuncion("abajo");
						
						break;
					case "empiezaMoverseArriba":
						if(hiloMov!=null) 
							hiloMov.setFuncion("arriba");
							
						
						break;
					case "dejaMoverseDerecha":
						if(hiloMov!=null) 
							hiloMov.setFuncion("soltarDerecha");
						

						break;
					case "dejaMoverseIzquierda":
						if(hiloMov!=null) 
							hiloMov.setFuncion("soltarIzquierda");

						break;
					case "dejaMoverseArriba":
						if(hiloMov!=null) 
							hiloMov.setFuncion("soltarArriba");
						break;
					case "dejaMoverseAbajo":
						if(hiloMov!=null) 
							hiloMov.setFuncion("soltarAbajo");


						break;
					case "ponerBomba":
						if(hiloMov!=null) 
							hiloMov.setFuncion("bomba");
		
						break;
					case "msj1":
						if(this.bomber!=null)
							ClienteServidor.sharedInstance().distribuirMensajeCliente(this.nombre, sala, "msj1!");
						break;
					case "msj2":
						if(this.bomber!=null)
							ClienteServidor.sharedInstance().distribuirMensajeCliente(this.nombre, sala, "msj2");
						break;
					case "msj3":
						if(this.bomber!=null)
							ClienteServidor.sharedInstance().distribuirMensajeCliente(this.nombre, sala, "msj3");
						break;
					case "msj4":
						if(this.bomber!=null)
							ClienteServidor.sharedInstance().distribuirMensajeCliente(this.nombre, sala, "msj4");
						break;
					}
					
					ClienteServidor.sharedInstance().messageRecived(this.nroCliente, message);
				}
			} 
			catch (IOException e) {
				if(!this.socket.isBound()){
					//no esta mas conectado
					try {
						Servidor.sharedInstance().decrementarClientes();
						ClienteServidor.sharedInstance().sacarCliente(this);
						
						this.socket.close();
						this.interrupt();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
	
	/*
	 * Metodo que me permite comunicarme con el cliente
	 */
	public void writeMessage(String message){
		try{
			DataOutputStream f = new DataOutputStream(this.socket.getOutputStream());
			f.writeUTF(message);
		} catch(Exception e){
			
		}
	}
	
	
	/*
	 * Permite interrumpir el thread que escucha al cliente
	 * y adem‡s cierra el puerto de comunicacion este.
	 */
	public void interrupt(){
		super.interrupt();
		try{
			this.socket.close();
	
		} catch(Exception e){
			
		}
	}

	public Bomberman getBomber() {
		return bomber;
	}

	public void setBomber(Bomberman bomber) {
		this.bomber = bomber;
	}

	public MovimientoServer getHiloMov() {
		return hiloMov;
	}

	public void setHiloMov(MovimientoServer hiloMov) {
		this.hiloMov = hiloMov;
	}

	public int getPuntaje() {
		return puntaje;
	}

	public void setPuntaje(int puntaje) {
		this.puntaje = puntaje;
	}
	
	
	
	
}