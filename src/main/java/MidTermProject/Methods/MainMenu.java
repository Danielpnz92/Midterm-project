package MidTermProject.Methods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static MidTermProject.Methods.Colors.*;

@Component
public class MainMenu {
    @Autowired
    Utils utils;

    /**
     * @Description Main menu input method. Retrieves user input and calls the corresponding method.
     * @param keyboardInput Contains methods for checking user input
     */
    public void introduceOption(KeyboardInput keyboardInput) {
        System.out.println();
        System.out.println(ANSI_YELLOW + "<----WELCOME TO IRONBANK!---->" + ANSI_RESET);

        while (true) {

            utils.logIn();



//            System.out.println("4. Search book by author");
//            System.out.println("5. List all books along with author");
//            System.out.println("6. Issue book to student");
//            System.out.println("7. List books by usn");
//            System.out.println("8. List books to be returned today" + ANSI_RESET);
//            System.out.println(ANSI_RED + "9. Exit" + ANSI_RESET);
//
//            System.out.println();
//            String promptMsg = "Please, introduce an option from the main menu: ";
//            String errorMsg = "Invalid option, please try again: ";
//            int chosenOption = keyboardInput.readInteger(promptMsg, errorMsg, 1, 9);
//
//            try {
//                switch (chosenOption) {
//                    case 1 -> methods.addBook(keyboardInput);
//                    case 2 -> methods.searchBookByTitle(keyboardInput);
//                    case 3 -> methods.searchBookByCategory(keyboardInput);
//                    case 4 -> methods.searchBookByAuthor(keyboardInput);
//                    case 5 -> methods.listAllBooksWithAuthor();
//                    case 6 -> methods.issueBook(keyboardInput);
//                    case 7 -> methods.listAllBooksByUsn(keyboardInput);
//                    case 8 -> methods.listAllBooksToBeReturnedToday();
//                    default -> System.exit(0);
//                }
//            } catch (IllegalStateException iae) {
//                System.out.println();
//                System.out.println(ANSI_RED + iae.getMessage() + ANSI_RESET);
//                System.out.println();
//            }
        }
    }


}
