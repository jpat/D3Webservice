package names;

import java.lang.Exception;
import java.lang.ClassNotFoundException;
import java.lang.InstantiationException;
import java.sql.SQLException;

import org.apache.log4j.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;


@RestController
public class NameRequestController {

    public static final Logger LOGGER = LogManager.getRootLogger();


    @RequestMapping("/one-state")
    public NameInfo getFrequencyOneState(@RequestParam(value="name", defaultValue="Julia") String nameParam,
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
            LOGGER.info("Connected to database");

            stmt = conn.createStatement();

            LOGGER.info("Checking for " + nameParam + " " + stateParam + " " + yearParam);
            String nameQuery = String.format(template, nameParam, stateParam, yearParam);
            rs = stmt.executeQuery(nameQuery);

            while (rs.next()){
                int freq = rs.getInt("FREQUENCY");
                LOGGER.info("Frequency: " + freq);
                frequency += freq;
            }
        }
        catch(Exception e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
        finally{

            if(rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    LOGGER.error(e);
                    e.printStackTrace();
                }
            }
            if(stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    LOGGER.error(e);
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    LOGGER.error(e);
                    e.printStackTrace();
                }
            }

        }

        return new NameInfo(stateParam, yearParam, frequency);
    }

    @RequestMapping("/all-states")
    public NameInfo[] getFrequencyAllStates(@RequestParam(value="name", defaultValue="Julia") String nameParam,
                                           @RequestParam(value="year", defaultValue="1989") int yearParam){
        int frequency;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        ArrayList<NameInfo> allResults = new ArrayList<>();
        NameInfo[] namesArray = new NameInfo[allResults.size() + 1];

        String template = "SELECT state,frequency FROM names WHERE name='%s' AND year='%s'";

        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            conn = DriverManager.getConnection("jdbc:mysql://localhost/namesdb", "root", "");
            System.out.println("Connected to database.");

            stmt = conn.createStatement();

            System.out.println("Checking for " + nameParam + " " + yearParam);
            String nameQuery = String.format(template, nameParam, yearParam);
            rs = stmt.executeQuery(nameQuery);


            while (rs.next()){
                String stateParam = rs.getString("STATE");
                frequency = rs.getInt("FREQUENCY");
                System.out.println(frequency);

                allResults.add(new NameInfo(stateParam, yearParam, frequency));
            }
        }


        catch(Exception e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
        finally{

            if(rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    LOGGER.error(e);
                    e.printStackTrace();
                }
            }
            if(stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    LOGGER.error(e);
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    LOGGER.error(e);
                    e.printStackTrace();
                }
            }

        }

        return allResults.toArray(namesArray);

    }

}