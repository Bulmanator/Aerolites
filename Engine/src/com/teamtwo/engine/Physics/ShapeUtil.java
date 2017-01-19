package com.teamtwo.engine.Physics;

import com.teamtwo.engine.Utilities.MathUtil;
import org.jsfml.system.Vector2f;

/**
 * Some utility methods for shapes
 */
final class ShapeUtil {

    /**
     * Calculates the area of a polygon
     * @param vertices The vertices which make up the polygon
     * @return The area of the polygon
     */
    static final float findArea(Vector2f[] vertices) {
        float area = 0;

        for(int i = 0, j = vertices.length - 1; i < vertices.length; j = i++) {
            area += MathUtil.cross(vertices[i], vertices[j]);
        }

        return Math.abs(0.5f * area);
    }

    /**
     * Calculates the centroid of a convex polygon using the polygonal centroid formula
     * @param vertices The vertices that make up the polygon
     * @return The centroid of the polygon
     */
    static final Vector2f findCentroid(Vector2f[] vertices) {
        float x = 0, y = 0;
        float area = 0;

        for(int i = 0, j = vertices.length - 1; i < vertices.length; j = i++) {
            Vector2f v0 = vertices[i];
            Vector2f v1 = vertices[j];

            float a = MathUtil.cross(v0, v1);
            area += a;
            x += (v0.x + v1.x) * a;
            y += (v0.y + v1.y) * a;
        }

        area *= 0.5f;

        x /= (6f * area);
        y /= (6f * area);

        return new Vector2f(x, y);
    }

}
