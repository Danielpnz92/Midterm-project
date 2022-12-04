package MidTermProject.Methods;

import static MidTermProject.Methods.Colors.ANSI_CYAN;

public class Utils {

    public void logIn() {
        KeyboardInput k = new KeyboardInput();
        System.out.println(ANSI_CYAN + "Please log in the application:");
        System.out.print("Username: ");
        System.out.println();
        System.out.print("Password");
    }
}
