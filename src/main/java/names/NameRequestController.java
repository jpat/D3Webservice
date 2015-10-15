package names;

import java.lang.Exception;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@RestController
public class NameRequestController {

    @RequestMapping("/name")
    public NameInfo getNameDataFromDB(@RequestParam(value="name", defaultValue="World") String name){

        String state = "PA";
        int year = 1998;
        int frequency = 5;

        return new NameInfo(state, year, frequency);



//        Connection conn = null;
//
//        try{
//            Class.forName("com.mysql.jdbc.Driver").newInstance();
//
//            conn = DriverManager.getConnection("jdbc:mysql://localhost/", "root", "");
//            System.out.println("connected!!!");
//
//        } catch(Exception e){
//            System.out.println(e);
//        } finally{
//            conn.close();
//        }
    }

}