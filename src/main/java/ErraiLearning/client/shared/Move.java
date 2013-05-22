package ErraiLearning.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class Move {
	
	private int game;
	
	private int player;
	
	private int row;
	
	private int col;
	
	private boolean validated = false;
	
	public Move() {}

	public Move(int game, int player, int row, int col) {

		this.setGame(game);
		this.setPlayer(player);
		this.setRow(row);
		this.setCol(col);
	}

	public int getGame() {
		return game;
	}

	public void setGame(int game) {
		this.game = game;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

}
