/* Name: Nabeel Majid(C3287060)
 * Seng4500-Assignment1
 * Date 22/08/23
description: Server that generate linkedlist to store taxscale data and connected to Client to for calculate tax.
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.net.ServerSocket;


public class TaxServer {

    static TaxServer t_server; // initialising  Server
    private LinkedList<TaxScale> linkedlist; //linkedlist to store data till 12 scale

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in); //
        t_server = new TaxServer();
        int port = 3000;

        System.out.println("SERVER: current port: " + port);
        System.out.print(" Do you want to override the port ? (Y/N)= ");
        String overide= scanner.nextLine();

        String respond = "";

        if (overide.equalsIgnoreCase("Y")) {

            System.out.print("port:");
            port = Integer.parseInt(scanner.nextLine());
        }

        while (true) {

            try (
//creating server and establishing connection with client
                    ServerSocket serversocket = new ServerSocket(port);
                    Socket socket = serversocket.accept();
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);) {
                System.out.println("CONNECTION OPEN");
                String message = "";

                //converting ascii to string
                while ((message = asciiToString(br.readLine())) != null) {

                    System.out.format("CLIENT: %s\n", message);


                    if (message.contains("TAX" + "\n"))
                    {

                        System.out.print("SERVER:" + "TAX: OK" + "\n");
                        out.println(toAscii("TAX: OK" + "\n"));
                    }

                    else if (message.contains("UPDATE" + "\n")) {
                        // check tax linkedlist is bigger than 12
                        if (t_server.linkedlist.get_Size() >= 12) {
                            respond = "UPDATE: Fail list can only store upto 12 tax scale";
                            System.out.print("SERVER:" + respond + "\n");

                            // convert into ACSII and send to server
                            out.println(toAscii("UPDATE" + respond + "\n"));

                        } else {

                            String[] str = message.split("\n");

                            if ( str[2].isEmpty()) {
                                str[2] = "-1";
                            }
                            // storing values of tax
                            TaxScale scale = new TaxScale(
                                    Integer.parseInt(str[1]), Integer.parseInt(str[2]),
                                    Integer.parseInt(str[3]), Integer.parseInt(str[4]));

                            // insert the TaxScale inorder
                            t_server.linkedlist.insertInOrder(scale);

                            // send UPDATE: OK respond back to client
                            respond = "UPDATE: OK";
                            // print Server respond message in console
                            System.out.print("SERVER:" + respond + "\n");

                            // convert into ACSII and send to server
                            out.println(toAscii(respond + "\n"));

                        }

                    }

                    else if (message.contains("QUERY" + "\n")) {

                        Iterator<TaxScale> iter = t_server.linkedlist.iterator();
                        String str = "";
                        while (iter.hasNext()) {

                            TaxScale temp = iter.next();
                            String emptyString = temp.get_endIncome().toString();
                            if (temp.get_endIncome().equals(-1)) {
                                emptyString = "~";
                            }

                            str += temp.get_startIncome() + "  " + emptyString + "  " + temp.get_baseTax()
                                    + "  " + temp.get_TaxPD() + "  " + "\n";

                        }

                        // send QUERY: OK respond back to client
                        respond = str + "QUERY: OK";
                        // print Server respond message in console
                        System.out.print("SERVER:" + respond + "\n");

                        // convert into ACSII and send to server
                        out.println(toAscii(respond + "\n"));

                    }

                    else if (message.contains("BYE" + "\n")) {
                        // send respond message: BYE: OK to Client

                        respond = "BYE: OK";
                        // print Server respond message in console
                        System.out.print("SERVER:" + respond + "\n");

                        // convert into ACSII and send to server
                        out.println(toAscii(respond + "\n"));

                        break;

                    }

                    else if (message.contains("END" + "\n"))
                    {

                        respond = "END:OK";
                        System.out.print("SERVER:" + respond + "\n");
                        out.println(toAscii(respond + "\n"));

                        break;

                    }


                    else {

                        //for valid message
                        if(validMessage(message))
                        {
                            message = message.replace("\n", "");
                            respond= calculate(Integer.parseInt(message));
                            System.out.print("SERVER:" + respond + "\n");
                            out.println(toAscii(respond + "\n"));



                        }
                        else {

                         // if wrong input is entered by client
                            respond = "Wrong Input " + message;
                            System.out.print("SERVER:" + respond + "\n");
                            // convert into ACSII and send to server
                            out.println(toAscii(respond + "\n"));


                        }


                    }



                }



                // closing all connections
                if (respond.contains("END:OK")) {
                    scanner.close();
                    serversocket.close();
                    socket.close();
                    br.close();
                    out.close();
                    System.out.println("Connection closed.");
                    System.exit(0);
                }

            } catch (IOException e) { // You should have some better exception handling
                e.printStackTrace();

            }

        }

    }


    //Tax server constructor
    public TaxServer() {
        this.linkedlist = new LinkedList<TaxScale>();
        TaxScale ts = new TaxScale(0, 0, 0, 0);
        this.linkedlist.Insert(ts);
    }

    // method to calculate tax
    public static String calculate(int Income){

        String sum = "";

        double tax = 0;

        boolean scale =false;


        Iterator<TaxScale> iter = t_server.linkedlist.iterator();

        while (iter.hasNext()) {

            TaxScale temp = iter.next();
            if((temp.get_startIncome() <= Income)&&(temp.get_endIncome() >= Income)){


                float cent = (float) (temp.get_TaxPD() * 0.01);
                tax = temp.get_baseTax() + (cent * (Income-temp.get_startIncome()));

                scale = true;

                break;
            }

        }

        if(scale){

            sum = "TAX IS " + String.format("%.2f", tax);

        }else {

            sum = "Cannot define Tax on "+ Income +" because it does not fall on tax scale";
        }

        return sum;
    }


    public static Boolean validMessage (String str){

        Boolean flag = true;
        for (int i = 0; i < str.length(); i++) {
            if (i == 0 && str.charAt(i) == '-')
                continue;
            if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != 10)
                flag = false;
        }


        return flag;

    }

    // method for converting string to ascii and integer
    public static List toAscii(String convertString) {


        byte[] bytes = convertString.toUpperCase().getBytes(StandardCharsets.US_ASCII);
        List<Integer> result = new ArrayList<>();
        for (byte aByte : bytes) {
            int ascii = (int) aByte; //t
            result.add(ascii);
        }

        return result;

    }
    //method to covert
    public static String asciiToString(String message) {


        message = message.replace("[", "");
        message = message.replace("]", "");

        // converting Integer
        List<Integer> List = new ArrayList<>();
        for (String i : message.split(", ")) {

            int messageInt = Integer.parseInt(i);
            List.add(messageInt);
        }

        String str = "";

        for (int i : List) {

            str += Character.toString((char) i);

        }

        return str;
    }

}

