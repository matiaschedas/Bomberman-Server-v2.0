package logicaServer;

import logicaServer.Sala;
import hilosDelJuego.Bombita;
import logicaJuego.Bomberman;

public class MovimientoServer extends Thread{
	
	private Bomberman bomber; 
	private String funcion;
	private boolean derecha;
	private boolean izquierda;
	private boolean abajo;
	private boolean arriba;
	private boolean bombaflag;
	private Sala sala;	
	private boolean flag;


	public MovimientoServer(Bomberman bomber, Sala sala) {
		this.sala=sala;
		this.bomber=bomber;
		funcion=new String();
		derecha=false;
		arriba=false;
		derecha=false;
		izquierda=false;
		bombaflag=false;
		flag=true;
	}

	public String getFuncion() {
		return funcion;
	}

	public void setFuncion(String direccion) {
		this.funcion = direccion;
		switch(direccion) {
		case "arriba":
			arriba=true;
			break;
		case "abajo":
			abajo=true;
			break;
		case "izquierda":
			izquierda=true;
			break;
		case "derecha":
			derecha=true;
			break;
		
		case "bomba":
			bombaflag=true;
			break;
			
		case "soltarAbajo":
			abajo=false;
			break;
		case "soltarIzquierda":
			izquierda=false;
			break;
		case "soltarArriba":
			arriba=false;
			break;
		case "soltarDerecha":
			derecha=false;
			break;
		}
		
	}
	
	public void run() {
		while(flag=true) {
		
			if(derecha) {
				bomber.setVelX(bomber.getVelocidad());	
			}
			else {
				bomber.setVelX(0);
			}
			if(izquierda) {
				bomber.setVelX(-bomber.getVelocidad());
			}
			else if(!derecha){
				bomber.setVelX(0);
			}
			if(arriba) {
				bomber.setVelY(-bomber.getVelocidad());
			}
			else {
				bomber.setVelY(0);
			}
			
			if(abajo) {
				bomber.setVelY(bomber.getVelocidad());
			}
			else if(!arriba){
				bomber.setVelY(0);
			}
			if(bombaflag) {
				Bombita hiloBomba = new Bombita(bomber,sala.getMapa());
				hiloBomba.start();
				bombaflag=false;
			}
			
			bomber.actualizacionGrafica(sala.getMapa());	

			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.out.println("tremendo error 2");
				e.printStackTrace();
			}
		}		
	}
	
	public void finalizarHilo() {
		this.flag=false;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	
}
