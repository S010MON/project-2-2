package testing;

import app.controller.TestingEngine;
import app.controller.io.FileManager;
import app.controller.logging.Logger;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.agents.AgentType;

public class Experiments
{
    private static final int iterations = 100;
    private static final int[] no_of_agents = {1, 2, 3, 4 ,5};

    /**
     * Enter a map name and run the Experiments file to run every agent through the map 100 times
     */
    public static void main(String[] args)
    {
        //run("experiment_map_1");
    }

    private static void run(String map_name)
    {
        String map_path = "src/main/resources/";

        System.out.println("Loading map: " + map_name);
        Settings settings = FileManager.loadSettings(map_path + map_name);
        settings.setNoOfIntruders(0);

        Logger logger = new Logger(map_name);
        logger.setOutputCsv();

        for(AgentType agent_under_test: AgentType.values())
        {
            settings.setGuardType(agent_under_test);

            for(int n: no_of_agents)
            {
                System.out.println("Agent Type: " + agent_under_test + " - " + n + " agents");

                for(int i = 0; i < iterations; i++)
                {
                    String test_name = "Iteration: " + i + "/" + iterations + " ";

                    Map map = new Map((settings));
                    TestingEngine gameEngine = new TestingEngine(map, test_name);
                    int[] data = gameEngine.run();
                    logger.log(agent_under_test.name(), data);
                }
            }
        }
    }
}