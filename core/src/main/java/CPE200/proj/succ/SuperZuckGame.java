package CPE200.proj.succ;

import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.GameState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ui.element.Button;
import org.mini2Dx.ui.element.TabButton;

public class SuperZuckGame extends BasicGame {
	public static final String GAME_IDENTIFIER = "CPE200.proj.succ";
    private Control game;
    private int gridSize;
    private int boardOffsetX, boardOffsetY;
	private Texture BackGround;
	private Sound sound;
	@Override
    public void initialise() {
        gridSize = 50;
        boardOffsetX = 50;
        boardOffsetY = 50;
        game = new Control();
        sound = Gdx.audio.newSound(Gdx.files.internal("song.mp3"));
        sound.play(0.5f);
    }
    
    @Override
    public void update(float delta) {
	    switch (game.getCurrentState()) {
            case Stage:
                switch (game.getCurrentTurn()) {
                    case Player_Move:
                        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                            game.moveRight();
                        }
                        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                            game.moveLeft();
                        }
                        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                            game.moveUp();
                        }
                        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                            game.moveDown();
                        }
                        break;
                }
        }
    }
    
    @Override
    public void interpolate(float alpha) {
    }
    
    @Override
    public void render(Graphics g) {
        switch (game.getCurrentState()) {
            case MainMenu:{
                renderMainmenu(g);
                break;
            }
            case Stage: {
                g.setBackgroundColor(Color.GRAY);
                renderBoard(g);
                break;
            }
        }
    }

    private void renderBoard(Graphics g){
        g.setLineHeight(5);
        g.setColor(Color.BLACK);
        for (int x = 0; x < 12; x++) {
            for (int y = 0; y < 18; y++) {
                int renderX = boardOffsetX + (y * gridSize);
                int renderY = boardOffsetY + (x * gridSize);

                GameObject current = game.gameBoard().board(x,y);
                switch (current.getType()) {
                    case Thumnaz:
                    case Key:
                    case Door:
                    case Flour:
                    case Police:
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

    public void renderMainmenu (Graphics g){
        BackGround = new Texture("bg.jpg");
        Texture Playbotton = new Texture("play_button.png");
        Texture name_Game =  new Texture("Full_Flour_Alchemist.png");
        g.drawTexture(BackGround,0,0);
        g.drawTexture(Playbotton,width/2-80,height/2);
        g.drawTexture(name_Game,(width/3),height/7-100);
        if (Gdx.input.isTouched()){
            game.setCurrentState(GameState.Stage);
        }
    }

}
