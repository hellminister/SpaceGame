package spacegame.userinterfaces.systemscreen;

import javafx.animation.AnimationTimer;
import spacegame.world.GameWorld;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by user on 2016-12-22.
 */
class SceneAnimator extends AnimationTimer {
    private static final Logger LOG = Logger.getLogger(SceneAnimator.class.getName());

    private static final int NANOSECONDS_IN_A_SECOND = 1000000000;

    private long last;
    private int nbFrame;

    private boolean started;

    SceneAnimator() {
    }

    @Override
    public void handle(long now) {
        nbFrame++;
        long secs = now - last;
        GameWorld.accessGameWorld().getCurrentStarDate().addSeconds(1);
        if (secs > NANOSECONDS_IN_A_SECOND) {
            LOG.info(GameWorld.accessGameWorld().getCurrentStarDate().toString());
            last = now;
            LOG.log(Level.INFO, "{0} secs fps : {1}", new Object[]{(double) secs / NANOSECONDS_IN_A_SECOND, nbFrame});
            nbFrame = 0;
        }
    }

    @Override
    public void start() {
        super.start();
        started = true;
    }

    @Override
    public void stop() {
        super.stop();
        started = false;
    }

    void toggle() {
        if (started) {
            stop();
        } else {
            start();
        }
    }

    boolean isStarted() {
        return started;
    }

}
