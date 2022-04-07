package frc.robot.commands;

import frc.robot.Control;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Sensors;
import frc.robot.subsystems.Climber;

import frc.robot.ControlScheme;

import javax.sound.midi.SysexMessage;
import javax.swing.Action;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveWithJoystick {
    public static ControlScheme scheme = new ControlScheme();

    //moved switch enabled toggle to the climber class

    public static class DoDrivetrain extends CommandBase {
        private final Drivetrain drivetrain;
        
        public DoDrivetrain(Drivetrain d){
            System.out.println("Do Drivetrain Constructed...");
            drivetrain = d;
            addRequirements(drivetrain);
        }
        
        @Override
        public void execute() {
            
            double turn = 0;
	        double power = 0;
            double creepRate = 0.3;

	        turn = -Control.getXboxCtrl().getLeftX();
	        power = scheme.getDriveForward() - scheme.getDriveBackward();
            
	        turn = inputTransform(turn, 0, 0.1);
	        power = inputTransform(power, 0.15, 0.03);
            
            if(scheme.getCreepButton()){ //If we're in creep mode
                turn *= creepRate;
                power *= creepRate;
            }
            if(Control.getPOV()== Control.POV.IntakePOV){
                power = -power; //Switch forwards and backwards.
            }
            power *= .7; //Intentionally limit ourselves.
            turn *= .5; // also limits turn power

            drivetrain.DrivePolar(power, turn);
            
        }

        @Override
        public void end(boolean interupted){
            drivetrain.Drive(0, 0);
        }

    }

    public static class DoClimber extends CommandBase {
        private final Climber climber;
        //private Sensors sensor = new Sensors();
        public DoClimber(Climber c){
            climber = c;
            addRequirements(climber);
            
        }

        enum Direction {
            up,
            down,
            none
        }

        private Boolean winchSwitchesEnabled = true;
        private Boolean actSwitchesEnabled = true;
        
        private Direction winch1MovingDir = Direction.none;
        private Direction winch1BlockedDir = Direction.none;
        private Boolean winch1Overshot = false;

        private Direction winch2HitGoing = Direction.none;

        private static long winch1LimitDelay = 1000;

        private long winch2LimitTimer = 0;
        private static long winch2LimitDelay = 1000;
        
        @Override
        public void execute(){
            if(scheme.overrideSwitchToggle()) {
                //I know it's weird to have 2 variables for this but it gives us flexibility in the future
                winchSwitchesEnabled = !winchSwitchesEnabled;
                //actSwitchesEnabled = !actSwitchesEnabled;
            }
            if(scheme.overrideActToggle()) {
                actSwitchesEnabled = !actSwitchesEnabled;
            }
            if(!winchSwitchesEnabled) {
                System.out.println("SWITCHES OVERRIDDEN");
            }
            if(!actSwitchesEnabled) {
                System.out.println("ACTUATORS OVERRIDDEN");
            }

            /*if(climber.actuatorOut.get()){
                System.out.println("Maximum extension");
            } else if(climber.actuatorIn.get()){
                System.out.println("Minimum extension");
            }
            */
            float actPower = (float) scheme.getActPower();
            if(actSwitchesEnabled){
                //TODO: Check direction of EVERYTHING here. I had to inverse it all for it to work -j
                //System.out.printf("act out is: %b act in is: %b\n", !climber.actuatorOut.get(), !climber.actuatorIn.get());
                if(!climber.actuatorOut.get() && actPower < 0.0){
                    actPower = 0.0f;
                } else if(!climber.actuatorIn.get() && actPower > 0.0){
                    actPower = 0.0f;
                }
            }
            //System.out.println(power);
            //reduce actuator power to 90%
            actPower *= 0.90;

            if(scheme.getCreepButton()) {
                actPower *= 0.5;
            }

            climber.driveActuator(actPower);
            
            float winch1Power = (float)scheme.getWinch1Power();
            
            if(winchSwitchesEnabled) {
                if(!climber.winchLimit1.get()) {
                    System.out.print("switch ");
                    if (winch1BlockedDir == Direction.none)
                    {
                        winch1BlockedDir = winch1MovingDir;
                        System.out.println("Blocked moving " + winch1BlockedDir);
                    }
                    winch1Overshot = false;
                } else {
                    if (winch1BlockedDir != Direction.none && winch1BlockedDir == winch1MovingDir)
                    {
                        winch1Overshot = true;
                        System.out.println("overshot");
                    }
                    else if (!winch1Overshot)
                    {
                        winch1BlockedDir = Direction.none;
                        System.out.println("Unblocked");
                    }
                }

                if(winch1BlockedDir == Direction.up && winch1Power > 0.05)
                {
                    winch1Power = 0;
                    System.out.println("prevent going up");
                }
                if(winch1BlockedDir == Direction.down && winch1Power < -0.05)
                {
                    winch1Power = 0;
                    System.out.println("prevent going down");
                }
            
                //reduce winch 1 power to 90%
                winch1Power *= 0.9;

                System.out.printf("winch1Power %f\n", winch1Power);
                if(winch1Power < -0.05)
                {
                    winch1MovingDir = Direction.down;
                    System.out.println("Dir is down");
                }
                if(winch1Power > 0.05)
                {
                    winch1MovingDir = Direction.up;
                    System.out.println("Dir is up");
                }
            }

            climber.driveWinch1(winch1Power);
            
            
            float winch2Power = (float)scheme.getWinch2Power();
            if (winchSwitchesEnabled) {
                //disables the retraction if within the delay period
                if(winch2LimitTimer + winch2LimitDelay  > System.currentTimeMillis() && System.currentTimeMillis() > winch2LimitDelay) {
                    System.out.println("Activated w2 timer");
                    if(winch2Power < 0.1) {
                        winch2Power = (float) -0.1;
                        System.out.println("Winch 2 is stopped");
                    }
                }
                //sets the timer once when the switch is pressed
                else{
                    if(!climber.winchLimit2.get()) {
                        winch2LimitTimer = System.currentTimeMillis();
                    }
                }
                //slows the retraction for a little bit after the disable ends
                if(winch2LimitTimer + winch2LimitDelay * 1.5  > System.currentTimeMillis()) {
                    if(winch2Power < 0) {
                        winch2Power *= 0.5;
                        System.out.println("Winch 2 is slowed");
                    }
                }
                
            }

                /*
                if(climber.winchLimit2.get()){
                    if(winch2HitGoing == Direction.none) {
                        if(winch2Power > 0) winch2HitGoing = Direction.up;
                        if(winch2Power < 0) winch2HitGoing = Direction.down;
                    }
                } else {
                    winch2HitGoing= Direction.none;
                }

                if(winch2HitGoing == Direction.up && winch2Power > 0){
                    winch2Power = 0;
                } else if(winch2HitGoing == Direction.down && winch2Power < 0){
                    winch2Power = 0;
                }
            }
            */
            //reduce winch 2 power to 90%
            winch2Power *= 0.9;

            climber.driveWinch2(winch2Power);
            
            

        }
    }

    public static double inputTransform(
        double input,
        double minPowerOutput,
        double inputDeadZone,
        double inputChangePosition,
        double outputChangePosition
    ) {
        double output = 0;
        double correctedInput = (Math.abs(input) - inputDeadZone) / (1 - inputDeadZone);
        
        if (correctedInput <= 0){
            return 0;
        } else if (correctedInput <= inputChangePosition) {
            output = (correctedInput / inputChangePosition * (outputChangePosition - minPowerOutput)) + minPowerOutput;
        } else {
            output = (correctedInput - inputChangePosition)
                    / (1 - inputChangePosition)
                    * (1 - outputChangePosition)
                    + outputChangePosition;
        }

        return (input < 0) ? (output * -1.0) : output;
    }

    public static double inputTransform(
        double input,
        double minPowerOutput,
        double inputDeadZone
    ) {
        return DriveWithJoystick.inputTransform(
            input, 
            minPowerOutput, 
            inputDeadZone, 
            .75, 
            .5
        );
    }

    public static double powerRampup(
        double input,
        double outputInit
    ) {
        
        if ((Math.abs(input) < Math.abs(outputInit)) && ((input < 0 && outputInit < 0 ) || (input > 0 && outputInit > 0))){
            return input;
        } 

        return outputInit + (0.1 * ((input > 0) ? 1.0 : -1.0));

        //int sign = (input > 0) ? 1 : -1;
        //*outputVar += 0.1*sign;
    }

}
