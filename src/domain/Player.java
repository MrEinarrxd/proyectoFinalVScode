package domain;

public class Player {
	private int id;
	private int[] coordenates = new int[2];
	private char state = 'H';

	public Player(int id, int[] coordenates, char state) {
		this.id = id;
		this.coordenates = coordenates;
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public char getState() {
		return state;
	}

	public void setState(char state) {
		this.state = state;
	}

	public int[] getCoordinates() {
		return coordenates;
	}

	public void setCoordinates(int[] coordenates) {
		this.coordenates = coordenates;
	}

	public int[] move() {
		int dir = (int) (Math.random() * 4);
		switch (dir) {
			case 0: // derecha
				coordenates[1] += 1;
				break;
			case 1: // izquierda
				coordenates[1] -= 1;
				break;
			case 2: // arriba
				coordenates[0] -= 1;
				break;
			case 3: // abajo
				coordenates[0] += 1;
				break;
		}
		return coordenates;
	}
}