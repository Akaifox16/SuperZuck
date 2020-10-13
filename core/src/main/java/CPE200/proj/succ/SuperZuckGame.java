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
import org.mini2Dx.core.graphics.Sprite;

public class SuperZuckGame extends BasicGame {
	public static final String GAME_IDENTIFIER = "CPE200.proj.succ";
    private Control game;
    private int gridSize;
    private int boardOffsetX, boardOffsetY;
	private Texture BackGround;
	private Sound sound;
	private boolean soundcheck = true;
	private Sprite sound_sprite_button;
	private Sprite play_sprite_button;
	private Sprite restartState;

	@Override
    public void initialise() {
        gridSize = 50;
        boardOffsetX = 50;
        boardOffsetY = 50;
        game = new Control();
        sound = Gdx.audio.newSound(Gdx.files.internal("song.mp3"));
        sound.play(0.5f);
        //------------------------------Sprite-----------------------------------------------------
        play_sprite_button = new Sprite(new Texture("play_spare.png"));
        play_sprite_button.setPosition(width/2-125,height/2-(float)37.5+100);
        //------------------------------Sprite_sound------------------------------------------------
        sound_sprite_button = new Sprite(new Texture("sound_up.png"));
        sound_sprite_button.setPosition(width-180,height-115);
        //------------------------------restartstate------------------------------------------------
        restartState = new Sprite(new Texture("restart.png"));
        restartState.setPosition(width-180,height-250);
    }
    
    @Override
    public void update(float delta) {
        switch (game.getCurrentState()) {
            case Stage1: case Stage2:case Stage3:case Stage4:case Stage5:
                switch (game.getCurrentPhase()) {
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
                    case Police_Check:
                        game.gameBoard().checkPolice(game);
                    case Bribe_CD:
                        game.nextPhase();
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
                renderMusic(g);
                break;
            }
            case Stage1: case Stage2: case Stage3: case Stage4: case Stage5:{
                g.setBackgroundColor(Color.GRAY);
                renderBoard(g);
                renderMusic(g);
                renderRestartstate(g);
                break;
            }
            case GameOver:
                renderEndscreen(g);
                break;
        }
    }

    private void renderBoard(Graphics g){
        g.setLineHeight(5);
        g.setColor(Color.BLACK);
        for (int x = 0; x < game.gameBoard().getRow(); x++) {
            for (int y = 0; y < game.gameBoard().getColumn(); y++) {
                int renderX = boardOffsetX + (y * gridSize);
                int renderY = boardOffsetY + (x * gridSize);

                GameObject current = game.gameBoard().board(x,y);
                switch (current.getType()) {
                    case Thumnaz:
                    case Key:
                    case StageDoor:
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


    public void renderMainmenu (Graphics g) {
        g.drawTexture(new Texture("bg.jpg"), 0, 0);
        g.drawTexture(new Texture("Full_Flour_Alchemist.png"), (width / 3), height / 7 - 100);
        g.drawSprite(play_sprite_button);
        if (Gdx.input.isTouched()) {
            if (Gdx.input.getX() > play_sprite_button.getX() && Gdx.input.getX() < play_sprite_button.getX() + play_sprite_button.getWidth()) {
                if (Gdx.input.getY() > play_sprite_button.getY() && Gdx.input.getY() < play_sprite_button.getY() + play_sprite_button.getHeight()) {
                    game.setCurrentState(GameState.Stage1);
                }
            }
        }
    }

    public void renderMusic(Graphics g){
	    g.drawSprite(sound_sprite_button);
        if (Gdx.input.justTouched()) {
            if (Gdx.input.getX() > sound_sprite_button.getX() && Gdx.input.getX() < sound_sprite_button.getX() + sound_sprite_button.getWidth()) {
                if (Gdx.input.getY() > sound_sprite_button.getY() && Gdx.input.getY() < sound_sprite_button.getY() + sound_sprite_button.getHeight()) {
                    if (soundcheck ){
                        soundcheck = false;
                        sound.pause();
                        sound_sprite_button.setTexture(new Texture("mute.png"));
                    }
                    else {
                        sound.play(0.5f);
                        soundcheck = true;
                        sound_sprite_button.setTexture(new Texture("sound_up.png"));
                    }
                    g.drawSprite(sound_sprite_button);
                }
            }
        }

    }

    public void renderEndscreen(Graphics g){
	    g.setBackgroundColor(Color.BLACK);
	    g.drawTexture(new Texture("end bg.jpg"),0,0);
	   //g.drawTexture(new Texture("Full Flour Alchemist (1).png"),width/2-250,height/2-250);
        if (Gdx.input.justTouched()){
            game.setCurrentState(GameState.Stage1);
            game.restartstate();
        }
    }

    public void renderRestartstate (Graphics g){
	    g.drawSprite(restartState);
        if (Gdx.input.justTouched()) {
            if (Gdx.input.getX() > restartState.getX() && Gdx.input.getX() < restartState.getX() + restartState.getWidth()) {
                if (Gdx.input.getY() > restartState.getY() && Gdx.input.getY() < restartState.getY() + restartState.getHeight()) {
                    game.restartState();
                }
            }
        }
    }

}
