package logicaServer;



import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import logicaServer.Sala;


public class Servidor {

	private int puerto;


	private int cantClientes;
	private ServerSocket serverSocket;
	private static Servidor server;
	private int idSalas;
	private ArrayList<Sala> salas;
	

	public Servidor(int puerto) {
		this.idSalas = 0;
		this.cantClientes = 0;
		this.puerto = puerto;
		this.salas = new ArrayList<>();
		System.out.println("SERVER INICIADO - Esperando conexiones de clientes ...");
		server = this;
		this.iniciarServidor();
	}
	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	public static Servidor sharedInstance(){
		return server;
	}
	

	public Sala agregarSala(String nombre, String nombreLider) {
		this.idSalas+=1;
		Sala sala=new Sala(nombre,idSalas,nombreLider,"");
		salas.add(sala);
		return sala;
	}
	public Sala agregarSala(String nombre, String pass, String nombreLider) {
		this.idSalas+=1;
		Sala sala=new Sala(nombre,idSalas,nombreLider,pass);
		salas.add(sala);
		return sala;
	}
	public void eliminarSala(Sala sala) {
		salas.remove(sala);
	}
	
	public ArrayList<Sala> devolverSalas() {
		return salas;
	}

	public void iniciarServidor() {
		try {
			serverSocket = new ServerSocket(this.puerto);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HiloServer hilo = new HiloServer(); 
		hilo.start();
	}
	
	public void incrementarClientes(){
		this.cantClientes++;
	}
	
	public void decrementarClientes(){
		if(this.cantClientes > 0)
			this.cantClientes--;
	}
	
	public int getCantClientes() {
		return cantClientes;
	}
	
	public void setCantClientes(int cantClientes) {
		this.cantClientes = cantClientes;
	}
	
	public int getPuerto() {
		return puerto;
	}
	
	public static void main(String[] args) {
		new Servidor(10000);
	}
	
	
}
