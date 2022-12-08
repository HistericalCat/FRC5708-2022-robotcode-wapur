package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Control;

public class ControlScheme{
    public ControlScheme(){

    }

    public boolean getCreepButton(){
        return Control.getXboxCtrl().getYButton();
    }
    public boolean clawToggle() {
        return Control.getXboxCtrl().getAButton();
    }
    public double clawOverride() {
        int POV = Control.getXboxCtrl().getPOV();
        if (POV>=0){
            if(POV>45 || POV<135){
                return 1;
            }
            if(POV<315 || POV>225){
                return -1;
            }
        }
        return 0;
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
        //button right of the xbox button
        return Control.getXboxCtrl().getStartButtonPressed();
    }
    public boolean overrideActToggle(){
        //button left of the xbox button
        return Control.getXboxCtrl().getBackButtonPressed();
    }


}