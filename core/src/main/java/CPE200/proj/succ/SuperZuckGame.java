package CPE200.proj.succ;

import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.GameObjectType;
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
	private static final float volume = 0.12f;
    private Control game;
    private int gridSize;
    private int boardOffsetX, boardOffsetY;
	private Sound music;
	private boolean soundOn = true;
	private Sprite sound_sprite_button;
	private Sprite play_sprite_button;
	private Sprite restartState;
	private long checkdelay = 0;

	@Override
    public void initialise() {
        gridSize = 50;
        boardOffsetX = 50;
        boardOffsetY = 50;
        game = new Control();
        music = Gdx.audio.newSound(Gdx.files.internal("sound/song.mp3"));
        music.play(volume);
        checkdelay = System.nanoTime()/1000000000;
        //------------------------------Sprite-----------------------------------------------------
        play_sprite_button = new Sprite(new Texture("re.png"));
        play_sprite_button.setPosition(width/2-125,height/2-(float)37.5+100);
        //------------------------------Sprite_sound------------------------------------------------
        sound_sprite_button = new Sprite(new Texture("sound_up.png"));
        sound_sprite_button.setPosition(width-180,height-115);
        //------------------------------restartstate------------------------------------------------
        restartState = new Sprite(new Texture("restart.png"));
        restartState.setPosition(width-180,height-250);
    }
    
    private boolean isSpriteTouch(Sprite button){
        if (Gdx.input.justTouched()) {
            if (Gdx.input.getX() > button.getX() && Gdx.input.getX() < button.getX() + button.getWidth()) {
                return Gdx.input.getY() > button.getY() && Gdx.input.getY() < button.getY() + button.getHeight(); 
            }
        }
        return false;
    }
    
    @Override
    public void update(float delta) {
        if (isSpriteTouch(sound_sprite_button)) {
            if (soundOn){
                soundOn = false;
                music.setVolume(0,0);
                sound_sprite_button.setTexture(new Texture("mute.png"));
            }
            else {
                music.setVolume(0,volume);
                soundOn = true;
                sound_sprite_button.setTexture(new Texture("sound_up.png"));
            }
            //  g.drawSprite(sound_sprite_button);
        }
        switch (game.getCurrentState()) {
            case MainMenu:
                        if (isSpriteTouch(play_sprite_button)) {
                            game.setCurrentState(GameState.Stage1);
                            playMusic();
                            game.restartState();
                        }break;
            case Stage1: case Stage2:case Stage3:case Stage4:case Stage5:
                switch (game.getCurrentPhase()) {
                    case Player_Move:
                        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                            game.moveRight();
                        }
                        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT))  {
                            game.moveLeft();
                        }
                        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                            game.moveUp();
                        }
                        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                            game.moveDown() ;
                        }
                        checkdelay = System.nanoTime()/1000000000;
                        break;
                    case Police_Check:
                        game.gameBoard().checkPolice(game);
                    case Object_Cool_down:
                        game.gameBoard().checkBomb(game);
                        game.nextPhase();
                }
                if (isSpriteTouch(restartState)) {
                    playMusic();
                    game.restartState();
                }
                break;
            case GameOver:
            case GameClear:
                if (Gdx.input.justTouched() && System.nanoTime()/1000000000 - checkdelay > 5){
                    game.setCurrentState(GameState.MainMenu);
                    if(soundOn)
                        music.play(volume);
                }
                break;
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
            case GameOver: {
                renderEndscreen(g);
                break;
            }
            case GameClear:{
                renderClear(g);
                break;
            }
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
                if(current.getType() != GameObjectType.NULL) g.drawTexture(current.getTexture(),renderX,renderY);
            }
        }
    }


    private void renderMainmenu (Graphics g) {
        g.drawTexture(new Texture("bg.jpg"), 0, 0);
        g.drawTexture(new Texture("Full_Flour_Alchemist.png"), (width / 3), height / 7 - 100);
        g.drawSprite(play_sprite_button);
    }

    private void renderMusic(Graphics g){
	    g.drawSprite(sound_sprite_button);
    }

    private void renderEndscreen(Graphics g){
	    g.setBackgroundColor(Color.GRAY);
	    renderBoard(g);
	    music.pause();
	    //music.dispose();
	    //g.drawTexture(new Texture("Full Flour Alchemist (1).png"),width/2-250,height/2-250);
        if (System.nanoTime()/1000000000 - checkdelay > 5){
            g.drawTexture(new Texture("end bg.jpg"),0,0);
        }
    }

    private void renderRestartstate (Graphics g){
	    g.drawSprite(restartState);
    }

    private void renderClear (Graphics g){
	    g.drawTexture(new Texture("complete_bg.jpg"),0,0);
	    g.drawTexture(new Texture("complete_spare_new.png"),width/2-300,height/6);
    }

    public void playMusic(){
        music.dispose();
	   switch (game.getCurrentState()){
           case MainMenu:
               music = Gdx.audio.newSound(Gdx.files.internal("sound/song.mp3"));
               break;
           case Stage1:
               music = Gdx.audio.newSound(Gdx.files.internal("sound/1.mp3"));
               break;
           case Stage2:
               music = Gdx.audio.newSound(Gdx.files.internal("sound/2.mp3"));break;
           case Stage3:
               music = Gdx.audio.newSound(Gdx.files.internal("sound/3.mp3"));break;
           case Stage4:
               music = Gdx.audio.newSound(Gdx.files.internal("sound/4.mp3"));break;
           case Stage5:
               music = Gdx.audio.newSound(Gdx.files.internal("sound/5.mp3"));break;
       }
        music.play(soundOn ? volume:0);
    }
}
