package banking;

import com.sun.source.tree.TryTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.ScatteringByteChannel;
import java.sql.SQLException;
import java.util.Scanner;

public class bank {
    //main class
    public static void main(String args[])
            throws IOException
    {
        Scanner sca = new Scanner(System.in);
        BufferedReader sc = new BufferedReader(
                new InputStreamReader(System.in));
        String name = "";
        int passcode;
        int ac_no;
        int ch;

        while (true) {
            System.out.println(
                    "\n ->|| Welcome to Abhishek Amble Bank ||<- \n");
            System.out.println("1)Create Account");
            System.out.println("2)Login Account");

            try {
                System.out.print("\n Enter Input:"); //user end  input
                ch = Integer.parseInt(sc.readLine());

                switch (ch) {
                    case 1:
                        try {
                            System.out.print(
                                    "Enter Unique UserName:");
                            name = sc.readLine();
                            System.out.print(
                                    "Enter New Password:");
                            passcode =
                                    Integer.parseInt(sc.readLine());

                            if (bankManagement.createAccount(
                                    name, passcode)) {
                                System.out.println(
                                        "MSG : Account Created Successfully!\n");
                            }
                            else {
                                System.out.println(
                                        "ERR : Account Creation Failed!\n");
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(
                                    " ERR : Enter Valid Data::Insertion Failed!\n");
                        }
                        break;

                    case 2:
                        try {
                            System.out.print(
                                    "Enter UserName:");
                            name = sc.readLine();
                            System.out.print(
                                    "Enter Password:");
                            passcode = sca.nextInt();


                            if (bankManagement.loginAccount(
                                    name, passcode)) {
                                System.out.println(
                                        "MSG : Login Successfull!\n");
                            }
                            else {

                                System.out.println(
                                        "ERR : login Failed!\n");
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(
                                    " ERR : Enter Valid Data::Login Failed!\n");
                        }

                        break;

                    default:
                        System.out.println("ERR: Invalid Entry!\n");
                }

                if (ch == 6) {
                    System.out.println(
                            "MSG: Loged Out  Successfully!\n\n Thank You :)");
                    break;
                }
            }
            catch (Exception e) {
                System.out.println("ERR: Enter Valid Entry!");
            }
        }
        sc.close();
    }
}
