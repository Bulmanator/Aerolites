package com.teamtwo.aerolites.Entities;

import com.teamtwo.engine.Graphics.Particles.ParticleConfig;
import com.teamtwo.engine.Graphics.Particles.ParticleEmitter;
import com.teamtwo.engine.Input.Controllers.*;
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

    // #### Static Begin ####

    // The base vertices which make up the player ship shape
    private static final Vector2f[] vertices = new Vector2f[] {
            new Vector2f(0, -15), new Vector2f(15, 30),
            new Vector2f(0, 35), new Vector2f(-15, 30)
    };

    // Constant values for movement
    private static final float rotationSpeed = MathUtil.PI * 1.2f;
    private static final float forceFromJet = 100000;
    // The delay between shots
    private static final float timeBetweenShots = 0.15f;

    // #### Static End ####


    // Whether or not the Player is controlled via a controller
    private boolean controller;
    // The Player ID
    private PlayerNumber player;

    // The amount of lives the player has left
    private int lives;
    private boolean alive;

    // The particle emitter for the jet engine
    private ParticleEmitter jet;

    private float shootCooldown;
    private boolean shoot;

    private Color defaultColour;

    private float immuneTime;

    //Scoring
    ScoreObject score;


    public Player(World world, PlayerNumber player) {

        BodyConfig config = new BodyConfig();
        controller = false;

        lives = 2;
        alive = true;

        immuneTime = 0;
        score = new ScoreObject();

        config.category = CollisionMask.PLAYER;
        config.mask = (CollisionMask.ALL & ~CollisionMask.BULLET);

        config.position = new Vector2f(State.WORLD_SIZE.x / 2, State.WORLD_SIZE.y / 2);
        config.shape = new Polygon(vertices);

        config.restitution = 0.4f;
        config.velocity = new Vector2f(0,0);
        config.angularVelocity = 0;

        config.density = 0.6f;

        body = world.createBody(config);
        body.setData(this);
        body.registerObserver(this, Message.Type.Collision);

        offScreenAllowance = new Vector2f(15, 15);

        // Generating the particle configuration for the jet stream
        ParticleConfig pConfig = new ParticleConfig();

        pConfig.minAngle = 0;
        pConfig.maxAngle = 0;
        pConfig.speed = 70;
        pConfig.rotationalSpeed = 40;
        pConfig.pointCount = 3;
        pConfig.fadeOut = false;
        pConfig.startSize = 14;
        pConfig.endSize = 4;
        pConfig.minLifetime = 1.5f;
        pConfig.maxLifetime = 3;

        pConfig.position = body.getTransform().getPosition();

        jet = new ParticleEmitter(pConfig, 40f, 400);

        renderColour = Color.GREEN;
        defaultColour = Color.GREEN;
        shoot = false;

        this.player = player;
    }

    /**
     * Handles the input for the player
     * @param dt The time since the last frame, in seconds
     */
    private void handleInput(float dt) {

        boolean boost, shouldShoot;
        float rotate = 0;

        // Get whether the player is boosting,
        if(controller) {
            // Store the current controller state
            ControllerState state = Controllers.getState(player);

            // Get the button states for boost, shoot and rotate
            boost = state.button(Button.RB);
            shouldShoot = state.button(Button.A);
            rotate = (state.thumbstick(Thumbstick.Left).x / 100f) * rotationSpeed;
        }
        else {
            // Get the keyboard state for boost, shoot and rotate
            boost = Keyboard.isKeyPressed(Keyboard.Key.W);
            shouldShoot = Keyboard.isKeyPressed(Keyboard.Key.SPACE);
            rotate -= Keyboard.isKeyPressed(Keyboard.Key.A) ? rotationSpeed : 0;
            rotate += Keyboard.isKeyPressed(Keyboard.Key.D) ? rotationSpeed : 0;
        }

        // Apply movement based off values yielded above
        if(boost) {
            Vector2f force = body.getTransform().applyRotation(new Vector2f(0, -forceFromJet));
            body.applyForce(force);

            timeBoosting += dt;

            jet.getConfig().position = body.getTransform().apply(new Vector2f(0, 15));

            jet.getConfig().maxAngle = -body.getTransform().getAngle() * MathUtil.RAD_TO_DEG - 60;
            jet.getConfig().minAngle = -body.getTransform().getAngle() * MathUtil.RAD_TO_DEG - 120;
        }
        jet.setActive(boost);
        body.setAngularVelocity(rotate);

        // If the player should shoot check if the can
        if(shouldShoot) {
            if(shootCooldown > timeBetweenShots) {
                shootCooldown = 0;
                shoot = true;
            }
        }
    }

    /**
     * Updates the player by one iteration
     * @param dt The time since the last frame
     */
    public void update(float dt) {
        if(!alive) return;

        // Update the particle emitter
        jet.update(dt);

        shootCooldown += dt;
        timeAlive += dt;
        renderColour = defaultColour;
        score.incrementTimeAlive(dt);
        if(immuneTime>0) {
            immuneTime -= dt;
            if (MathUtil.round(immuneTime % 0.3f, 1) == 0)
                renderColour = Color.WHITE;
        }

        // Update base
        super.update(dt);

        // Handle input
        handleInput(dt);
    }

    /**
     * Draws the player on the screen
     * @param renderer The RenderWindow used to draw
     */
    public void render(RenderWindow renderer) {
        if(!alive) return;

        // Draw the jet and shape
        jet.render(renderer);
        super.render(renderer);
    }

    /**
     * Used to receive messages when subscribed events occur
     * @param message The message which was sent
     */
    public void receiveMessage(Message message) {
        if(message.getType() == Message.Type.Collision) {
            CollisionMessage cm = (CollisionMessage) message;
            Type typeA = (Entity.Type) cm.getBodyA().getData().getType();
            Type typeB = (Entity.Type) cm.getBodyB().getData().getType();

            if(typeA != Type.Bullet && typeB != Type.Bullet){
                if(typeA != Type.Player || typeB != Type.Player) {
                    if(immuneTime <= 0) {
                        lives--;
                        if(lives < 0) alive = false;
                        immuneTime = 3;
                    }
                }
            }
        }
    }

    /**
     * Gets the Player type
     * @return {@link Type#Player}
     */
    public Type getType() { return Type.Player; }

    /**
     * Whether or not the player is alive
     * @return True if the player is alive, otherwise false
     */
    public boolean isAlive() { return alive; }

    /**
     * Gets whether or not the player is shooting
     * @return True if the player is shooting, otherwise false
     */
    public boolean isShooting() {
        if(shoot) {
            shoot = false;
            return true;
        }

        return false;
    }

    /**
     * Gets the number of bullets the player has fired
     * @return The number of bullets
     */
    public float getBulletsFired() { return bulletsFired; }

    /**
     * Gets the number of bullets the player has missed
     * @return The number of missed bullets
     */
    public float getBulletsMissed() { return bulletsMissed; }

    /**
     * Gets the number of lives the player has left
     * @return The life count
     */
    public int getLives() { return lives; }

    public Color getDefaultColour() { return defaultColour; }

    public float getTimeAlive() { return timeAlive; }

    public float getTimeBoosting() { return timeBoosting; }

    public void setLives(int lives) { this.lives = lives; }

    public void incrementBulletsMissed() { bulletsMissed++; }
    public void incrementBulletsShot() { bulletsFired++; }

    public ScoreObject getScore() {
        return score;
    }
}
