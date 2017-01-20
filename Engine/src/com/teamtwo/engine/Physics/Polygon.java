package com.teamtwo.engine.Physics;

import com.teamtwo.engine.Physics.Collisions.AABB;
import com.teamtwo.engine.Utilities.Interfaces.Initialisable;
import com.teamtwo.engine.Utilities.MathUtil;
import org.jsfml.system.Vector2f;

/**
 * A class which represents a polygon shape that can be used with a {@link RigidBody}
 */
public class Polygon implements Initialisable<RigidBody> {

    /**
     * The maximum number of vertices a polygon can have for performance reasons
     */
    private static final int MAX_VERTICES = 16;

    /** The vertices which make up the polygon */
    private Vector2f[] vertices;
    /** The body associated with the polygon */
    private RigidBody body;

    /**
     * Creates a randomly generated polygon
     */
    public Polygon() {
        // Pick a random radius between 10 and 100 and generate
        this(MathUtil.randomFloat(10, 50));
    }

    /**
     * Creates a randomly generated polygon where all of the vertices are within the radius specified
     * @param radius The radius to generate the vertices in
     */
    public Polygon(float radius) {
        // Temporary vertex array with MAX_VERTICES
        Vector2f[] tmp = new Vector2f[MAX_VERTICES];

        // Generate all of the vertices needed
        int vertexCount = 0;
        for(float angle = 0; angle < MathUtil.PI2;) {

            tmp[vertexCount] = new Vector2f(MathUtil.cos(angle) * radius, MathUtil.sin(angle) * radius);

            vertexCount++;
            angle += MathUtil.randomFloat(20, 80) * MathUtil.DEG_TO_RAD;
        }

        // Check vertex count
        if(vertexCount >= MAX_VERTICES) {
            throw new IllegalStateException("Error: A Polygon cannot have more than " + MAX_VERTICES + " vertices");
        }

        // Resize the vertex array so it's not taking up more space
        vertices = new Vector2f[vertexCount];
        System.arraycopy(tmp, 0, vertices, 0, vertexCount);

        // Set the body to be null as it isn't attached to one
        body = null;
    }

    /**
     * Creates a new polygon from the vertices given
     * @param vertices The vertices to make up the polygon
     */
    public Polygon(Vector2f[] vertices) {
        // Check vertex count
        if(vertices.length >= MAX_VERTICES) {
            throw new IllegalStateException("Error: A Polygon cannot have more than " + MAX_VERTICES + " vertices");
        }

        // Find the centroid to make sure it is (0, 0)
        Vector2f centroid = ShapeUtil.findCentroid(vertices);

        // If the centroid is not at (0, 0) then translate all vertices so the centroid is
        this.vertices = new Vector2f[vertices.length];
        for(int i = 0; i < vertices.length; i++) {
            this.vertices[i] = Vector2f.sub(vertices[i], centroid);
        }

        // Set body to be null because it isn't attached
        body = null;
    }

    /**
     * Initialises the shape and calculates the mass and inertia of the body supplied
     * @param body The
     */
    public void initialise(RigidBody body) {
        this.body = body;

        // Calculate the area and set the mass
        float area = ShapeUtil.findArea(vertices);
        body.setMass(body.getDensity() * area);

        // Use the moment of inertia of a rectangle
        // This is because it is way too complicated to work out the inertia of the actual shape
        AABB aabb = new AABB(vertices);
        float inertia = ((4 * aabb.getHalfSize().x * aabb.getHalfSize().y)) / 12f;
        inertia *= (MathUtil.squared(aabb.getHalfSize().x * 2) + MathUtil.squared(aabb.getHalfSize().y * 2));

        // You can get huge inertia values from this but they seem to work all the same
        body.setInertia(Math.abs(inertia));
    }

    /**
     * Gets the untransformed vertices which make up the shape
     * @return The untransformed vertices
     */
    public Vector2f[] getVertices() { return vertices; }

    /**
     * Gets the transformed vertices relative to the body that this shape is attached to
     * @return The transformed vertices
     */
    public Vector2f[] getTransformed() {
        Vector2f[] transformed = new Vector2f[vertices.length];
        float sin = MathUtil.sin(body.getTransform().getAngle());
        float cos = MathUtil.cos(body.getTransform().getAngle());

        for(int i = 0; i < vertices.length; i++) {
            Vector2f vertex = vertices[i];
            float x = (vertex.x * cos) - (vertex.y * sin);
            float y = (vertex.x * sin) + (vertex.y * cos);

            transformed[i] = Vector2f.add(new Vector2f(x, y), body.getTransform().getPosition());
        }

        return transformed;
    }
}
