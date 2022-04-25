package testing;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.agents.Agent;
import app.model.agents.Human;
import app.model.agents.Team;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RayFeedbackTest
{
    GraphicsEngine ge = new GraphicsEngine(180);
    Vector agent1Pos = new Vector(10, 10);
    Vector agent1Dir = new Vector(1, 0);
    Vector agent2Pos = new Vector(10, 20);
    Vector agent2Dir = new Vector(-1, 0);

    Human agent1 = new Human(agent1Pos, agent1Dir, 1, Team.GUARD);
    Human agent2 = new Human(agent2Pos, agent2Dir, 1, Team.INTRUDER);


    @Test void rayWithGuard()
    {
        Vector start = new Vector();
        Vector end = new Vector(10,10);
        Ray r = new Ray(start, end, Team.GUARD);
        assertEquals(Team.GUARD, r.getAgentTeam());
    }

    @Test void rayWithIntruder()
    {
        Vector start = new Vector();
        Vector end = new Vector(10,10);
        Ray r = new Ray(start, end, Team.INTRUDER);
        assertEquals(Team.INTRUDER, r.getAgentTeam());
    }

    @Test void rayWithNull()
    {
        Vector start = new Vector();
        Vector end = new Vector(10,10);
        Ray r = new Ray(start, end);
        assertNull(r.getAgentTeam());
    }

    @Test void seeIntruder()
    {
        ArrayList<Agent> agents = new ArrayList<>();
        agents.add(agent1);
        agents.add(agent2);

        Map map = new Map(agents, new ArrayList<>());

        // Compute rays
        agent1.updateView(ge.compute(map, agent1));
        for(Ray r : agent1.getView())
        {
            if(r.getAgentTeam() != null)
            {
                assertEquals(Team.INTRUDER, r.getAgentTeam());
            }
        }
    }

    @Test void seeGuard()
    {
        ArrayList<Agent> agents = new ArrayList<>();
        agents.add(agent1);
        agents.add(agent2);

        Map map = new Map(agents, new ArrayList<>());

        // Compute rays
        agent2.updateView(ge.compute(map, agent2));
        for(Ray r : agent2.getView())
        {
            if(r.getAgentTeam() != null)
            {
                assertEquals(Team.GUARD, r.getAgentTeam());
            }
        }
    }

    @Test void seeNoAgents()
    {
        Map map = new Map(agent1, new ArrayList<>());

        // Compute rays
        agent1.updateView(ge.compute(map, agent1));
        for(Ray r : agent1.getView())
        {
            assertNull(r.getAgentTeam());
        }
    }
}