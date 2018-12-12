package servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import org.json.*;

public class Companies extends HttpServlet {

  @Override
  public void init() throws ServletException {
    try {
      Class.forName("org.sqlite.JDBC");
    }catch(ClassNotFoundException cnfe){
      System.err.println("Could not load driver: " + cnfe.getMessage());
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), UTF_8), true);
    try {
      Connection con = DriverManager.getConnection("jdbc:sqlite:companies.db");
      Statement stm = con.createStatement();
      ResultSet rs = stm.executeQuery("SELECT * FROM companies");
      JSONArray array = new JSONArray();

      response.setContentType("text/json;charset=UTF-8");

      while(rs.next()){
        JSONObject obj = new JSONObject();
        obj.put("name", rs.getString("name"));
        obj.put("email", rs.getString("email"));
        obj.put("info", rs.getString("info"));
        obj.put("fileName", rs.getString("fileName"));
        obj.put("caseNo", rs.getInt("caseNo"));
        array.put(obj);
      }
      out.println(array.toString(1));
      stm.close();
    } catch(SQLException sqle){
      out.println("Database error: " + sqle.getMessage());
    }
    out.close();
  }
}
