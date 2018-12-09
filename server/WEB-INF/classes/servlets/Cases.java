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

public class Cases extends HttpServlet {

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
      ResultSet rs = stm.executeQuery("SELECT * FROM cases");
      JSONArray array = new JSONArray();

      response.setContentType("text/json;charset=UTF-8");

      while(rs.next()){
        JSONObject obj = new JSONObject();
        obj.put("caseNo", rs.getInt("id"));
        JSONArray arr = new JSONArray();
        arr.put(rs.getInt("x"));
        arr.put(rs.getInt("y"));
        arr.put(rs.getInt("width"));
        arr.put(rs.getInt("height"));
        obj.put("coords", arr);
        array.put(obj);
      }
      out.println(array.toString(1));

    } catch(SQLException sqle){
      out.println("Database error: " + sqle.getMessage());
    }
    out.close();
  }
}
