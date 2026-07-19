package com.voltaire.d_n_a.dungeons_and_arcanus.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class VoxelShaper {

    protected static VoxelShape rotatedCopy(VoxelShape shape, float rotation) {
        Vec3 center = new Vec3(8.0, 8.0, 8.0);

        Stream<VoxelShape>[] stream = new Stream[]{Stream.empty()};

        shape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
            Vec3 v1 = new Vec3(x1, y1, z1).subtract(center);
            Vec3 v2 = new Vec3(x2, y2, z2).subtract(center);

            v1 = rotate(v1, rotation).add(center);
            v2 = rotate(v2, rotation).add(center);

            // Fix min/max order
            if (v1.x > v2.x) {
                Vec3 tmp = v1;
                v1 = new Vec3(v2.x, v1.y, v1.z);
                v2 = new Vec3(tmp.x, v2.y, v2.z);
            }
            if (v1.z > v2.z) {
                Vec3 tmp = v1;
                v1 = new Vec3(v1.x, v1.y, v2.z);
                v2 = new Vec3(v2.x, v2.y, tmp.z);
            }

            VoxelShape rotated = Block.box(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
            stream[0] = Stream.concat(stream[0], Stream.of(rotated));
        });

        return stream[0].reduce(Shapes::or).get();
    }

    private static Vec3 rotate(Vec3 v, float rotation) {
        double rot = Math.toRadians(rotation);
        double cos = Math.cos(rot);
        double sin = Math.sin(rot);

        return new Vec3(
                v.x * cos - v.z * sin,
                v.y,
                v.x * sin + v.z * cos
        );
    }

    public static Map<Direction, VoxelShape> generateRotations(VoxelShape shape) {
        Map<Direction, VoxelShape> shapes = new HashMap<>();
        shapes.put(Direction.NORTH, shape);
        shapes.put(Direction.EAST, rotatedCopy(shape, 90.0F));
        shapes.put(Direction.SOUTH, rotatedCopy(shape, 180.0F));
        shapes.put(Direction.WEST, rotatedCopy(shape, 270.0F));
        return shapes;
    }
}
