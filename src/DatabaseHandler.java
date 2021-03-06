/**
 * Created by gismat on 14.05.2017.
 */
import java.sql.*;
import java.util.ArrayList;

import java.time.*;
import java.util.Calendar;

public class DatabaseHandler {

    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306";

    //  Database credentials
    static final String USER = "root"; // may be different on your mysql server
    static final String PASS = ""; // may be different on your mysql server
    private static Connection conn = null;

    private static void getConnection(){


        try{
            //STEP 2: Register JDBC driver
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);


            //System.out.println("Creating statement...");

            //STEP 6: Clean-up environment


        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }


    }

    public static User checkLogin(String email, String password){

        User user=null;
        Statement stmt = null;

        getConnection();


        try {
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT _id,fullname, email, password, type,totalfine FROM librarybookloan.Users WHERE email='" + email +"'";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.first()) {
                user = new User(
                        rs.getInt("_id"),
                        rs.getString("fullname"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("type"),
                        rs.getFloat("totalfine"));

                System.out.println("on db handler: "+user);
            }else{
                user=null;
                return user;
            }

            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(password.equals(user.getPassword())){

            return user;

        }else{

            user=null;
            return user;

        }

    }

    public static ArrayList<Book> searchBooks(String title){

        ArrayList<Book> books=new ArrayList<>();
        Statement stmt = null;



            getConnection();


        try {
            stmt = conn.createStatement();
            int istaken=0;
            String sql;
            sql = "SELECT _id,title,author,publication,librarylocation,status FROM librarybookloan.Books WHERE title='" + title +"' AND istaken='"+istaken+"' ";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publication"),

                        rs.getString("librarylocation"),
                        rs.getString("status")));



            }

            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return books;

    }


    public static ArrayList<Book> getWaitingList(int userId){

        ArrayList<Book> books=new ArrayList<>();
        Statement stmt = null;


        getConnection();

        try {
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT Books._id,Books.title,author,publication,librarylocation,status FROM librarybookloan.Waitings INNER JOIN librarybookloan.Books ON Waitings.bookid=Books._id WHERE userid= '" + userId +"'";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publication"),
                        rs.getString("librarylocation"),
                        rs.getString("status")));



            }

            System.out.println("books: "+books.size());

            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return books;
    }



    public static ArrayList<Book> getAllBooks(){

        ArrayList<Book> books=new ArrayList<>();
        Statement stmt = null;


            getConnection();


        try {
            stmt = conn.createStatement();
            int istaken=0;
            String sql;
            sql = "SELECT _id,title,author,publication,librarylocation,status FROM librarybookloan.Books ";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publication"),

                        rs.getString("librarylocation"),
                        rs.getString("status")));


            }


            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return books;

    }

    public static ArrayList<User> getAllUsers(){

        ArrayList<User> users=new ArrayList<>();
        Statement stmt = null;


            getConnection();


        try {
            stmt = conn.createStatement();
            int istaken=0;
            String sql;
            sql = "SELECT _id,fullname,email, password, type, totalfine FROM librarybookloan.Users ";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("_id"),
                        rs.getString("fullname"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("type"),
                        rs.getFloat("totalfine")));


            }


            for (int i = 0; i < users.size(); i++) {
                System.out.println(users.get(i).toString());
            }

            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return users;

    }


    public static Book insertBook(Book input){

        PreparedStatement statement=null;



            getConnection();

        try {

            String sql;
            sql = "INSERT INTO LIBRARYBOOKLOAN.Books(title,author,publication,librarylocation) VALUES (?,?,?,?)";

            statement = conn.prepareStatement(sql);
            statement.setString(1,input.getTitle());
            statement.setString(2,input.getAuthor());
            statement.setString(3, input.getPublication());
            statement.setString(4, input.getLocation());
            statement.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            input=null;
            e.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            input=null;

            e.printStackTrace();
        }

        return input;




    }

    public static Book updateBook(Book input){

        PreparedStatement statement=null;



        getConnection();

        try {

            String sql;
            //sql = "INSERT INTO LIBRARYBOOKLOAN.Books(title,author,publication,librarylocation) VALUES (?,?,?,?)";
            sql="UPDATE librarybookloan.Books SET title=?,author=?,publication=?,librarylocation=?,status=? WHERE `_id`=?";

            statement = conn.prepareStatement(sql);
            statement.setString(1,input.getTitle());
            statement.setString(2,input.getAuthor());
            statement.setString(3, input.getPublication());
            statement.setString(4, input.getLocation());
            statement.setString(5, input.getStatus());

            statement.setInt(6, input.getId());

            int result=statement.executeUpdate();
            System.out.println("result: "+result);
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            input=null;
            e.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            input=null;

            e.printStackTrace();
        }

        return input;




    }

    public static Book checkReturningBook(Book input){

        PreparedStatement statement=null;



        getConnection();

        try {

            String sql;
            //sql = "INSERT INTO LIBRARYBOOKLOAN.Books(title,author,publication,librarylocation) VALUES (?,?,?,?)";
            sql="UPDATE librarybookloan.Waitings SET enabled=? WHERE `bookid`=?";

            statement = conn.prepareStatement(sql);
            statement.setInt(1,1);
            statement.setInt(2,input.getId());


            int result=statement.executeUpdate();
            System.out.println("result: "+result);
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            input=null;
            e.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            input=null;

            e.printStackTrace();
        }

        return input;




    }

    public static User updateUser(User input){

        PreparedStatement statement=null;



        getConnection();

        try {

            String sql;
            //sql = "INSERT INTO LIBRARYBOOKLOAN.Books(title,author,publication,librarylocation) VALUES (?,?,?,?)";
            sql="UPDATE librarybookloan.Users SET fullname=?,email=?,password=?,type=? WHERE `_id`=?";

            statement = conn.prepareStatement(sql);
            statement.setString(1,input.getFullname());
            statement.setString(2,input.getEmail());
            statement.setString(3, input.getPassword());
            statement.setString(4, input.getType());
            statement.setInt(5, input.getId());

            int result=statement.executeUpdate();
            System.out.println("result: "+result);
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            input=null;
            e.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            input=null;

            e.printStackTrace();
        }

        return input;




    }

    public static int deleteBook(int bookId){

        PreparedStatement statement=null;


        int result=0;
        getConnection();

        try {

            String sql;
            //sql = "INSERT INTO LIBRARYBOOKLOAN.Books(title,author,publication,librarylocation) VALUES (?,?,?,?)";
            sql="DELETE FROM librarybookloan.Books  WHERE `_id`=?";

            statement = conn.prepareStatement(sql);

            statement.setInt(1,bookId);

            result=statement.executeUpdate();
            System.out.println("result: "+result);
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            result=0;

            e.printStackTrace();
        }

        return result;




    }

    public static int returnBook(Book book,int userId){

        PreparedStatement statement=null;


        int result=0;
        getConnection();

        try {

            String sql;
            //sql = "INSERT INTO LIBRARYBOOKLOAN.Books(title,author,publication,librarylocation) VALUES (?,?,?,?)";
            sql="DELETE FROM librarybookloan.userbooks  WHERE `bookid`=?";

            statement = conn.prepareStatement(sql);

            statement.setInt(1,book.getId());

            result=statement.executeUpdate();
            System.out.println("result: "+result);
            conn.close();

            book.setStatus("available");
            updateBook(book);
            checkReturningBook(book);

        } catch (SQLException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            result=0;

            e.printStackTrace();
        }

        return result;




    }

    public static int removeWaiting(Book book,int userId){

        PreparedStatement statement=null;


        int result=0;
        getConnection();

        try {

            String sql;
            //sql = "INSERT INTO LIBRARYBOOKLOAN.Books(title,author,publication,librarylocation) VALUES (?,?,?,?)";
            sql="DELETE FROM librarybookloan.Waitings  WHERE `bookid`=? AND `userid`=? ";

            statement = conn.prepareStatement(sql);

            statement.setInt(1,book.getId());
            statement.setInt(2,userId);


            result=statement.executeUpdate();
            System.out.println("result: "+result);
            conn.close();

            book.setStatus("available");
            updateBook(book);
            checkReturningBook(book);

        } catch (SQLException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            result=0;

            e.printStackTrace();
        }

        return result;




    }

    public static int deleteUser(int userId){

        PreparedStatement statement=null;


        int result=0;
        getConnection();

        try {

            String sql;

            sql="DELETE FROM librarybookloan.Users  WHERE `_id`=?";

            statement = conn.prepareStatement(sql);

            statement.setInt(1,userId);

            result=statement.executeUpdate();
            System.out.println("result: "+result);
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            result=0;

            e.printStackTrace();
        }

        return result;




    }



    public static boolean insertCheckout(int userId, Book book){


        // bu fonksiyon checkout yapacak 2 dakikalık yetiştiremedim tamamlıcam akşam.


        PreparedStatement statement=null;



            getConnection();


        try {

            String sql;
            sql = "INSERT INTO librarybookloan.UserBooks(startdate, enddate, userid, bookid) VALUES (?,?,?,?)";

            Calendar calendar = Calendar.getInstance();


            java.util.Date currentDate = calendar.getTime();

            // now, create a java.sql.Date from the java.util.Date

            LocalDate startDate=LocalDate.now();


            LocalDate endDate=LocalDate.now().plusMonths(1);


            statement = conn.prepareStatement(sql);
            statement.setDate(1, Date.valueOf(startDate));
            statement.setDate(2,Date.valueOf(endDate));
            statement.setInt(3, userId);
            statement.setInt(4, book.getId());
            statement.executeUpdate();



            conn.close();

            book.setStatus("taken");
            updateBook(book);

        } catch (SQLException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            return false;
        }catch(Exception e){
            //Handle errors for Class.forName


            e.printStackTrace();
            return false;
        }
        return true;


    }

    public static boolean addToWaitList(int userId, Book book){


        // bu fonksiyon checkout yapacak 2 dakikalık yetiştiremedim tamamlıcam akşam.


        PreparedStatement statement=null;



        getConnection();


        try {

            String sql;
            sql = "INSERT INTO librarybookloan.Waitings(userid, bookid) VALUES (?,?)";



            statement = conn.prepareStatement(sql);

            statement.setInt(1, userId);
            statement.setInt(2, book.getId());
            statement.executeUpdate();



            conn.close();


        } catch (SQLException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            return false;
        }catch(Exception e){
            //Handle errors for Class.forName


            e.printStackTrace();
            return false;
        }
        return true;


    }

    public  static User insertUser(User input){

        PreparedStatement statement=null;


        getConnection();

        try {

            String sql;
            sql = "INSERT INTO LIBRARYBOOKLOAN.Users(fullname,email,password,type) VALUES (?,?,?,?)";

            statement = conn.prepareStatement(sql);
            statement.setString(1,input.getFullname());
            statement.setString(2,input.getEmail());
            statement.setString(3, input.getPassword());
            statement.setString(4, input.getType());
            statement.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            input=null;
            e.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            input=null;

            e.printStackTrace();
        }


        return input;




    }

    public static ArrayList<Book> getUserBooks(int id) {
        ArrayList<Book> books=new ArrayList<>();
        Statement stmt = null;


        getConnection();

        try {
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT Books._id,Books.title,author,publication,librarylocation,status FROM librarybookloan.UserBooks INNER JOIN librarybookloan.Books ON UserBooks.bookid=Books._id WHERE userid= '" + id +"'";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publication"),

                        rs.getString("librarylocation"),
                        rs.getString("status")));



            }

            System.out.println("books: "+books.size());

            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return books;
    }



}
