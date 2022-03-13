package frc.robot;

import frc.robot.ControlScheme;

public class DanesControls extends ControlScheme {
    @Override
    public double getWinch1Power(){
        if(Control.getXboxCtrl().getRightBumper()){
            return 0.5;
        }
        else if(Control.getXboxCtrl().getLeftBumper()){
            return -0.5;
        }
        else
        return 0;
    }
    @Override
    public double getWinch2Power(){
        double power = Control.getXboxCtrl().getRightTriggerAxis() - Control.getXboxCtrl().getLeftTriggerAxis();
        if(power > 0.05 || power < -0.05){
            return power;
        }
        return 0;
    }
}
