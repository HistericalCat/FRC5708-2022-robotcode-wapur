package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Control;

public class ControlScheme{
    public ControlScheme(){

    }

    public boolean getCreepButton(){
        return Control.getXboxCtrl().getYButton();
    }
    public double getWinch1Power() {
        double power = Control.getXboxCtrl().getRightY();
        if(power > .05 || power < -.05){
            return power;
        }
        return 0;
    }


    public double getWinch2Power(){
        double power = Control.getXboxCtrl().getLeftY();
        if(power > .05 || power < -.05){
            return -power;
        }
        return 0;
    }
    public boolean getSwitchMode(){
        return Control.getXboxCtrl().getXButton();
    }
    public double getDriveForward(){
        return Control.getXboxCtrl().getRightTriggerAxis();
    }
    public double getDriveBackward(){
        return Control.getXboxCtrl().getLeftTriggerAxis();
    }
    public double getActPower(){
        int POV = Control.getXboxCtrl().getPOV();
        if (POV>=0){
            if(POV>270 || POV<90){
                return 0.5;
            }
            if(POV>90 && POV<270){
                return -0.5;
            }
        }
        return 0;
    }
    public boolean holdArmStill(){
        return Control.getXboxCtrl().getAButton();
    }
    public boolean overrideSwitchToggle(){
        return Control.getXboxCtrl().getStartButtonPressed();
    }
    public boolean overrideActToggle(){
        return Control.getXboxCtrl().getBackButtonPressed();
    }


}