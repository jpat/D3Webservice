package names;

import java.lang.Exception;
import java.lang.ClassNotFoundException;
import java.lang.InstantiationException;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

@RestController
public class NameRequestController {

    @RequestMapping("/name")
    public NameInfo getNameDataFromDB(@RequestParam(value="name", defaultValue="Julia") String nameParam,
                                      @RequestParam(value="state", defaultValue="PA") String stateParam,
                                      @RequestParam(value="year", defaultValue="1989") int yearParam){

        int frequency = 0;

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        String template = "SELECT frequency FROM names WHERE name='%s' AND state='%s' AND year='%s'";

        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            conn = DriverManager.getConnection("jdbc:mysql://localhost/namesdb", "root", "");
            System.out.println("Connected to database.");

            stmt = conn.createStatement();

            System.out.println("Checking for " + nameParam + " " + stateParam + " " + yearParam);
            String nameQuery = String.format(template, nameParam,stateParam,yearParam);
            rs = stmt.executeQuery(nameQuery);

            while (rs.next()){
                int freq = rs.getInt("FREQUENCY");
                System.out.println(freq);
                frequency += freq;
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
        finally{

            try {
                rs.close();
            }catch(Exception e){
                System.out.println(e);
            }
            try {
                stmt.close();
            }catch(Exception e){
                System.out.println(e);
            }
            if(conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

        }

        return new NameInfo(stateParam, yearParam, frequency);
    }

}