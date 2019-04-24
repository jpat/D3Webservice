package names;

import java.io.FileReader;
import java.lang.Exception;

import org.apache.log4j.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;


@RestController
@CrossOrigin
public class NameRequestController {

    static final Logger LOGGER = LogManager.getRootLogger();
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    private void _openConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            FileReader fr = new FileReader("credentials.txt");
            Scanner scanner = new Scanner("credentials.txt");
            String username = "";
            String password = "";
            if (scanner.hasNext()){
                username = scanner.next();
            }
            if (scanner.hasNext()){
                password = scanner.next();
            }

            conn = DriverManager.getConnection("jdbc:mysql://localhost/namesdb", username, password);
            if (conn != null) {
                LOGGER.info("Connected to database");
            }
            else{
                LOGGER.error("Could not connect to database");
            }
        }
        catch(Exception e){

        }
    }

    private void _closeConnections(){
        if(rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                LOGGER.error("Error closing ResultSet: " + e);
                e.printStackTrace();
            }
        }
        if(stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
                LOGGER.error("Error closing statement: " + e);
                e.printStackTrace();
            }
        }
        if(conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                LOGGER.error("Error closing connection: " + e);
                e.printStackTrace();
            }
        }
    }


    @RequestMapping("/one-state")
    public NameInfo getFrequencyOneState(@RequestParam(value="name", defaultValue="Julia") String nameParam,
                                        @RequestParam(value="state", defaultValue="PA") String stateParam,
                                        @RequestParam(value="year", defaultValue="1989") int yearParam){

        int frequency = 0;
        String queryTemplate = "SELECT frequency FROM names WHERE name='%s' AND state='%s' AND year='%s'";

        try{
            _openConnection();

            stmt = conn.createStatement();

            LOGGER.info("Checking for " + nameParam + " " + stateParam + " " + yearParam);
            String nameQuery = String.format(queryTemplate, nameParam, stateParam, yearParam);
            rs = stmt.executeQuery(nameQuery);

            while (rs.next()){
                int freq = rs.getInt("FREQUENCY");
                LOGGER.info("Frequency: " + freq);
                frequency += freq; //there could be male and female for one name so we add
            }
        }
        catch(Exception e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
        finally{
            _closeConnections();
        }

        return new NameInfo(nameParam, stateParam, yearParam, frequency);
    }

    @RequestMapping("/all-states")
    public NameInfo[] getFrequencyAllStates(@RequestParam(value="name", defaultValue="Julia") String nameParam,
                                           @RequestParam(value="year", defaultValue="1989") int yearParam){

        ArrayList<NameInfo> allResults = new ArrayList<>();
        NameInfo[] namesArray = new NameInfo[allResults.size() + 1];

        String queryTemplate = "SELECT state,frequency FROM names WHERE name='%s' AND year='%s'";

        try{
            _openConnection();

            LOGGER.info("Checking for name: " + nameParam + " year: " + yearParam);
            String nameQuery = String.format(queryTemplate, nameParam, yearParam);
            rs = stmt.executeQuery(nameQuery);

            while (rs.next()){
                String stateData = rs.getString("STATE");
                int frequency = rs.getInt("FREQUENCY");
                System.out.println(frequency);
                // TODO this is not correct for M & F with same name

                allResults.add(new NameInfo(nameParam, stateData, yearParam, frequency));
            }
        }

        catch(Exception e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
        finally{
            _closeConnections();
        }

        return allResults.toArray(namesArray);

    }

    @RequestMapping("/get-frequency")
    public NameInfo[] getFrequency(@RequestParam(value="name", defaultValue="Julia") String nameParam,
                                   @RequestParam(value="year", defaultValue="1989") Integer yearParam,
                                   @RequestParam(value="state", defaultValue="PA") String stateParam){

        if (nameParam == null){
            //TODO return error message, we need a name at least
        }

        ArrayList<NameInfo> allResults = new ArrayList<>();
        NameInfo[] namesArray;

        StringBuilder nameQuery = new StringBuilder("SELECT * FROM names WHERE name='" + nameParam + "'");

        if (yearParam != null){
            nameQuery.append(" AND year='" + yearParam + "'");
        }
        if (stateParam != null){
            nameQuery.append(" AND state='" + stateParam + "'");
        }

        try{
            _openConnection();

            LOGGER.info("Executing on: " + nameQuery);
            rs = stmt.executeQuery(nameQuery.toString());

            while (rs.next()){
                String stateData = rs.getString("STATE");
                int yearData = rs.getInt("YEAR");
                int frequency = rs.getInt("FREQUENCY");
                System.out.println(frequency);
                // TODO this is not correct for M & F with same name

                allResults.add(new NameInfo(nameParam, stateData, yearData, frequency));
            }
        }

        catch(Exception e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
        finally{
            _closeConnections();
        }

        namesArray = new NameInfo[allResults.size() + 1];
        return allResults.toArray(namesArray);

    }

}