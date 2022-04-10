package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.agents.Agent;
import app.model.soundBoundary.SoundBoundary;
import java.util.ArrayList;
import java.util.Stack;

public class SoundEngine
{
    public static ArrayList<Vector> compute(Map map, Vector origin)
    {
        ArrayList<SoundBoundary> boundaries = collectSoundBoundaries(map);
        ArrayList<Vector> output = new ArrayList<>();
        Stack<SoundRay> rays = SoundRayScatter.angle360(origin, 10, 1000);

        while(!rays.isEmpty())
        {
            SoundRay r = rays.pop();
            Vector agentIntersection = getAgentIntersection(r, map.getAgents());
            Vector bdyIntersection = getIntersection(r, boundaries);

            if(bdyIntersection != null && agentIntersection != null)
                output.add(closestPoint(r.getU(), agentIntersection, bdyIntersection));

            else if(bdyIntersection != null)
            {
                if(r.getBounces() > 0)
                    rays.addAll(SoundRayScatter.angle360(r.getU(), 10, 1000));
                output.add(bdyIntersection);
            }
            else if(agentIntersection != null)
                output.add(agentIntersection);
        }
        return output;
    }

    private static Vector getIntersection(SoundRay r, ArrayList<SoundBoundary> boundaries)
    {
        Vector intersection = null;
        double closestDist = Double.MAX_VALUE;
        for (SoundBoundary obj: boundaries)
        {
            if(obj.intersects(r))
            {
                Vector currentV = obj.intersection(r);
                double dist = r.getU().dist(currentV);
                if(dist < closestDist)
                {
                    intersection = currentV;
                    closestDist = dist;
                }
            }
        }
        return intersection;
    }

    private static Vector getAgentIntersection(SoundRay r, ArrayList<Agent> agents)
    {
        Vector intersection = null;
        double closestDist = Double.MAX_VALUE;
        for (Agent agent: agents)
        {
            if (agent.isHit(r))
            {
                Vector currentV = agent.intersection(r);
                double dist = r.getU().dist(currentV);
                if (dist < closestDist && currentV.getAngle() == r.angle())
                {
                    intersection = currentV;
                    closestDist = dist;
                }
            }
        }
        return intersection;
    }

    private static ArrayList<SoundBoundary> collectSoundBoundaries(Map map)
    {
        ArrayList<SoundBoundary> soundBoundaries = new ArrayList<>();
        map.getFurniture()
           .forEach(furniture -> soundBoundaries.addAll(furniture.getSoundBoundaries()));
        return soundBoundaries;
    }

    private static Vector closestPoint(Vector origin, Vector a, Vector b)
    {
        if(a.dist(origin) <= b.dist(origin))
            return a;
        else
            return b;
    }
}
