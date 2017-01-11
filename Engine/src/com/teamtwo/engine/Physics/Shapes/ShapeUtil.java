package com.teamtwo.engine.Physics.Shapes;

import com.teamtwo.engine.Utilities.MathUtil;
import org.jsfml.system.Vector2f;

public final class ShapeUtil {

    private ShapeUtil() {}

    // TODO Implement All
    // Remember to make package-private after testing

    public static void getAABB(AABB aabb, Vector2f[] vertices) {
        float minX, minY;
        float maxX, maxY;

        minX = maxX = vertices[0].x;
        minY = maxY = vertices[0].y;

        for(int i = 1; i < vertices.length; i++) {
            Vector2f v = vertices[i];

            if(v.x < minX) { minX = v.x; }
            else if(v.x > maxX) { maxX = v.x; }

            if(v.y < minY) { minY = v.y; }
            else if(v.y > maxY) { maxY = v.y; }
        }

        Vector2f halfSize = new Vector2f((maxX - minX) / 2f, (maxY - minY) / 2f);
        Vector2f centre = Vector2f.add(new Vector2f(minX, minY), halfSize);

        System.out.println("Min X: " + minX + " Min Y: " + minY);
        System.out.println("Max X: " + maxX + " Max Y: " + maxY);

        aabb.setCentre(centre);
        aabb.setHalfSize(halfSize);
    }

    static Vector2f getCentroid(Vector2f[] vertices) { return null; }

    public static void getNormals(Vector2f[] vertices, Vector2f[] normals) {
        for(int i = 0, j = vertices.length - 1; i < vertices.length; j = i++) {
            Vector2f v =  Vector2f.sub(vertices[i], vertices[j]);
            normals[i] = MathUtil.normalise(new Vector2f(v.y, -v.x));
        }
    }


}
