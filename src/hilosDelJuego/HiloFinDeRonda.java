package hilosDelJuego;

import logicaServer.ClienteServidor;
import logicaServer.Sala;
import logicaJuego.Mapa;

public class HiloFinDeRonda extends Thread {

	private Mapa mapa;
	private Sala sala;
	private boolean flag;
	
	
	public HiloFinDeRonda(Mapa mapa, Sala sala) {
		this.sala = sala;
		this.mapa = mapa;
		flag = true;
		
	}

	public void run() {
		int minutos=1, segundos=00;
		String temporizador = new String();
		
		while (flag && sala!=null) {
			if (mapa.revisarSiTerminaPartida() == true) {
				sala.finalizarRonda();
				minutos=1;
				segundos=0;
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			segundos--;
			if(segundos==-1) {
				minutos--;
				segundos=59;
			}
			
			if(minutos==0 && segundos==0) {
				sala.finalizarRondaTiempo();
				minutos=1;
				segundos=0;
			}
			temporizador=temporizador.concat(((Integer)minutos).toString());
			temporizador=temporizador.concat(":");
			if(segundos<10) {
				temporizador=temporizador.concat("0"+((Integer)segundos).toString());
			}else
				temporizador=temporizador.concat(((Integer)segundos).toString());
			ClienteServidor.sharedInstance().distribiurASala(sala, temporizador, "temporizador");
			temporizador = new String();
			
		}
	}

	public void finalizarHilo() {
		flag = false;
	}

	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	public void reanudarHilo(Mapa mapa) {
		this.mapa = mapa;
		this.flag = true;
	}


}
