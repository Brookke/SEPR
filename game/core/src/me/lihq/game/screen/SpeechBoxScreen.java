package me.lihq.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import me.lihq.game.GameMain;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


/**
 * Created by Ben on 11/12/2016.
 */
public class SpeechBoxScreen extends AbstractScreen
{

    private ShapeRenderer renderer = new ShapeRenderer();
    public Stage stage;
    private Group group;
    private Skin buttonSkin;
    private TextField.TextFieldStyle fontStyle;
    private String personTalking = "TESTPERSON";//the person talking
    private String voiceTalking = "TESTVOICE";//what the person says
    private ShapeRenderer rectRenderer;

    private int padding = 5;//the padding to put around the textbox
    private int textBoxHeight = 100;//the height of the text box

    public SpeechBoxScreen(GameMain game)
    {
        super(game);
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();


        stage = new Stage();
        initSkins();
        group = new Group();
        group.setOrigin(-w/2+padding,-h/2+padding);
        group.setWidth(w-2*padding);
        group.setHeight(textBoxHeight);
        int textBoxWidth = Math.round(w-2*padding);
        ShapeRenderer recRenderer = new ShapeRenderer();
        recRenderer.begin(ShapeRenderer.ShapeType.Filled);
        recRenderer.setColor(Color.WHITE);
        recRenderer.rect(padding,padding,textBoxWidth,textBoxHeight);
        recRenderer.end();

        boolean playerQuestion = false; //decide how to implement this properly
        if (playerQuestion == true)
        {
            TextButton questionButton = new TextButton("Question", buttonSkin);
            group.addActor(questionButton);
            TextButton accuseButton = new TextButton("Accuse", buttonSkin);
            group.addActor(accuseButton);
            //TextButton ignoreButton = new TextButton("Ignore",buttonSkin); //for the next group to just un-comment - you get the gist from above
            questionButton.setPosition(0, textBoxHeight / 2);
            accuseButton.setPosition(0, 0); //use "textBoxWidth/2" to get the button on the right half
            stage.addActor(group);
        }
        else
        {
            TextField.TextFieldStyle fontStyle = new TextField.TextFieldStyle();
            BitmapFont font = new BitmapFont();
            fontStyle.font = font;
            fontStyle.fontColor = Color.BLUE;
            TextField person = new TextField(personTalking, fontStyle);
            group.addActor(person);
            TextArea voice = new TextArea(voiceTalking,fontStyle); //what the person says
            group.addActor(voice);
            person.setPosition(0,textBoxHeight-10);
            voice.setPosition(person.getWidth(),0);
            stage.addActor(group);
        }
    }

    private void initSkins()
    {
        initButtonSkin();
    }

    private void initButtonSkin()
    {
        //Create a font
        BitmapFont font = new BitmapFont();
        font.setColor(Color.BLUE);
        buttonSkin = new Skin();
        buttonSkin.add("default", font);

        //Create a texture
        Pixmap pixmap = new Pixmap((int) Gdx.graphics.getWidth()/2-padding*2, textBoxHeight/2, Pixmap.Format.RGB888); //may need to edit
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        buttonSkin.add("background",new Texture(pixmap));

        //Create a button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = buttonSkin.newDrawable("background", Color.GREEN);
        textButtonStyle.down = buttonSkin.newDrawable("background", Color.GRAY);
        textButtonStyle.checked = buttonSkin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.over = buttonSkin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.font = buttonSkin.getFont("default");
        textButtonStyle.font.setColor(Color.BLUE);//why doesn't this work?
        buttonSkin.add("default", textButtonStyle);

    }

    public void render(float delta)
    {
        stage.act();
        stage.draw();
    }

    public void setPersonVoice(String person,String voice) //use to set who is talking and what they are saying
    {
        personTalking = person+" :";
        voiceTalking = voice; //might need to check length for so it doesnt go out of the text box
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void show()
    {

    }

    @Override
    public void hide()
    {
        stage.dispose();
    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }

    @Override
    public void update()
    {

    }
}
