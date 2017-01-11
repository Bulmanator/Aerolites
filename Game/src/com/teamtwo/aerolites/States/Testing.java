package com.teamtwo.aerolites.States;

import com.teamtwo.aerolites.Aerolites;
import com.teamtwo.aerolites.Controls;
import com.teamtwo.engine.Graphics.Animation;
import com.teamtwo.engine.Graphics.Particles.Particle;
import com.teamtwo.engine.Graphics.Particles.ParticleEmitter;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

public class Testing extends State {

    private Controls controls;

    private ParticleEmitter emitter2;
    private ParticleEmitter emitter;
    private float angleMin, angleMax;

    private Animation animation;

    private boolean add;

    private Font font;

    public Testing(GameStateManager gsm) {
        super(gsm);

        ContentManager.instance.loadFont("Ubuntu", "Fonts/Ubuntu.ttf");
        ContentManager.instance.loadTexture("PlayerMove", "Textures/PlayerMove.png");

        font = ContentManager.instance.getFont("Ubuntu");

        animation = new Animation("PlayerMove", 1, 5);
        animation.setPosition(200, 250);

        Aerolites a = (Aerolites)game;

        controls = a.getControls();

        Particle.Configuration config = new Particle.Configuration();

        config.position = new Vector2f(640, 360);
        config.speed = 60;
        config.minAngle = 180;
        config.maxAngle = 180;
        config.rotationalSpeed = 60;

        config.pointCount = 6;
        config.fadeOut = true;

        config.startSize = 12;
        config.endSize = 4;

        config.colours[0] = new Color(53, 255, 234);
        config.colours[1] = new Color(0, 174, 255);

        config.minLifetime = 1.3f;
        config.maxLifetime = 1.3f;

        emitter = new ParticleEmitter(config, 20, 450);
        emitter.setActive(true);

        angleMin = 65;
        angleMax = 115;

        config.colours[0] = new Color(0, 255, 33);

        config.position = new Vector2f(config.position.x, config.position.y - 60);

        emitter2 = new ParticleEmitter(config, 20, 450);
    }

    public void update(float dt) {

        if(controls.isMousePressed()) {
            animation.setPosition(controls.getPosition());
        }

        if(controls.isePressed()) {
            angleMin += 10;
            angleMax += 10;
        }
        else if(controls.isqPressed()) {
            angleMin -= 10;
            angleMax -= 10;
        }

        if(angleMin < 0) angleMin += 360;
        if(angleMin > 360) angleMin -= 360;

        if(angleMax < 0) angleMax += 360;
        if(angleMax > 360) angleMax -= 360;

        emitter.getConfig().minAngle = angleMin;
        emitter.getConfig().maxAngle = angleMax;

        emitter2.getConfig().minAngle = angleMin;
        emitter2.getConfig().maxAngle = angleMax;

        animation.setRotation(angleMin);

        emitter.update(dt);
        emitter2.update(dt);
        animation.update(dt);
    }

    public void render() {
        emitter.render(window);
        emitter2.render(window);
        animation.render(window);

        Text t = new Text("Hello World!", ContentManager.instance.getFont("Ubuntu"), 20);
        t.setPosition(100, 100);

        window.draw(t);
    }

    public void dispose() {}
}
