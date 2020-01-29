package logicaServer;

import com.google.gson.Gson;

import logicaServer.Sala;
import logicaJuego.Mapa;

public class RefrescarMapa extends Thread{
	private Sala sala;
	private ClienteSocket cliente;
	private boolean flag;
	
	public RefrescarMapa(Sala sala, ClienteSocket clienteSocket) {
		this.sala=sala;
		cliente= clienteSocket;
		this.flag=true;
		if(cliente.getBomber()==null){
			Gson gson = new Gson();
			Mensaje mensajes = new Mensaje();
			MensajeMapa mensajeMapa=new MensajeMapa();
			mensajes.setTipo("modoEspectador");
			mensajes.setPropietarioMsj(sala.getNombreLider());
			mensajes.setEspectador(true);
			Mapa mapa=sala.getMapa();
			mensajeMapa.cargarAtributos(mapa);
			mensajes.setMapa(mensajeMapa);
			
			String msj = gson.toJson(mensajes, Mensaje.class);
			cliente.writeMessage(msj);
		}
	}
	public void run() {
		while(flag) {
			ClienteServidor.sharedInstance().distribuirMapaACliente(cliente, sala);
			try {
				sleep(45);
			} catch (InterruptedException e) {
				System.out.println("tremendo error 3");
				e.printStackTrace();
			}
		}
	}
	
	public Sala getSala() {
		return sala;
	}
	public void setSala(Sala sala) {
		this.sala = sala;
	}
	
	public void finalizarHilo() {
		flag=false;
	}
	public ClienteSocket getCliente() {
		return cliente;
	}
	public void setCliente(ClienteSocket cliente) {
		this.cliente = cliente;
	}
	
	
}
