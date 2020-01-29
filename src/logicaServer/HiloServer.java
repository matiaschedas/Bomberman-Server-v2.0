package logicaServer;

import java.net.ServerSocket;
import java.net.Socket;

public class HiloServer extends Thread{
	
	public void run(){
		int nroCliente = 0;
		
		
		while(true){
			try{
				ServerSocket servidor = Servidor.sharedInstance().getServerSocket();
				Socket socket = servidor.accept();
				
				System.out.println("Se conecto el cliente " + nroCliente);
				ClienteSocket cliente = new ClienteSocket(socket,nroCliente);
				nroCliente++;
				cliente.start();
				ClienteServidor.sharedInstance().agregarCliente(cliente);
				Servidor.sharedInstance().incrementarClientes();
				Thread.sleep(20);
			} 
			catch(Exception e){
			}
		}
	}

}
