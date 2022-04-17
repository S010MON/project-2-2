package app.model.agents.ACO;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.agents.AgentImp;
import app.model.agents.Cells.GraphCell;
import app.model.agents.MemoryGraph;
import app.model.agents.Team;
import app.model.agents.WallFollow.WfWorld;
import lombok.Getter;
import lombok.Setter;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

public class AcoAgent extends AgentImp
{
    //General

    @Getter private static MemoryGraph<GraphCell, DefaultEdge> world;
    @Getter private static int acoAgentCount;
    @Getter private static int acoMoveCount;

    @Getter private double maxPheromone = 2;
    private Random randomGenerator = new Random(1);
    private int[] cardinalAngles = {0, 90, 180, 270};

    @Getter private ArrayList<Vector> possibleMovements = new ArrayList<>();
    @Getter private Stack<Vector> visualDirectionsToExplore = new Stack<>();
    @Getter private ArrayList<Vector> pheromoneDirections = new ArrayList<Vector>();
    private double movementHeuristic = 0.75;
    private Vector movementContinuity = new Vector();
    @Getter @Setter private double visionDistance = 100.0;
    @Getter @Setter private int distance = 20;
    @Getter protected Move previousMove;
    private HashMap<Integer, Vector> shortTermMemory = new HashMap<>();

    private Vector targetDirection;
    protected double epsilon = 0.3;

    public AcoAgent(Vector position, Vector direction, double radius, Team team)
    {
        super(position, direction, radius, team);
        initializeWorld();
    }

    private void initializeWorld()
    {
        if(world == null)
        {
            world = new AcoWorld<>(distance);
        }

        pheromoneSenseDirections();
        world.add_or_adjust_Vertex(position);
        movementContinuity = direction.copy();
        targetDirection = direction.copy();
        acoAgentCount ++;
        acoMoveCount ++;
    }

    @Override
    public Move move()
    {
        //Detect pheromones and translate to directions to explore
        if(visualDirectionsToExplore.isEmpty() && possibleMovements.isEmpty())
        {
            return smellPheromones();
        }
        //Explore areas visually
        else if(!visualDirectionsToExplore.isEmpty())
        {
            return visibleExploration();
        }
        else if(moveFailed)
        {
            //Implement short term memory
        }
        else
        {
            return makeMove();
        }
    }

    @Override
    public void updateLocation(Vector endPoint)
    {
        position = endPoint;

        if(detectChangeInPosition())
        {
            successfulMovement();
        }
    }

    //Memory//
    public void shortTermMemory()
    {
        // Case 1: possibleMoves not empty -> select next move and make it
        Move moveFromMemory = selectNextPossibleMove();

        //Case 2: possibleMovesEmpty -> Remove this pheromone sensing direction (Condition to be added)
        Move evaluateSenses = revaluateSenses();

        //Case 3: None of the above has worked. Use short term memory to replace sensing & looking

    }

    public void relyOnMemory()
    {
        if(shortTermMemory.size() == pheromoneDirections.size())
        {

        }
    }

    private Move selectNextPossibleMove()
    {
        possibleMovements.remove(previousMove.getDeltaPos());
        return makeMove();
    }

    private Move revaluateSenses()
    {
        Vector previousMovement = previousMove.getDeltaPos();
        shortTermMemory.put(previousMovement.hashCode(), previousMovement);
        return new Move(position, new Vector());
    }

    //Movement//
    public void successfulMovement()
    {
        world.leaveVertex(previousMove.getEndDir(), maxPheromone);
        world.add_or_adjust_Vertex(position);

        movementContinuity = previousMove.getDeltaPos();
        possibleMovements.clear();
        shortTermMemory.clear();
    }

    private boolean detectChangeInPosition()
    {
        return (previousMove.getEndDir().equals(position));
    }
    public Move makeMove()
    {
        Vector movement;
        if(randomGenerator.nextDouble() < movementHeuristic && movementContinuityPossible())
        {
            movement = movementContinuity;
        }
        else
        {
            movement = possibleMovements.get(randomGenerator.nextInt(possibleMovements.size()));
        }

        previousMove = new Move(position, movement.scale(distance));
        return new Move(position, movement);
    }

