package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.apache.jasper.runtime.*;

public class error_jsp extends HttpJspBase {


  private static java.util.Vector _jspx_includes;

  public java.util.List getIncludes() {
    return _jspx_includes;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    javax.servlet.jsp.PageContext pageContext = null;
    HttpSession session = null;
    Throwable exception = (Throwable) request.getAttribute("javax.servlet.jsp.jspException");
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html;charset=ISO-8859-1");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("<!DOCTYPE HTML PUBLIC \"-//w3c//dtd html 4.0 transitional//en\">\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<title>ERROR");
      out.write("</title>\r\n");
      out.write("</head>\r\n");
      out.write("<body bgcolor=\"#FFFFFF\">\r\n\r\n");
      out.write("<h2>An error occured");
      out.write("</h2>\r\n\r\n");
      out.write("<pre>");
      out.write("<h3 style=\"color: red\">");
      out.print( exception );
      out.write("</h3>");
      out.write("</pre>\r\n\r\n");
      out.write("<pre>");
 exception.printStackTrace(new java.io.PrintWriter(out)); 
      out.write("</pre>\r\n\r\n");
      out.write("<a href=\"javascript:history.back()\">You may be able to recover by going back");
      out.write("</a>\r\n\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
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
