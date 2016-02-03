

import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class login extends HttpServlet {

  private Connection conn;
    public void creatDesign(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException
             {
 RequestDispatcher rd =  request.getRequestDispatcher("buildForm");     
 PrintWriter out = response.getWriter();
 out.println("<HTML>");	
        out.println("<BODY>");
       out.println("<BR>");
       rd.include(request, response);
        out.println("</BODY>");	
        out.println("</HTML>");

        }
    

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
          response.setContentType("text/html"); 
         HttpSession session = request.getSession(false);
         String logged = (String) session.getAttribute("loggedIn");
         if (session != null){
                creatDesign(request, response);    
                     } 
            else
             {
	
	if (!logged.equals("true")||logged==null){
           creatDesign(request, response);    
               
                  }

       
    }
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { 
         response.setContentType("text/html");
 PrintWriter out = response.getWriter();
 String user = request.getParameter("username");
 String password = request.getParameter("pass");
  if (user == null ||user.equals("") && password == null || password.equals("")) {    
  out.println("<html>");
 out.println("<head>");
 out.println("<title>Error page</title>");
 out.println("</head>");
 out.println("<body>");
 out.println("<h1>Empty Data</h1>");
 out.println("<h2>back and enter Data</h2>");
 RequestDispatcher rd =  request.getRequestDispatcher("buildForm");
 rd.include(request, response);
 out.println("</body>");
 out.println("</html>");
      
        }
  else if (validateUser(user, password)) {
   HttpSession session = request.getSession(true);
  session.setAttribute("loggedIn", new String("true"));
  response.sendRedirect("ContentServletURL");
  RequestDispatcher rd =  request.getRequestDispatcher("Home.html");
  rd.forward(request, response);
  

 } else {
  
 out.println("<html>");
 out.println("<head>");
 out.println("<title>Error page</title>");
 out.println("</head>");
 out.println("<body>");
 out.println("<h1>Incorrect Data</h1>");
 out.println("<h2>back and enter a correct user ID and password</h2>");
 RequestDispatcher rd =  request.getRequestDispatcher("buildForm");
  rd.include(request, response);
 out.println("</body>");
 out.println("</html>");
 }
       
    }
        public boolean validateUser(String user,String password){
	try   {
      
      String JDBC_DRIVER  = "com.mysql.jdbc.Driver"; 
      String DB_URL = "jdbc:mysql://localhost/Chat";
       Class.forName(JDBC_DRIVER);
       conn = (Connection) DriverManager.getConnection(DB_URL, "root","");
         PreparedStatement pStm = conn.prepareStatement("select email , password   from Chat.Users where email= ?  and password= ?");
            pStm.setString(1, user);
               pStm.setString(2, password);
                ResultSet result = pStm.executeQuery();
	            if (result.next()) {
                String userEmail = result.getString(1);         
               String userPass = result.getString(2);
               if(user.equals(userEmail) && password.equals(userPass)){
                   return true;
               }     
            }
            result.close();
            pStm.close();
            conn.close();
             
	}
	catch (Exception e)  {
   	  Logger.getLogger(login .class.getName()).log(Level.SEVERE, null, e);
	}
	return false;
}

        }


