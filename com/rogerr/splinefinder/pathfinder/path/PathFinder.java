package com.rogerr.splinefinder.pathfinder.path;

import Jama.Matrix;
import com.rogerr.splinefinder.pathfinder.PathRenderer;
import com.rogerr.splinefinder.pathfinder.astar.AStarNode;
import com.rogerr.splinefinder.pathfinder.astar.AStarPathFinder;
import com.rogerr.splinefinder.util.Util;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PathFinder {


    private static PathFinder finder;
    public PathFinder() {
        finder = this;
    }

    public void find(BlockPos begin, BlockPos end, int depth) {
        List<AStarNode> nodes = AStarPathFinder.compute(begin, end, depth);
        List<Vec3> points = new ArrayList<>();
        List<Vec3> dir = new ArrayList<>();
        Vec3 prevDir = new Vec3(0,0,0);
        for(int i = 1; i < nodes.size(); i++) {
            Vec3 sum = nodes.get(i).asVec3(0,0,0).add(nodes.get(i-1).asVec3(0,0,0));
            Vec3 direction = nodes.get(i).asVec3(0,0,0).subtract(nodes.get(i-1).asVec3(0,0,0));



            Vec3 midpoint = new Vec3(sum.xCoord/2f,sum.yCoord/2f, sum.zCoord/2f);


            if(points.size()>0)
                points.addAll(interpolateSpline(points.get(points.size()-1), prevDir, midpoint, direction, 30));
            else
                points.add(midpoint);


            prevDir = direction;
        }

        //
        // out.println(points.size());

        PathRenderer.getInstance().setPoints(points);
    }



    // perpendicular gradient
    List<Vec3> interpolateSpline(Vec3 begin, Vec3 beginDir, Vec3 end, Vec3 endDir, int amount) {

        List<Vec3> points = new ArrayList<>();
        // ax^3 + bx^2 + cx + d = z
        // 3ax^2 + 2bx + c = grad
        // 6ax + 2b = 0

        // think about case when the derivative is pointing the same side which means it becomes a relation
        if(begin.xCoord!=end.xCoord && begin.zCoord != end.zCoord) {

            double bx = begin.xCoord;
            double ex = end.xCoord;

            int POLYNOMIAL_DEGREE = 3;

            double[][] array = new double[4][POLYNOMIAL_DEGREE+1];

            // point equality
            for(int i = 0; i <= POLYNOMIAL_DEGREE; i++) {
                array[0][i] = Math.pow(bx, POLYNOMIAL_DEGREE-i);
                array[1][i] = Math.pow(ex, POLYNOMIAL_DEGREE-i);
            }

            // derivative
            for(int i = 0; i <= POLYNOMIAL_DEGREE-1; i++) {
                array[2][i] = (POLYNOMIAL_DEGREE-i) * Math.pow(bx, POLYNOMIAL_DEGREE-i-1);
                array[3][i] = (POLYNOMIAL_DEGREE-i) * Math.pow(ex, POLYNOMIAL_DEGREE-i-1);
            }

            // second derivative if needed
            // --//


            // limit the angle cuz cubic polynomial has hard time connecting perpendicular tangent (many oscillation)
            double angle = Util.calculateAngleVec2D(beginDir, endDir);

            int ANGLE_LIMIT = 70;
            if(angle>ANGLE_LIMIT) {

                double rotateAmount = angle-ANGLE_LIMIT;

                // either leftRightVec2D is not right or something is not right here. Intuitively if isRight is true, beignDir should
                // increase in yaw and endDir should decrease. however in practice the opposite yields the correct results. Look into this later but
                // for now, if it works it works.
                boolean isRight = Util.leftRightVec2D(endDir,beginDir);

                if(!isRight) {
                    beginDir = beginDir.rotateYaw((float)Math.toRadians(rotateAmount));
                    endDir = endDir.rotateYaw((float)Math.toRadians(-rotateAmount));
                } else {
                    beginDir = beginDir.rotateYaw((float)Math.toRadians(-rotateAmount));
                    endDir = endDir.rotateYaw((float)Math.toRadians(rotateAmount));
                }

            }

            double bGrad = beginDir.zCoord / (beginDir.xCoord==0?0.01:beginDir.xCoord);
            double eGrad = endDir.zCoord / (endDir.xCoord==0?0.01:endDir.xCoord);

            Matrix A = new Matrix(array);




            //System.out.println(A.rank());
           // printMatrix(A);

            Matrix B = new Matrix(new double[][]{
                    {begin.zCoord},
                    {end.zCoord},
                    {bGrad},
                    {eGrad},
   });
            printMatrix(B);

            Matrix ans = A.solve(B);

            double[] coefficients = new double[POLYNOMIAL_DEGREE+1];
            for(int i = 0; i < POLYNOMIAL_DEGREE+1; i++) {
                coefficients[i]=ans.get(i,0);
            }

            Function<Double, Double> get = (Double x) -> {
                double sum = 0d;
                for(int i = 0; i <= POLYNOMIAL_DEGREE; i++) {
                    sum+= coefficients[i]*Math.pow(x, POLYNOMIAL_DEGREE-i);
                }
                return sum;
            };

           // System.out.println(begin + " " + end);
            //System.out.println(beginDir + " " + endDir);
            double x = begin.xCoord;
            for(int i = 0; i < amount-1; i++) {
                x += (end.xCoord-begin.xCoord)/amount;
                points.add(new Vec3(x, begin.yCoord, get.apply(x)));
            }

            for(Vec3 point : points) {
             //   System.out.println(point);
            }
        } else {

            double x = begin.xCoord;
            double y = begin.yCoord;
            double z = begin.zCoord;
            for (int i = 0; i < amount - 1; i++) {
                x += (end.xCoord - begin.xCoord) / amount;
                y += (end.yCoord - begin.yCoord) / amount;
                z += (end.zCoord - begin.zCoord) / amount;

                points.add(new Vec3(x, y, z));
            }
        }

        points.add(end);
        return points;

    }

    public static void printMatrix(Matrix m) {
        double[][] d = m.getArray();

        for(int i = 0; i < d.length; i++) {
            for(int j = 0; j < d[i].length; j++) {
                System.out.print(d[i][j] + " ");
            }
            System.out.println();
        }
    }


    public static PathFinder getInstance() {
        return finder;
    }

}
