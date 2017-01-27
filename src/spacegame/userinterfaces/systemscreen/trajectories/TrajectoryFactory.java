package spacegame.userinterfaces.systemscreen.trajectories;

import java.util.logging.Logger;

/**
 * Created by user on 2016-12-18.
 */
public final class TrajectoryFactory {

    private static final Logger LOG = Logger.getLogger(TrajectoryFactory.class.getName());

    private TrajectoryFactory(){
    }

    public static Trajectory createTrajectory(String splittableArguments){
        String[] parts = splittableArguments.split(",");
        Trajectory traj = null;
        switch (parts[0]){
            case "orbit":
                traj = new OrbitingTrajectory(parts);
                break;
            case "fixed":
                traj = new FixedPointTrajectory(parts);
                break;
            default:
                LOG.severe(parts[0] + " is not treated!!! full line :" + splittableArguments);
        }
        return traj;
    }
}
