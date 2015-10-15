package names;

import java.lang.ClassNotFoundException;
import java.lang.Exception;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.InstantiationException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
public class NameRequestController {

    @RequestMapping("/name")
    public NameInfo getNameDataFromDB(@RequestParam(value="name", defaultValue="World") String name){

        String state = "PA";
        int year = 1998;
        int frequency = 5;

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        String template = "SELECT frequency FROM names WHERE name='%s' AND state='PA' AND year='1989'";

        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            conn = DriverManager.getConnection("jdbc:mysql://localhost/namesdb", "root", "");
            System.out.println("connected!!!");

            stmt = conn.createStatement();

            String nameQuery = String.format(template, name);
            rs = stmt.executeQuery(nameQuery);

            while (rs.next()){
                int freq = rs.getInt("FREQUENCY");
                System.out.println(freq);
                frequency = freq;
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
        finally{

//            try {
//                rs.close();
//            }catch(Exception e){
//                System.out.println(e);
//            }
//            try {
//                stmt.close();
//            }catch(Exception e){
//                System.out.println(e);
//            }
            if(conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

        }

        return new NameInfo(state, year, frequency);
    }

}