package hilosDelJuego;

import java.io.Serializable;

import logicaJuego.Bomberman;

public class HiloBuffos extends Thread implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private int buffTiempoMaxBomba;
	

	private double buffTiempoMaxRango;
	
	
	private double buffTiempoMaxVelocidad;
	
	private int cont;
	private Bomberman bomber;
	
	public HiloBuffos(Bomberman bomber) {
		this.bomber = bomber;
		
		cont=0;
		buffTiempoMaxBomba=-1;
		buffTiempoMaxVelocidad=-1;
		buffTiempoMaxRango=-1;
	}
	public void run() {
		
		while(true) {
			cont++;
			if(cont>10000)
				cont=0;
	
			if(bomber.isBuffoBomba())
				this.setBuffActualBomba();
			if(bomber.isBuffoRango())
				this.setBuffActualRango();
			if(bomber.isBuffoVelocidad())
				this.setBuffActualVelocidad();
			
			if(cont==buffTiempoMaxBomba) {
				bomber.setCantBombas(1);
				bomber.setCantBombasMax(1);
				buffTiempoMaxBomba=-1;
			}
			if(cont==buffTiempoMaxRango) {
				bomber.setRango(1);
				buffTiempoMaxRango=-1;
			}
			if(cont==buffTiempoMaxVelocidad) {
				bomber.setVelocidad(2);
				buffTiempoMaxVelocidad=-1;
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.out.println("tremendo error");
				e.printStackTrace();
			}
		}
		
	}

	public void setBuffActualBomba() {
		bomber.setBuffoBomba(false);
		if((cont+1000)>10000)
			this.buffTiempoMaxBomba = 1000;
		else
			this.buffTiempoMaxBomba = cont+1000;
	}
	public void setBuffActualRango() {
		bomber.setBuffoRango(false);
		if((cont+1000)>10000)
			this.buffTiempoMaxRango = 1000;
		else
			this.buffTiempoMaxRango = cont+1000;
	}
	public void setBuffActualVelocidad() {
		bomber.setBuffoVelocidad(false);
		if((cont+1000)>10000)
			this.buffTiempoMaxVelocidad = 1000;
		else
			this.buffTiempoMaxVelocidad = cont+1000;
	}

	
}
