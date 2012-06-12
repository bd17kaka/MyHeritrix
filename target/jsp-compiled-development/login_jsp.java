package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.apache.jasper.runtime.*;
import java.net.URLDecoder;

public class login_jsp extends HttpJspBase {


  private static java.util.Vector _jspx_includes;

  static {
    _jspx_includes = new java.util.Vector(1);
    _jspx_includes.add("/include/nocache.jsp");
  }

  public java.util.List getIncludes() {
    return _jspx_includes;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    javax.servlet.jsp.PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html;charset=ISO-8859-1");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			"/error.jsp", true, 8192, true);
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;


response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
response.setHeader("Pragma", "no-cache"); // HTTP 1.0
response.setDateHeader ("Expires", 0); // Prevents caching at the proxy server

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n\r\n");
  String sMessage = null;
      out.write("\r\n\r\n");
      out.write("<html>\r\n    ");
      out.write("<head>\r\n        ");
      out.write("<title>Heritrix: Login");
      out.write("</title>\r\n        ");
      out.write("<link rel=\"stylesheet\" \r\n            href=\"");
      out.print(request.getContextPath());
      out.write("/css/heritrix.css\">\r\n    ");
      out.write("</head>\r\n\r\n    ");
      out.write("<body onload='document.loginForm.j_username.focus()'>\r\n        ");
      out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" height=\"100%\">\r\n            ");
      out.write("<tr>\r\n                ");
      out.write("<td width=\"155\" height=\"60\" valign=\"top\" nowrap>\r\n                    ");
      out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"\r\n                            width=\"100%\" height=\"100%\">\r\n                        ");
      out.write("<tr>\r\n                            ");
      out.write("<td align=\"center\" height=\"40\" valign=\"bottom\">\r\n                                ");
      out.write("<a border=\"0\" \r\n                                href=\"");
      out.print(request.getContextPath());
      out.write("/\">");
      out.write("<img border=\"0\" src=\"");
      out.print(request.getContextPath());
      out.write("/images/logo.gif\" width=\"145\">");
      out.write("</a>\r\n                            ");
      out.write("</td>\r\n                        ");
      out.write("</tr>\r\n                        ");
      out.write("<tr>\r\n                            ");
      out.write("<td class=\"subheading\">\r\n                                Login\r\n                            ");
      out.write("</td>\r\n                        ");
      out.write("</tr>\r\n                    ");
      out.write("</table>\r\n                ");
      out.write("</td>\r\n                ");
      out.write("<td width=\"100%\">&nbsp;");
      out.write("</td>\r\n            ");
      out.write("</tr>\r\n            ");
      out.write("<tr>\r\n                ");
      out.write("<td bgcolor=\"#0000FF\" height=\"1\" colspan=\"2\">\r\n                ");
      out.write("</td>\r\n            ");
      out.write("</tr>\r\n            ");
      out.write("<tr>\r\n                ");
      out.write("<td colspan=\"2\" height=\"100%\" valign=\"top\" class=\"main\">\r\n                    ");
      out.write("<form method=\"post\" \r\n                        action='");
      out.print( response.encodeURL("j_security_check") );
      out.write("'\r\n                            name=\"loginForm\">\r\n                        ");
      out.write("<input type=\"hidden\" name=\"action\" value=\"login\">\r\n                        ");
      out.write("<input type=\"hidden\" name=\"redirect\" \r\n                            value=\"");
      out.print(request.getParameter("back"));
      out.write("\">\r\n                        ");
      out.write("<table border=\"0\">\r\n                            ");
 if(sMessage != null ){ 
      out.write("\r\n                                ");
      out.write("<tr>\r\n                                    ");
      out.write("<td colspan=\"2\" align=\"left\">\r\n                                    ");
      out.write("<b>");
      out.write("<font color=red>");
      out.print(sMessage);
      out.write("</font>");
      out.write("</b>\r\n                                    ");
      out.write("</td>\r\n                                ");
      out.write("</tr>\r\n                            ");
}
      out.write("\r\n                            ");
      out.write("<tr>\r\n                                ");
      out.write("<td class=\"dataheader\">\r\n                                    Username:\r\n                                ");
      out.write("</td>\r\n                                ");
      out.write("<td> \r\n                                    ");
      out.write("<input name=\"j_username\">\r\n                                ");
      out.write("</td>\r\n                            ");
      out.write("</tr>\r\n                            ");
      out.write("<tr>\r\n                                ");
      out.write("<td class=\"dataheader\">\r\n                                    Password:\r\n                                ");
      out.write("</td>\r\n                                ");
      out.write("<td>\r\n                                    ");
      out.write("<input type=\"password\" name=\"j_password\">\r\n                                ");
      out.write("</td>\r\n                            ");
      out.write("</tr>\r\n                            ");
      out.write("<tr>\r\n                                ");
      out.write("<td colspan=\"2\" align=\"center\">\r\n                                    ");
      out.write("<input type=\"submit\" value=\"Login\">\r\n                                ");
      out.write("</td>\r\n                            ");
      out.write("</tr>\r\n                    ");
      out.write("</form>\r\n                ");
      out.write("</td>\r\n            ");
      out.write("</tr>\r\n        ");
      out.write("</table>\r\n    ");
      out.write("</body>\r\n");
      out.write("</HTML>\r\n");
    } catch (Throwable t) {
      out = _jspx_out;
      if (out != null && out.getBufferSize() != 0)
        out.clearBuffer();
      if (pageContext != null) pageContext.handlePageException(t);
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(pageContext);
    }
  }
}
