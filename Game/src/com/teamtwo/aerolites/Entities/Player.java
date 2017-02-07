package com.teamtwo.aerolites.Entities;

import com.teamtwo.engine.Graphics.Particles.ParticleConfig;
import com.teamtwo.engine.Graphics.Particles.ParticleEmitter;
import com.teamtwo.engine.Input.Controllers.Controller;
import com.teamtwo.engine.Input.Controllers.Controllers;
import com.teamtwo.engine.Messages.Message;
import com.teamtwo.engine.Messages.Types.CollisionMessage;
import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.MathUtil;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;

/**
 * @author Matthew Threlfall
 */
public class Player extends Entity {

    private final float ROTATION_SPEED = MathUtil.PI*1.2f;
    private final float FORCE_FROM_JET = 100000;
    private final float TIME_BETWEEN_SHOTS = 0.15f;
    private boolean controller;
    private Controller.Player controllerNum;

    private ParticleEmitter jet;
    private float shootCooldown;
    private boolean shoot;

    public Player(World world) {
        BodyConfig config = new BodyConfig();
        controller = false;

        Vector2f[] vertices = new Vector2f[4];
        vertices[0] = new Vector2f(0, -15);
        vertices[1] = new Vector2f(15, 30);
        vertices[2] = new Vector2f(0, 35);
        vertices[3] = new Vector2f(-15, 30);

        offScreenAllowance = new Vector2f(15,15);

        config.position = new Vector2f(State.WORLD_SIZE.x/2, State.WORLD_SIZE.y/2);
        config.shape = new Polygon(vertices);

        config.restitution = 0.4f;
        config.velocity = new Vector2f(0,0);
        config.angularVelocity = 0;

        config.density = 0.6f;
        body = world.createBody(config);
        body.setData(this);
        body.registerObserver(this, Message.Type.Collision);

        ParticleConfig pConfig = new ParticleConfig();


        pConfig.minAngle = 0;
        pConfig.maxAngle = 0;
        pConfig.speed = 70;
        pConfig.rotationalSpeed = 40;
        pConfig.pointCount = 3;
        pConfig.colours[0] = Color.RED;
        pConfig.colours[1] = Color.MAGENTA;
        pConfig.colours[2] = Color.YELLOW;
        pConfig.colours[3] = Color.WHITE;
        pConfig.colours[4] = Color.BLUE;
        pConfig.colours[5] = Color.RED;
        pConfig.colours[6] = Color.GREEN;
        pConfig.colours[7] = Color.YELLOW;
        pConfig.fadeOut = false;
        pConfig.startSize = 14;
        pConfig.endSize = 4;
        pConfig.minLifetime = 1.5f;
        pConfig.maxLifetime = 6;


        pConfig.position = body.getTransform().getPosition();
        jet = new ParticleEmitter(pConfig, 40f, 400);
        body.registerObserver(this, Message.Type.Collision);

        renderColour = Color.GREEN;
        shoot = false;
    }

    @Override
    public void update(float dt) {

        // Update the particle emitter
        jet.update(dt);
        shootCooldown += dt;
        super.update(dt);

        input();
    }

    @Override
    public void render(RenderWindow renderer) {
        jet.render(renderer);
        super.render(renderer);
    }

    @Override
    public void receiveMessage(Message message) {
        if(message.getType() == Message.Type.Collision) {
            CollisionMessage cm = (CollisionMessage) message;
            ///System.out.println(cm.getBodyA().getData().getType() + " collided with " + cm.getBodyB().getData().getType());
            if(cm.getBodyB().getData().getType() != Type.Bullet){
                //System.exit(0);
            }
        }
    }

    private void input(){
        if(!controller) {
            if (Keyboard.isKeyPressed(Keyboard.Key.W)) {
                boost();
            } else {
                // Deactivate the particle emitter as the ship is not boosting
                jet.setActive(false);
            }
            if (Keyboard.isKeyPressed(Keyboard.Key.D)) {
                turn(false);
            } else if (Keyboard.isKeyPressed(Keyboard.Key.A)) {
                turn(true);
            } else {
                body.setAngularVelocity(0);
            }
            if (Keyboard.isKeyPressed(Keyboard.Key.SPACE)) {
                shoot();
            }
        }
        else{
            if (Keyboard.isKeyPressed(Keyboard.Key.W)) {
                boost();
            } else {
                // Deactivate the particle emitter as the ship is not boosting
                jet.setActive(false);
            }
            if (Controllers.getThumbstickValues(controllerNum, Controller.Thumbstick.Left).x > 0) {
                turn(false);
            } else if (Keyboard.isKeyPressed(Keyboard.Key.A)) {
                turn(true);
            } else {
                body.setAngularVelocity(0);
            }
            if (Keyboard.isKeyPressed(Keyboard.Key.SPACE)) {
                shoot();
            }
        }
    }

    private void shoot(){
        if (shootCooldown > TIME_BETWEEN_SHOTS) {
            shootCooldown = 0;
            shoot = true;
        }
    }

    private void turn(boolean left){
        if(left)
            body.setAngularVelocity(-ROTATION_SPEED);
        else
            body.setAngularVelocity(ROTATION_SPEED);
    }

    private void boost(){
        // Apply force to move the ship
        move(0, -FORCE_FROM_JET);

        // Move the particle emitter to be at the bottom of the ship
        jet.getConfig().position = body.getTransform().apply(new Vector2f(0, 15));
        jet.setActive(true);

        jet.getConfig().maxAngle = -body.getTransform().getAngle() * MathUtil.RAD_TO_DEG - 60;
        jet.getConfig().minAngle = -body.getTransform().getAngle() * MathUtil.RAD_TO_DEG - 120;
    }

    public boolean shooting(){
        if(shoot){
            shoot = false;
            return true;
        }
        return false;
    }



    @Override
    public Type getType() { return Type.Player; }
}
