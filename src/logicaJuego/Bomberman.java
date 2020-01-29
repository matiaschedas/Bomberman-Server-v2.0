package logicaJuego;


import java.io.Serializable;

import javax.swing.ImageIcon;

import logicaJuego.Entidad;
import logicaServer.ClienteServidor;

public class Bomberman extends Entidad implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ImageIcon[] up, down, left, right, muerte;
	private ImageIcon bomberman;
	
	private boolean vivo;
	private double velocidad;
	private int puntos;
	private int cantMuertes;
	private int cantBombasMax;
	private int cantBombas;
	private int posX;
	private int posY;
	private float posGrafX;
	private float posGrafY;
	private int contMovimientos;
	private String whichDir;
	private double velX;
	private double velY;
	private int tiempoMuerte;
	private int rango;
	private boolean buffoBomba;
	private boolean buffoVelocidad;
	private boolean buffoRango;
	private String nombre;
	private int puntaje;

	public double getVelX() {
		return velX;
	}

	public void setVelX(double velX) {
		this.velX = velX;
	}

	public double getVelY() {
		return velY;
	}

	public void setVelY(double velY) {
		this.velY = velY;
	}

	public Bomberman(int x, int y, String nombre) {
		super(x, y);
		this.vivo = true;
		this.velocidad = 2;
		this.puntos = 0;
		this.cantMuertes = 0;
		this.cantBombas = 1;
		this.cantBombasMax= 1;
		this.posX = x;
		this.posY = y;
		this.posGrafX = 40 * x;
		this.posGrafY = 40 * y;
		this.contMovimientos = 0;
		this.whichDir = "nn";
		this.tiempoMuerte=0;
		this.rango=1;
		buffoBomba=false;
		buffoVelocidad=false;
		buffoRango=false;
		this.nombre=nombre;
		this.puntaje=0;
		
		
		up = new ImageIcon[] { new ImageIcon("./src/imagenes/1-up.png"), new ImageIcon("./src/imagenes/2-up.png"),
				new ImageIcon("./src/imagenes/3-up.png") };
		down = new ImageIcon[] { new ImageIcon("./src/imagenes/1-down.png"), new ImageIcon("./src/imagenes/2-down.png"),
				new ImageIcon("./src/imagenes/3-down.png") };
		left = new ImageIcon[] { new ImageIcon("./src/imagenes/1-left.png"), new ImageIcon("./src/imagenes/2-left.png"),
				new ImageIcon("./src/imagenes/3-left.png") };
		right = new ImageIcon[] { new ImageIcon("./src/imagenes/1-right.png"),
				new ImageIcon("./src/imagenes/2-right.png"), new ImageIcon("./src/imagenes/3-right.png") };
		
		muerte = new ImageIcon[] { new ImageIcon("./src/imagenes/muere/muere1.png"), new ImageIcon("./src/imagenes/muere/muere2.png"),
				new ImageIcon("./src/imagenes/muere/muere3.png"),new ImageIcon("./src/imagenes/muere/muere4.png"),
				new ImageIcon("./src/imagenes/muere/muere5.png"),new ImageIcon("./src/imagenes/muere/muere6.png")};
		
		bomberman = new ImageIcon("./src/imagenes/1-down.png");
	}

	public void actualizacionGrafica(Mapa test) {
		tiempoMuerte++;
		contMovimientos++;       
        
		Entidad proximo;
		int desplazamientoPermitido = 10;
		int desplazamientoEntreBloques = 5;
		if (vivo == true) {
			if (velX > 0) {
				proximo = test.getPosicionMapa(posX + 1, posY);
				if (proximo.esBloque() && (posGrafY % 40 == 0 || (posGrafY>=(posY*40)-desplazamientoPermitido && posGrafY<=(posY*40+1)+desplazamientoPermitido))) {
					if (((Bloque) proximo).queTipo().equals("transitable") || ((Bloque) proximo).queTipo().equals("fuego") || 
							((Bloque) proximo).queTipo().equals("fuegoPiedra") || ((Bloque) proximo).queTipo().equals("velocidad") ||
							((Bloque) proximo).queTipo().equals("masBombas") || ((Bloque) proximo).queTipo().equals("rango") ) {
						if (posGrafX >= (((posX * 40) + ((posX + 1) * 40)) / 2))
							this.moverse(Moverse.DERECHA, test);
						posGrafX += velX;
						if (contMovimientos % (int)velocidad != 0)
							if(contMovimientos%5 == 0 || contMovimientos%2 == 0) {
								this.setBomberman(this.move("right"));

							}
					} else if (posGrafX <= (posX * 40+desplazamientoEntreBloques)) {
						posGrafX += velX;
						if (contMovimientos % (int)velocidad != 0)
							if(contMovimientos%5 == 0 || contMovimientos%2 == 0) {
								this.setBomberman(this.move("right"));

							}
					}
				} else if (posGrafX <= (posX * 40+desplazamientoEntreBloques)) {
					posGrafX += velX;
					if (contMovimientos % (int)velocidad != 0)
						if(contMovimientos%5 == 0 || contMovimientos%2 == 0) {
							this.setBomberman(this.move("right"));

						}
				}
			}
			if (velX < 0) {
				proximo = test.getPosicionMapa(posX - 1, posY);
				if (proximo.esBloque() && (posGrafY % 40 == 0 || (posGrafY>=(posY*40)-desplazamientoPermitido && posGrafY<=(posY*40+1)+desplazamientoPermitido))) {
					if (((Bloque) proximo).queTipo().equals("transitable") || ((Bloque) proximo).queTipo().equals("fuego") || 
							((Bloque) proximo).queTipo().equals("fuegoPiedra") || ((Bloque) proximo).queTipo().equals("velocidad") ||
							((Bloque) proximo).queTipo().equals("masBombas") || ((Bloque) proximo).queTipo().equals("rango")) {
						if (posGrafX <= (((posX * 40) + ((posX - 1) * 40)) / 2))
							this.moverse(Moverse.IZQUIERDA, test);
						posGrafX += velX;
						if (contMovimientos % (int)velocidad != 0)
							if(contMovimientos%5 == 0 || contMovimientos%2 == 0) {
								this.setBomberman(this.move("left"));

							
							}
					} else if (posGrafX>= ((posX) * 40)-desplazamientoEntreBloques) {
						posGrafX += velX;
						if (contMovimientos % (int)velocidad != 0)
							if(contMovimientos%5 == 0 || contMovimientos%2 == 0) {
								this.setBomberman(this.move("left"));

							}
					}
				} else if (posGrafX >= (posX * 40)-desplazamientoEntreBloques) {
					posGrafX += velX;
					if (contMovimientos % (int)velocidad != 0)
						if(contMovimientos%5 == 0 || contMovimientos%2 == 0) {
							this.setBomberman(this.move("left"));

						}
				}
			}
			if (velY < 0) {
				proximo = test.getPosicionMapa(posX, posY - 1);
				if (proximo.esBloque() && (posGrafX % 40 == 0 || ((posGrafX>=(posX*40)-desplazamientoPermitido) && posGrafX<=(posX*40)+desplazamientoPermitido ))) {
					if (((Bloque) proximo).queTipo().equals("transitable") || ((Bloque) proximo).queTipo().equals("fuego") || 
							((Bloque) proximo).queTipo().equals("fuegoPiedra") || ((Bloque) proximo).queTipo().equals("velocidad") ||
							((Bloque) proximo).queTipo().equals("masBombas") || ((Bloque) proximo).queTipo().equals("rango")) {
						if (posGrafY <= (((posY * 40) + ((posY - 1) * 40)) / 2))
							this.moverse(Moverse.ARRIBA, test);
						posGrafY += velY;
						if (contMovimientos % (int)velocidad != 0)
							if(contMovimientos%5 == 0 || contMovimientos%2 == 0) {
								this.setBomberman(this.move("up"));

							}

					} else if (posGrafY >= (posY * 40)-desplazamientoEntreBloques) {
						posGrafY += velY;
						if (contMovimientos % (int)velocidad != 0)
							if(contMovimientos%5 == 0 || contMovimientos%2 == 0) {
								this.setBomberman(this.move("up"));

							}

					}
				} else if (posGrafY >= (posY * 40)-desplazamientoEntreBloques) {
					posGrafY += velY;
					if (contMovimientos % (int)velocidad != 0)
						if(contMovimientos%5 == 0 || contMovimientos%2 == 0) {
							this.setBomberman(this.move("up"));
						}
				}
			}
			if (velY > 0) {
				proximo = test.getPosicionMapa(posX, posY + 1);
				if (proximo.esBloque() && (posGrafX % 40 == 0 || ((posGrafX>=(posX*40)-desplazamientoPermitido) && posGrafX<=(posX*40)+desplazamientoPermitido ))) {
					if (((Bloque) proximo).queTipo().equals("transitable") || ((Bloque) proximo).queTipo().equals("fuego") || 
							((Bloque) proximo).queTipo().equals("fuegoPiedra") || ((Bloque) proximo).queTipo().equals("velocidad") ||
							((Bloque) proximo).queTipo().equals("masBombas") || ((Bloque) proximo).queTipo().equals("rango")) {
						if (posGrafY >= (((posY * 40) + ((posY + 1) * 40)) / 2))
							this.moverse(Moverse.ABAJO, test);
						posGrafY += velY;
						if (contMovimientos % (int)velocidad != 0)
							if(contMovimientos%5 == 0 || contMovimientos%2 == 0) {
								this.setBomberman(this.move("down"));
							
							}
										
					} else if (posGrafY  <= ((posY) * 40)+desplazamientoEntreBloques) {
						posGrafY += velY;
						if (contMovimientos % (int)velocidad != 0)
							if(contMovimientos%5 == 0 || contMovimientos%2 == 0) {
								this.setBomberman(this.move("down"));	
							}
					}
				} else if (posGrafY <= (posY * 40)+desplazamientoEntreBloques)  {
					posGrafY += velY;
					if (contMovimientos % (int)velocidad != 0)
						if(contMovimientos%5 == 0 || contMovimientos%2 == 0) {
							this.setBomberman(this.move("down"));

						}
				}
			}
		}
		else {
			if(tiempoMuerte%25 == 0)
				bomberman = muerte[tiempoMuerte%6];
		}
			
	}

	public ImageIcon move(String dir) {
		if (dir.equals("up")) {
			whichDir = "u";
			return up[contMovimientos % 3];
		} else if (dir.equals("down")) {
			whichDir = "d";
			return down[contMovimientos % 3];
		} else if (dir.equals("right")) {
			whichDir = "r";
			return right[contMovimientos % 3];
		} else if (dir.equals("left")) {
			whichDir = "l";
			return left[contMovimientos % 3];
		} else {
			if (whichDir.equals("r"))
				return right[0];
			else if (whichDir.equals("l"))
				return left[2];
			else if (whichDir.equals("u"))
				return up[1];
			return down[1];
		}
	}

	public void moverse(Moverse mov, Mapa mapa) {
		int dY = mov.getParametroY();
		int dX = mov.getParametroX();
		int posIni[] = { this.posX, this.posY };
		Bloque transitable = new Bloque("transitable", posX, posY);
		boolean codigoHorrible = false;
		if (vivo == true) {
			if (((Entidad) mapa.getPosicionMapa(this.posX, this.posY)).esBloque()) {
				
				if (((Bloque) mapa.getPosicionMapa(this.posX, this.posY)).queTipo().equals("masBombas")) {
					mapa.setMatrizMapa(transitable, this.posX, this.posY);
					if (cantBombasMax < 4) {
						cantBombasMax++;
						cantBombas++;
						buffoBomba=true;
					}
				}
				if (((Bloque) mapa.getPosicionMapa(this.posX, this.posY)).queTipo().equals("velocidad")) {
					mapa.setMatrizMapa(transitable, this.posX, this.posY);
					if (velocidad < 4) {
						velocidad+=0.5;
						buffoVelocidad=true;
					}
				}
				if (((Bloque) mapa.getPosicionMapa(this.posX, this.posY)).queTipo().equals("rango")) {
					mapa.setMatrizMapa(transitable, this.posX, this.posY);
					if (rango < 6) {
						rango++;
						buffoRango=true;
					}
				}
			}
			if (mapa.esTransitable(this.posX + dX, this.posY + dY)
					|| ((Entidad) mapa.getPosicionMapa(this.posX + dX, this.posY + dY)).esBloque()) {
				if (mapa.esTransitable(this.posX + dX, this.posY + dY)
						|| ((Bloque) mapa.getPosicionMapa(this.posX + dX, this.posY + dY)).queTipo().equals("fuego") || ((Bloque) mapa.getPosicionMapa(this.posX + dX, this.posY + dY)).queTipo().equals("fuegoPiedra") ) {
					if (((Bloque) mapa.getPosicionMapa(this.posX + dX, this.posY + dY)).queTipo().equals("fuego") || ((Bloque) mapa.getPosicionMapa(this.posX + dX, this.posY + dY)).queTipo().equals("fuegoPiedra")) {
						this.muere(mapa);
					} else if(mapa.esTransitable(this.posX + dX, this.posY + dY)){
						this.posY += dY;
						this.posX += dX;
						codigoHorrible=true;
					}
				}
			}
			if (((Entidad) mapa.getPosicionMapa(this.posX + dX, this.posY + dY)).esBloque() && codigoHorrible==false) {
				if (((Bloque) mapa.getPosicionMapa(this.posX + dX, this.posY + dY)).queTipo().equals("masBombas")
						|| ((Bloque) mapa.getPosicionMapa(this.posX + dX, this.posY + dY)).queTipo().equals("velocidad")
						|| ((Bloque) mapa.getPosicionMapa(this.posX + dX, this.posY + dY)).queTipo().equals("rango")) {
					this.posX += dX;
					this.posY += dY;
					
				}
			}


			int posFin[] = { this.posX, this.posY };
			if (posIni != posFin && mapa.getPosicionMatrizCosas(posIni[0], posIni[1]).esBomba()) {
				Bomba bomba = (Bomba) mapa.getPosicionMatrizCosas(posIni[0], posIni[1]);
				mapa.setMatrizMapa(bomba, posIni[0], posIni[1]);
			}
		}
	}



	public void muere(Mapa mapa) {
		if (vivo) {
			this.cantMuertes += 1;
			vivo = false;
		}
	
	}

	public void revivir(Mapa mapa) {
		mapa.setMatrizMapa(new Bloque("transitable", this.posX, this.posY), this.posX, this.posY);
		
		this.vivo = true;
		this.posX = this.getX();
		this.posY = this.getY();
		this.posGrafX = this.getX() * 40;
		this.posGrafY = this.getY() * 40;
		
		bomberman = new ImageIcon("./src/imagenes/1-down.png");
	}

	public Bomba ponerBomba(Mapa mapa) {
		if(!mapa.getPosicionMatrizCosas(this.posX, this.posY).esBomba()) {
			Bomba nueva = new Bomba(this.posX, this.posY,rango);// aca la ubico en el mapa (en el mismo lugar que el bomberman)
			nueva.setId(ClienteServidor.sharedInstance().obtenerIDBomba());
			mapa.setMatrizCosas(nueva, this.posX, this.posY);// la muestro en la interfaz
			mapa.setMatrizMapa(nueva, this.posX, this.posY);
			if(cantBombas>0)
				cantBombas--;
			return nueva;
		}
		return null;
	}

	public float getPosGrafX() {
		return posGrafX;
	}

	public void setPosGrafX(float posGrafX) {
		this.posGrafX = posGrafX;
	}

	public float getPosGrafY() {
		return posGrafY;
	}

	public void setPosGrafY(float posGrafY) {
		this.posGrafY = posGrafY;
	}

	public boolean isVivo() {
		return vivo;
	}

	@Override
	public String toString() {
		return "Bomberman";
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public void setVivo(boolean vivo) {
		this.vivo = vivo;
	}

	public double getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(double velocidad) {
		this.velocidad = velocidad;
	}

	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	public int getCantMuertes() {
		return cantMuertes;
	}

	public void setCantMuertes(int cantMuertes) {
		this.cantMuertes = cantMuertes;
	}

	public int getCantBombas() {
		return cantBombas;
	}
	public void setCantBombas(int cantBombas) {
		this.cantBombas = cantBombas;
	}

	public void incrementarUnaBomba() {
		this.cantBombas +=1;
	}
	public void decrementarUnaBomba() {
		this.cantBombas -=1;
	}

	public int getRango() {
		return rango;
	}

	public void setRango(int rango) {
		this.rango = rango;
	}

	@Override
	public boolean esBloque() {
		return false;
	}

	@Override
	public boolean esBomberman() {
		return true;
	}

	@Override
	public boolean esBomba() {
		return false;
	}

	public int getContMovimientos() {
		return contMovimientos;
	}

	public void setContMovimientos(int contMovimientos) {
		this.contMovimientos = contMovimientos;
	}

	public ImageIcon getBomberman() {
		return bomberman;
	}

	public void setBomberman(ImageIcon bomberman) {
		this.bomberman = bomberman;
	}

	public boolean isBuffoBomba() {
		return buffoBomba;
	}

	public void setBuffoBomba(boolean buffoBomba) {
		this.buffoBomba = buffoBomba;
	}

	public boolean isBuffoVelocidad() {
		return buffoVelocidad;
	}

	public void setBuffoVelocidad(boolean buffoVelocidad) {
		this.buffoVelocidad = buffoVelocidad;
	}

	public boolean isBuffoRango() {
		return buffoRango;
	}

	public void setBuffoRango(boolean buffoRango) {
		this.buffoRango = buffoRango;
	}

	public String getNombre() {
		return nombre;
	}

	public int getCantBombasMax() {
		return cantBombasMax;
	}

	public void setCantBombasMax(int cantBombasMax) {
		this.cantBombasMax = cantBombasMax;
	}

	public int getPuntaje() {
		return puntaje;
	}

	public void setPuntajes(int puntos) {
		this.puntaje=puntos;
	}
	public void setPuntaje() {
		this.puntaje +=1;
	}

	
	
}
