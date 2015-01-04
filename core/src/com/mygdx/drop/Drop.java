package com.mygdx.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.awt.Rectangle;

public class Drop extends ApplicationAdapter {
	private Texture dropImage;
    private Texture bucketImage;
    private Sound dropSound;
    private Music rainMusic;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Rectangle bucket;

	@Override
	public void create () {
		//load images for the bucket and water drop, 64x64 pixels
        dropImage = new Texture(Gdx.files.internal("droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));

        //load sound files for sound effect and background music
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        //start playing background music immediately
        rainMusic.setLooping(true);
        rainMusic.play();

        //create camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        //create SpriteBatch
        batch = new SpriteBatch();

        //create rectangle for bucket display info
        bucket = new Rectangle();
        bucket.x = 800/2 -64/2;
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;
	}

	@Override
	public void render () {
        //clear screen with dark blue
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //good practice to update the camera once per frame, though not done here
        camera.update();

        //render bucket
        batch.setProjectionMatrix(camera.combined);     //use coordinate system of the camera
        batch.begin();
        batch.draw(bucketImage, bucket.x, bucket.y);
        batch.end();

        //move the bucket on user input
        if(Gdx.input.isTouched()){
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = (int)touchPos.x - 64/2;
        }

	}
}
