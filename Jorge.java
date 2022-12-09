package teamsandman;

import robocode.*;

import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

import java.awt.*;
import java.io.IOException;

public class Jorge extends TeamRobot {
    boolean visionOfAll;
    double maxPossibleMoves;

    public void run() {
        RobotColors robotColor = new RobotColors();

        this._coloringRobot(robotColor);
        this._coloringFridnelyRobot(robotColor);

        this._adjustingPositions();

        while (true) {
            this._searchingForEnemies();
        }
    }

    public void onScannedRobot(ScannedRobotEvent friendlyRrobot) {
        if (isTeammate(friendlyRrobot.getName())) {
            return;
        }

        double enemyAttack = this.getHeading() + friendlyRrobot.getBearing();

        double enemyPositionX = getX() + friendlyRrobot.getDistance() * Math.sin(Math.toRadians(enemyAttack));
        double enemyPositionY = getY() + friendlyRrobot.getDistance() * Math.cos(Math.toRadians(enemyAttack));

        try {
            broadcastMessage(new Point(enemyPositionX, enemyPositionY));
        } catch (IOException ex) {
            out.println("Unable to send order: ");
            ex.printStackTrace(out);
        }

        if (visionOfAll) {
            scan();
        }
    }

    private RobotColors _coloringRobot(RobotColors rc) {
        rc.bodyColor = Color.black;
        rc.gunColor = Color.black;
        rc.radarColor = Color.black;
        rc.scanColor = Color.yellow;
        rc.bulletColor = Color.white;

        return rc;
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
