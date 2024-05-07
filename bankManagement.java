package banking;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;

public class bankManagement {
    private static final int NULL = 0;
    static Connection con = connection.getConnection();
    static String sql = "";

    public static boolean createAccount(String name, int passcode) {
        try {
            if (name == "" || passcode == NULL) {
                System.out.println("ERR: All Fields Are Required! ");
                return false;
            }
            int bal = 1000;
            sql = "insert into customer(cname,balance,pass_code)  values(?,?,?) ";

            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, name);
            st.setInt(2, bal);
            st.setInt(3, passcode);

            if (st.executeUpdate() == 1) {
                System.out.println(name + ", Now you Login! ");
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean
    loginAccount(String name, int passcode) // login method
    {
        try {
            // validation
            if (name == "" || passcode == NULL) {
                System.out.println("ERR: All Field Required!");
                return false;
            }
            // query
            sql = "select * from customer where cname= ?  and pass_code= ?  ";

            PreparedStatement st
                    = con.prepareStatement(sql);
            st.setString(1, name);
            st.setInt(2, passcode);
            ResultSet rs = st.executeQuery();
            // Execution
            BufferedReader sc = new BufferedReader(
                    new InputStreamReader(System.in));

            if (rs.next()) {
                // after login menu driven interface method

                int ch = 5;
                int amt = 0;
                int senderAc = rs.getInt("ac_no");
                ;
                int receiveAc;
                while (true) {
                    try {
                        System.out.println(
                                "\n\nHello, "
                                        + rs.getString("cname"));
                        System.out.println(
                                "1)Transfer Money");
                        System.out.println("2)View Balance\n3)Deposit Amount\n4)Withdraw Amount\n5)Change Password");
                        System.out.println("6)Log-Out");

                        System.out.print("Enter Choice:");
                        ch = Integer.parseInt(
                                sc.readLine());
                        if (ch == 1) {
                            System.out.print(
                                    "Enter Receiver A/c No:");
                            receiveAc = Integer.parseInt(
                                    sc.readLine());
                            System.out.print(
                                    "Enter Amount:");
                            amt = Integer.parseInt(
                                    sc.readLine());

                            if (bankManagement
                                    .transferMoney(
                                            senderAc, receiveAc,
                                            amt)) {
                                System.out.println(
                                        "MSG : Money Sent Successfully!\n");

                            } else {
                                System.out.println(
                                        "ERR : Failed!\n");
                            }
                        } else if (ch == 2) {

                            bankManagement.getBalance(
                                    senderAc);
                        } else if (ch == 3) {
                            System.out.println("Enter Amount to be deposite:");
                            int amount = Integer.parseInt(
                                    sc.readLine());
                            if(bankManagement.deposite(amount, senderAc)){

                                System.out.println("MSG: Money deposited successfully\n");
                                bankManagement.getBalance(
                                        senderAc);
                            }else {

                                System.out.println("ERR: Transaction Failed(deposite money)\n");
                            }

                        } else if (ch == 4) {
                            System.out.println("Enter Amount to be Withdrawn:");
                            int money = Integer.parseInt(
                                    sc.readLine());
                            if(bankManagement.withdrawn(money, senderAc)){

                                System.out.println("MSG: Money withdrawn successfully\n");
                                bankManagement.getBalance(
                                        senderAc);
                            }else {

                                System.out.println("ERR: Transaction Failed (deposite money)\n");
                            }

                        } else if (ch == 5) {
                            System.out.println("Enter new password :" );
                            int pass= Integer.parseInt(sc.readLine());
                            if (bankManagement.changePassword(
                                     pass,senderAc)) {
                                System.out.println(
                                        "MSG : Password Changed  Successfully!\n");
                            }
                            else {
                                System.out.println(
                                        "ERR : Transaction Failed!\n");
                            }

                        } else if (ch == 6) {
                            break;
                        } else {
                            System.out.println(
                                    "Err : Enter Valid input!\n");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                return false;
            }
            // return
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("ERR: Username Not Available!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean changePassword(int pass ,int senderAc) {
        try {
            sql = "update customer set pass_code=? where ac_no=? ";
            PreparedStatement st = con.prepareStatement(sql);


            st.setInt(1,pass );
            st.setInt(2,senderAc );

            // execute
            st.executeUpdate();

            return true;
        }catch(Exception e){
            e.printStackTrace();

        }
        return false;
    }

    private static boolean withdrawn(int money, int senderAc) {
        try {

            sql = "select * from customer where ac_no="
                    + senderAc;
            PreparedStatement ps
                    = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                if (rs.getInt("balance") < money) {
                    System.out.println(
                            "Insufficient Balance!");
                    return false;
                }
            }

            sql = "update customer set balance=balance-"
                    + money + " where ac_no="
                    + senderAc;
            Statement st = con.createStatement();
            if (st.executeUpdate(sql) == 1) {
                System.out.println("MSG: Amount Withrawn successfully!");
            }
            //con.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean deposite(int amount, int senderAc) {
        try {

            sql = "update customer set balance=balance+"
                    + amount + " where ac_no="
                    + senderAc;
            Statement st = con.createStatement();
            if (st.executeUpdate(sql) == 1) {
                System.out.println("MSG: Amount Deposited Successfully!");
            }
            //con.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void
    getBalance(int acNo) // fetch balance method
    {
        try {

            // query
            sql = "select * from customer where ac_no="
                    + acNo;
            PreparedStatement st
                    = con.prepareStatement(sql);

            ResultSet rs = st.executeQuery(sql);
            System.out.println(
                    "-----------------------------------------------------------");
            System.out.printf("%12s %10s %10s\n",
                    "Account No", "Name",
                    "Balance");

            // Execution

            while (rs.next()) {
                System.out.printf("%12d %10s %10d.00\n",
                        rs.getInt("ac_no"),
                        rs.getString("cname"),
                        rs.getInt("balance"));
            }
            System.out.println(
                    "-----------------------------------------------------------\n");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean transferMoney(int sender_ac,
                                        int receiver_ac,
                                        int amount)
            throws SQLException // transfer money method
    {
        // validation
        if (receiver_ac == NULL || amount == NULL) {
            System.out.println("All Field Required!");
            return false;
        }
        try {
            con.setAutoCommit(false);
            sql = "select * from customer where ac_no="
                    + sender_ac;
            PreparedStatement ps
                    = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                if (rs.getInt("balance") < amount) {
                    System.out.println(
                            "Insufficient Balance!");
                    return false;
                }
            }

                Statement st = con.createStatement();

                // debit amount
                con.setSavepoint();

                sql = "update customer set balance=balance-"
                        + amount + " where ac_no=" + sender_ac;
                if (st.executeUpdate(sql) == 1) {
                    System.out.println("Amount Debited Successfully!");
                }

                // credit
                sql = "update customer set balance=balance+"
                        + amount + " where ac_no=" + receiver_ac;
                st.executeUpdate(sql);

                con.commit();
                return true;

        }
        catch (Exception e) {
            e.printStackTrace();
            con.rollback();
        }
        // return
        return false;
    }
}
