package frc.robot;

import frc.robot.ControlScheme;
import java.lang.Math;

public class JohnControls extends ControlScheme {
    @Override
    public double getWinch1Power(){
        double power = Control.getXboxCtrl().getRightY();
        if(power>0.05 || power< -0.05){
            return power;
        }
        return 0;
    }

    @Override
    public double getWinch2Power(){
        double power = Control.getXboxCtrl().getLeftY();
        if(power>0.05 || power<-0.05){
            return power;
        }
        return 0; 
    }
    @Override
    public double getActPower(){
        double power = Control.getXboxCtrl().getRightTriggerAxis() - Control.getXboxCtrl().getLeftTriggerAxis();
        if(power > 0.05) {
            return Math.pow(power, (3.5));
        }
        else if (power < -0.05){
            return -1 * Math.pow(power, (3.5));
        }
        return 0;
    }



}
