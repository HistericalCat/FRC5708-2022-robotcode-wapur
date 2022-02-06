package frc.robot;
import edu.wpi.first.wpilibj.XboxController;

public class Control {

    public static enum POV {
        ShooterPOV,
        IntakePOV
    };

    private static XboxController controller = new XboxController(0);
    private static POV pov = POV.ShooterPOV;

    public static XboxController getXboxCtrl() {
        return Control.controller;
    }

    public static void togglePov() {
        Control.pov = (Control.pov == POV.ShooterPOV) ? POV.IntakePOV : POV.ShooterPOV;
        System.out.println("POV Switeched To " + (Control.pov == POV.ShooterPOV ? "Shooter" : "Intake"));
    }

    public static POV getPOV() {
        return Control.pov;
    }
}
