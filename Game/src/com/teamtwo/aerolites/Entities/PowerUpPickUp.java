package com.teamtwo.aerolites.Entities;

import com.teamtwo.engine.Messages.Message;
import com.teamtwo.engine.Messages.Types.CollisionMessage;
import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.MathUtil;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

/**
 * Created by Tijan Weir
 */
public class PowerUpPickUp extends Entity {

    private float lifeTime;
    private final float MAX_LIFE_TIME;
    int[] PowerUpType = new int[4];
    //The powers up are:
    //ToDo above powerups

    public PowerUpPickUp(float lifeTime, Vector2f position, World world) {
        for(int i = 0; i < PowerUpType.length; i++)
        {
            PowerUpType[i] = i;
        }


        MAX_LIFE_TIME = lifeTime;
        this.lifeTime = 0;
        onScreen = true;

        BodyConfig config = new BodyConfig();

        //Can change the shape here
        Vector2f shape[] = new Vector2f[4];
        shape[0] = new Vector2f(0f, 0);
        shape[1] = new Vector2f(0f, 30);
        shape[2] = new Vector2f(30f, 30);
        shape[3] = new Vector2f(30, 0);

        this.offScreenAllowance = new Vector2f(0, 0);

        config.shape = new Polygon(shape);
        config.position = position;
        config.mask = CollisionMask.POWERUP;
        config.category = CollisionMask.PLAYER;
        config.angularVelocity = MathUtil.randomFloat(-0.2f, 0.2f);

        body = world.createBody(config);
        body.setData(this);

        this.renderColour = Color.BLUE;

    }

    @Override
    public void receiveMessage(Message message){

    }

    @java.lang.Override
    public Entity.Type getType() {
        return Type.PowerUp;
    }

    @Override
    public void render(RenderWindow window) {
        super.render(window);
    }

    public int[] getPowerUpType() {
        return PowerUpType;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        lifeTime += dt;
        if (lifeTime > MAX_LIFE_TIME) {
            onScreen = false;
        }
    }

}