    private boolean movementContinuityPossible()
    {
        for(Vector v: possibleMovements)
        {
            if(v.equals(movementContinuity.normalise()))
            {
                return true;
            }
        }
        return false;
    }

    // Smell //
    public Move smellPheromones()
    {
        smellPheromonesToVisualExplorationDirection();
        direction = visualDirectionsToExplore.peek();

        previousMove = new Move(position, new Vector());
        return new Move(position, new Vector());
    }

    public void smellPheromonesToVisualExplorationDirection()
    {
        ArrayList<Double> aggregatePheromones = accessAvailableCellAggregatePheromones();
        minimumPheromonesToDirections(aggregatePheromones);
        //TODO Check if visualDirections is empty, if so place memory - Should allow to cycle
    }

    public void minimumPheromonesToDirections(ArrayList<Double> pheromoneValues)
    {
        double minValue = Double.MAX_VALUE;
        visualDirectionsToExplore = new Stack<>();

        for(int i = 0; i < pheromoneValues.size(); i++)
        {
            Double pheromoneValue = pheromoneValues.get(i);
            Vector pheromoneDirection = pheromoneDirections.get(i);
            boolean movementInMemory = shortTermMemory.get(pheromoneDirection.hashCode()) != null;

            if(!movementInMemory && pheromoneValue == minValue)
            {
                visualDirectionsToExplore.add(pheromoneDirection);
            }
            else if(!movementInMemory && pheromoneValues.get(i) < minValue)
            {
                visualDirectionsToExplore.clear();
                minValue = pheromoneValues.get(i);
                visualDirectionsToExplore.add(pheromoneDirection);
            }
        }
    }

    public ArrayList<Double> accessAvailableCellAggregatePheromones()
    {
        ArrayList<Double> cellPheromoneValues = new ArrayList<>();

        for(Vector movement : pheromoneDirections)
        {
            double aggregatePheromone = world.aggregateCardinalPheromones(position, movement);
            cellPheromoneValues.add(aggregatePheromone);
        }
        return cellPheromoneValues;
    }


    // Vision //
    /*
    1. Agent accesses smells in 360 degrees
    2. Determines directions of lowest pheromones
    3. Determines if directions are viable through vision ~ (Randomly) (If not viable ...)
    4. Moves in that direction
     */

    public Move visibleExploration()
    {
        explorationToViableMovement();
        nextExplorationVisionDirection();

        previousMove = new Move(position, new Vector());
        return new Move(position, new Vector());
    }

    public void explorationToViableMovement()
    {
        Ray cardinalRay = detectCardinalPoint(direction.getAngle());
        Vector currentDirection = visualDirectionsToExplore.pop();
        if(moveEvaluation(cardinalRay))
        {
            possibleMovements.add(direction);
        }
    }

    public void nextExplorationVisionDirection()
    {
        if(!visualDirectionsToExplore.empty())
        {
            Vector directionToExplore = visualDirectionsToExplore.peek();
            direction = directionToExplore.normalise();
        }
    }

    public boolean moveEvaluation(Ray cardinalRay)
    {
        double rayLength = cardinalRay.rayLength();
        return(rayLength > visionDistance + epsilon);
    }

    public Ray detectCardinalPoint(double targetCardinalAngle)
    {
        for(Ray ray: view)
        {
            if(approximateAngleRange(ray.angle(), targetCardinalAngle))
            {
                return ray;
            }
        }
        throw new RuntimeException("Cardinal point not found");
    }

    public boolean approximateAngleRange(double detectedAngle, double targetAngle)
    {
        return detectedAngle < (targetAngle + epsilon) && detectedAngle > (targetAngle - epsilon);
    }


    //World (SWARM memory)
    public void acceptWorld(WfWorld wfWorld)
    {
        world = wfWorld;
    }

    //Setup
    public void pheromoneSenseDirections()
    {
        for(int cardinalAngle: cardinalAngles)
        {
            pheromoneDirections.add(angleToGraphMovementLink(cardinalAngle));
        }
    }

    public Vector angleToGraphMovementLink(int angle)
    {
        return switch (angle)
                {
                    case 0 -> new Vector(0, distance);
                    case 90 -> new Vector(distance, 0);
                    case 180 -> new Vector(0, -distance);
                    case 270 -> new Vector(-distance, 0);
                    default -> null;
                };
    }
}
