package logicaJuego;


public enum Moverse {
	IZQUIERDA(-1,0),
	DERECHA(1,0),
	ABAJO(0,1),
	ARRIBA(0,-1),
	LMATRIZ(0,-1),
	RMATRIZ(0,1),
	DOWNMATRIZ(1,0),
	UPMATRIZ(-1,0);
	
	
	private int parametroX;
	private int parametroY;
	
	private Moverse(int parametroX, int parametroY) {
		this.parametroX=parametroX;
		this.parametroY=parametroY;
	}

	public int getParametroX() {
		return parametroX;
	}

	public int getParametroY() {
		return parametroY;
	}

}