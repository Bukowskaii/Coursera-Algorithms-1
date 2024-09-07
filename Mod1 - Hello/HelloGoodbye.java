/* *****************************************************************************
 *  Name:              Tom Bernens
 *  Coursera User ID:  https://www.coursera.org/learner/tombernens
 *  Last modified:     Aug 22 2024
 **************************************************************************** */

public class HelloGoodbye {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("2 Args Required");
        }
        else {
            System.out.println("Hello " + args[0] + " and " + args[1] + ".");
            System.out.println("Goodbye " + args[1] + " and " + args[0] + ".");
        }
    }
}
