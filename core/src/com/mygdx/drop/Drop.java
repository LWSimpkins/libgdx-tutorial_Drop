package com.mygdx.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import com.badlogic.gdx.math.Rectangle;
import java.util.Iterator;

public class Drop extends ApplicationAdapter {
	private Texture dropImage;
    private Texture bucketImage;
    private Sound dropSound;
    private Music rainMusic;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Rectangle bucket;
    private Vector3 touchPos;

    private Array<Rectangle> raindrops;
    private long lastDropTime;

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

        //create vector3, is 3D coordinate
        touchPos = new Vector3();

        //create raindrop array, spawn first raindrop
        raindrops = new Array<Rectangle>();
        spawnRaindrop();
	}

	@Override
	public void render () {
        //clear screen with dark blue
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //good practice to update the camera once per frame, though not done here
        camera.update();

        //render bucket, raindrops
        batch.setProjectionMatrix(camera.combined);     //use coordinate system of the camera
        batch.begin();
        batch.draw(bucketImage, bucket.x, bucket.y);
        for(Rectangle raindrop : raindrops)
        {
            batch.draw(dropImage, raindrop.x, raindrop.y);
        }
        batch.end();

        //move the bucket on user touch/mouse input
        if(Gdx.input.isTouched()){
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos); //convert coordinates to cameras coordinate system
            bucket.x = (int)touchPos.x - 64/2;
        }

        //move bucket on keyboard input, 200px per second
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

        //keep bucket within screen limits
        if(bucket.x <0) bucket.x = 0;
        if(bucket.x > 800 - 64) bucket.x = 800 - 64;

        //check time since last raindrop spawned. create new one if necessary
        if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

        //make raindrops move
        Iterator<Rectangle> iter = raindrops.iterator();
        while(iter.hasNext()){
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if(raindrop.y +64 < 0) iter.remove();

            //play sound, remove raindrop if it hits the bucket
            if(raindrop.overlaps(bucket)){
                dropSound.play();
                iter.remove();
            }
        }
    }

    private void spawnRaindrop(){
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800-64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }
}
