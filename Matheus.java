package teamsandman;

import robocode.*;

import robocode.Droid;
import robocode.MessageEvent;
import robocode.TeamRobot;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Matheus extends TeamRobot implements Droid {
    boolean visionOfAll;
    double maxPossibleMoves;

    public void run() {
        this._adjustingPositions();

        while (true) {
            this._searchingForEnemies();
        }
    }

    public void onMessageReceived(MessageEvent friendlyRrobot) {
        if (friendlyRrobot.getMessage() instanceof RobotColors) {
            this._coloringFridnelyRobot(friendlyRrobot);
        }

        if (friendlyRrobot.getMessage() instanceof Point) {
            Point point = (Point) friendlyRrobot.getMessage();

            double directionX = point.getX() - this.getX();
            double directionY = point.getY() - this.getY();

            double theta = Math.toDegrees(Math.atan2(directionX, directionY));

            turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));

            fire(3);
        }
    }

    private void _coloringFriendlyRobot(RobotColors friendlyRrobot) {
        RobotColors rc = (RobotColors) friendlyRrobot.getMessage();

        setBodyColor(rc.bodyColor);
        setGunColor(rc.gunColor);
        setRadarColor(rc.radarColor);
        setScanColor(rc.scanColor);
        setBulletColor(rc.bulletColor);
    }

    private void _adjustingPositions() {
        maxPossibleMoves = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
        visionOfAll = false;

        turnLeft(getHeading() % 90);
        ahead(maxPossibleMoves);

        visionOfAll = true;
        turnGunRight(90);
        turnRight(90);
    }

    private void _searchingForEnemies() {
        visionOfAll = true;

        ahead(maxPossibleMoves);
        visionOfAll = false;

        turnRight(90);
    }
}
