package CPE200.proj.succ;

import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.TurnState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;

public class SuperZuckGame extends BasicGame {
	public static final String GAME_IDENTIFIER = "CPE200.proj.succ";
    private Control game;
    private int gridSize;
    private int boardOffsetX, boardOffsetY;
	private Texture BackGround;
	@Override
    public void initialise() {
        gridSize = 50;
        boardOffsetX = 50;
        boardOffsetY = 50;
        game = new Control();
    }
    
    @Override
    public void update(float delta) {
	    if(game.getCurrentState() == GameState.Stage) {
            if(game.getCurrentTurn() == TurnState.Player_Move) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                    game.moveRight();
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                    game.moveLeft();
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                    game.moveUp();
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                    game.moveDown();
                }
            }
        }
    }
    
    @Override
    public void interpolate(float alpha) {
    }
    
    @Override
    public void render(Graphics g) {
        g.setBackgroundColor(Color.NAVY);
        if(game.getCurrentState() == GameState.Stage){
           renderBoard(g);
        }
    }

    private void renderBoard(Graphics g){
        g.setLineHeight(5);
        g.setColor(Color.BLACK);
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 20; y++) {
                int renderX = boardOffsetX + (y * gridSize);
                int renderY = boardOffsetY + (x * gridSize);

                GameObject current = game.gameBoard().board(x,y);
                switch (current.getType()) {
                    case Thumnaz:
                    case Wall:
                    case Bribe:
                        g.drawTexture(current.getTexture(),renderX,renderY);
                        break;
                    case NULL:
                    default:
                        break;
                }
            }
        }
    }
}
