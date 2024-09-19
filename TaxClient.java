/* Name: Nabeel Majid(C3287060)
 * Seng4500-Assignment1
 * Date 22/08/23
description: Client that connects with server and has to perform some function e.g Tax for connection.
*/
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;



public class TaxClient {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String host = "127.0.0.1"; //default host address
        int port = 3000; //  default port number
        System.out.println("CLIENT: host: "+ host + " port: "+ port);
        System.out.print(" Do you want to override host and port ? press(Y/N) : ");
        String overide = scanner.nextLine();
        if (overide.equalsIgnoreCase("y")) {
            System.out.println(" Please enter host address:");
            host = scanner.nextLine();
            System.out.print("Please enter port:");
            port = Integer.parseInt(scanner.nextLine());
        }

        String message =""; //Server response
        System.out.print("CLIENT:");
        String str = scanner.nextLine() + "\n";


        while (!str.equalsIgnoreCase("TAX" + "\n")) {
            System.out.println("Wrong Input: please enter Tax for connection");
            System.out.println("To quit enter Exit");
            str = scanner.nextLine() + "\n";

            if (str.equalsIgnoreCase("EXIT" + "\n")) {
                System.exit(0);

                scanner.close();

            }
        }

        try (

                Socket socket = new Socket(host, port);
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // converting into Ascii
            out.println(to_Ascii(str));


            while ((message = convertToString(br.readLine())) != null) {

                System.out.format("SERVER: %s\n", message);



                if (message.equalsIgnoreCase("TAX: OK" + "\n")) {


                    System.out.print("CLIENT:");

                    String in_String = scanner.nextLine() + "\n";

                    // for Update command
                    if (in_String.equalsIgnoreCase("UPDATE" + "\n")) {
                        String start = scanner.nextLine() + "\n";
                        String end = scanner.nextLine() + "\n";
                        String base = scanner.nextLine() + "\n";
                        String tax = scanner.nextLine() + "\n";

                        // converting into Ascii
                        out.println(to_Ascii(in_String + start + end + base + tax));

                    } else{
                        out.println(to_Ascii(in_String));
                    }



                }


                else if (message.equalsIgnoreCase("UPDATE: OK" + "\n")) {


                    System.out.print("CLIENT:");
                    String in_String = scanner.nextLine() + "\n";

                    if (in_String.equalsIgnoreCase("UPDATE" + "\n")) {
                        String start = scanner.nextLine() + "\n";
                        String end = scanner.nextLine() + "\n";
                        String base = scanner.nextLine() + "\n";
                        String tax = scanner.nextLine() + "\n";
                        out.println(to_Ascii(in_String + start + end + base + tax));

                    } else {
                        out.println(to_Ascii(in_String));
                    }
                }

                else if (message.equalsIgnoreCase("QUERY: OK" + "\n")) {

                    System.out.print("CLIENT:");
                    // Client enter next operation command
                    String inputString = scanner.nextLine() + "\n";
                    if (inputString.equalsIgnoreCase("UPDATE" + "\n")) {
                        String start = scanner.nextLine() + "\n";
                        String end = scanner.nextLine() + "\n";
                        String base = scanner.nextLine() + "\n";
                        String tax = scanner.nextLine() + "\n";
                        // convert into ACSII and send to server
                        out.println(to_Ascii(inputString + start + end + base + tax));

                    } else {
                        out.println(to_Ascii(inputString)); // convert into ACSII and send to server
                    }
                }

                else if (message.equalsIgnoreCase("BYE: OK" + "\n")) {

                    // client close all the connection
                    scanner.close();
                    socket.close();
                    br.close();
                    out.close();
                    System.out.println("TaxClient closedown.");

                    // exit the application
                    System.exit(0);

                }

                else if (message.equalsIgnoreCase("END:OK")) {

                    // client close all the connection
                    scanner.close();
                    socket.close();
                    br.close();
                    out.close();
                    System.out.println("Closed.");

                    // exit the application
                    System.exit(0);

                }


                else {

                    // Client next command
                    System.out.print("CLIENT:");
                    String inString = scanner.nextLine() + "\n";
                    if (inString.equalsIgnoreCase("UPDATE" + "\n")) {
                        String start = scanner.nextLine() + "\n";
                        String end = scanner.nextLine() + "\n";
                        String base = scanner.nextLine() + "\n";
                        String tax = scanner.nextLine() + "\n";
                        out.println(to_Ascii(inString + start + end + base + tax));// converting into ACSII


                    } else{
                        out.println(to_Ascii(inString));


                    }


                }

            }



        } catch (Exception e) { //better exception handling
            e.printStackTrace();

        }

    }

    //constructor
    public TaxClient()
    {

    }

    public static List to_Ascii(String convertString) {

        // convert to string
        byte[] b = convertString.toUpperCase().getBytes(StandardCharsets.US_ASCII);
        List<Integer> output = new ArrayList<>();
        for (byte a_Byte : b) {
            int ascii = (int) a_Byte; // to int
            output.add(ascii);
        }

        return output; //

    }

    public static String convertToString(String message) {
        if (message == null) {
            return null;
        }
        message = message.replace("[", "");
        message = message.replace("]", "");

        List<Integer> messageList = new ArrayList<>();
        for (String i : message.split(", ")) {
            int messageInt = Integer.parseInt(i);
            messageList.add(messageInt);
        }

        String str = "";
        for (int i : messageList) {
            str += Character.toString((char) i);
        }
        return str; // return the  string
    }

}
