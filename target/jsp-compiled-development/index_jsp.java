package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.apache.jasper.runtime.*;
import org.archive.crawler.admin.CrawlJobHandler;
import org.archive.crawler.admin.CrawlJob;
import org.archive.crawler.Heritrix;
import org.archive.crawler.framework.CrawlController;
import org.archive.crawler.admin.CrawlJob;
import org.archive.crawler.Heritrix;
import org.archive.crawler.admin.StatisticsTracker;
import org.archive.util.ArchiveUtils;
import org.archive.util.TextUtils;
import javax.servlet.jsp.JspWriter;
import java.text.SimpleDateFormat;
import org.archive.crawler.Heritrix;
import org.archive.crawler.admin.CrawlJob;
import org.archive.util.ArchiveUtils;
import org.archive.util.TextUtils;
import org.archive.crawler.admin.StatisticsTracker;
import org.archive.crawler.Heritrix;
import java.util.Map;
import java.util.Iterator;

public class index_jsp extends HttpJspBase {


	private void printTime(final JspWriter out,long time)
    throws java.io.IOException {
	    out.println(ArchiveUtils.formatMillisecondsToConventional(time,false));
	}


  private static java.util.Vector _jspx_includes;

  static {
    _jspx_includes = new java.util.Vector(4);
    _jspx_includes.add("/include/handler.jsp");
    _jspx_includes.add("/include/head.jsp");
    _jspx_includes.add("/include/stats.jsp");
    _jspx_includes.add("/include/foot.jsp");
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
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			"/error.jsp", true, 8192, true);
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

    /**
     * This include page ensures that the handler exists and is ready to be
     * accessed.
     */
    CrawlJobHandler handler =
        (CrawlJobHandler)application.getAttribute("handler");
    Heritrix heritrix = (Heritrix)application.getAttribute("heritrix");
    
    // If handler is empty then this is the first time this bit of code is
    // being run since the server came online. In that case get or create the
    // handler.
    if (handler == null) {
        if(Heritrix.isSingleInstance()) {
            heritrix = Heritrix.getSingleInstance();
            handler = heritrix.getJobHandler();
            application.setAttribute("heritrix", heritrix);
            application.setAttribute("handler", handler);
        } else {
            // TODO:
            // If we get here, then there are multiple heritrix instances
            // and we have to put up a screen allowing the user choose between.
            // Otherwise, there is no Heritrix instance.  Thats a problem.
            throw new RuntimeException("No heritrix instance (or multiple " +
                    "to choose from and we haven't implemented this yet)");
        }
    }
    
    // ensure controller's settingsHandler is always thread-installed 
    // in web ui threads
    if(handler != null) {
        CrawlJob job = handler.getCurrentJob();
        if(job != null) {
            CrawlController controller = job.getController();
            if (controller != null) {
                controller.installThreadContextSettingsHandler();
            }
        }
    }

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

    String sAction = request.getParameter("action");
    if(sAction != null) {
        if(sAction.equalsIgnoreCase("logout")) {
            // Logging out.
            session = request.getSession();
            if (session != null) {
                session.invalidate();
                // Redirect back to here and we'll get thrown to the login
                // page.
                response.sendRedirect(request.getContextPath() + "/index.jsp"); 
            }
        }
    }

    String title = "Admin Console";
    int tab = 0;

      out.write("\r\n\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

    String currentHeritrixName = (heritrix == null)?
        "No current Heritrix instance":
        (heritrix.getMBeanName() == null)?
            heritrix.getInstances().keySet().iterator().next().toString():
            heritrix.getMBeanName().toString();

    /**
     * An include file that handles the "look" and navigation of a web page. 
     * Include at top (where you would normally begin the HTML code).
     * If used, the include "foot.jsp" should be included at the end of the HTML
     * code. It will close any table, body and html tags left open in this one.
     * Any custom HTML code is thus placed between the two.
     *
     * The following variables must exist prior to this file being included:
     *
     * String title - Title of the web page
     * int tab - Which to display as 'selected'.
     *           0 - Console
     *           1 - Jobs
     *           2 - Profiles
     *           3 - Logs
     *           4 - Reports
     *           5 - Settings
     *           6 - Help
     *
     * SimpleHandler handler - In general this is provided by the include
     *                         page 'handler.jsp' which should be included
     *                         prior to this one.
     *
     * @author Kristinn Sigurdsson
     */
    String shortJobStatus = null;
	if(handler.getCurrentJob() != null) {
		shortJobStatus = TextUtils.getFirstWord(handler.getCurrentJob().getStatus());
	}
	String favicon = System.getProperties().getProperty("heritrix.favicon","h.ico");
	

      out.write("\r\n");

    StatisticsTracker stats = null;
    if(handler.getCurrentJob() != null) {
        // Assume that StatisticsTracker is being used.
        stats = (StatisticsTracker)handler.getCurrentJob().
            getStatisticsTracking();
    }

      out.write("\r\n");
      out.write("\r\n\r\n");
      out.write("<html>\r\n    ");
      out.write("<head>\r\n    \t");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\r\n        ");
      out.write("<title>Heritrix: ");
      out.print(title);
      out.write("</title>\r\n        ");
      out.write("<link rel=\"stylesheet\" \r\n            href=\"");
      out.print(request.getContextPath());
      out.write("/css/heritrix.css\">\r\n        ");
      out.write("<link rel=\"icon\" href=\"");
      out.print(request.getContextPath());
      out.write("/images/");
      out.print(favicon);
      out.write("\" type=\"image/x-icon\" />\r\n        ");
      out.write("<link rel=\"shortcut icon\" href=\"");
      out.print(request.getContextPath());
      out.write("/images/");
      out.print(favicon);
      out.write("\" type=\"image/x-icon\" />\r\n        ");
      out.write("<script src=\"/js/util.js\">\r\n        ");
      out.write("</script>\r\n    ");
      out.write("</head>\r\n\r\n    ");
      out.write("<body>\r\n        ");
      out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\">\r\n            ");
      out.write("<tr>\r\n                ");
      out.write("<td>\r\n                    ");
      out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" height=\"100%\">\r\n                        ");
      out.write("<tr>\r\n                            ");
      out.write("<td height=\"60\" width=\"155\" valign=\"top\" nowrap>\r\n                                ");
      out.write("<table border=\"0\" width=\"155\" cellspacing=\"0\" cellpadding=\"0\" height=\"60\">\r\n                                    ");
      out.write("<tr>\r\n                                        ");
      out.write("<td align=\"center\" height=\"40\" valign=\"bottom\">\r\n                                            ");
      out.write("<a border=\"0\" \r\n                                            href=\"");
      out.print(request.getContextPath());
      out.write("/index.jsp\">");
      out.write("<img border=\"0\" src=\"");
      out.print(request.getContextPath());
      out.write("/images/logo.gif\" height=\"37\" width=\"145\">");
      out.write("</a>\r\n                                        ");
      out.write("</td>\r\n                                    ");
      out.write("</tr>\r\n                                    ");
      out.write("<tr>\r\n                                        ");
      out.write("<td class=\"subheading\">\r\n                                            ");
      out.print(title);
      out.write("\r\n                                        ");
      out.write("</td>\r\n                                    ");
      out.write("</tr>\r\n                                ");
      out.write("</table>\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td width=\"5\" nowrap>\r\n                                &nbsp;&nbsp;\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td width=\"460\" align=\"left\" nowrap>\r\n                                ");
      out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" height=\"60\">\r\n                                    ");
      out.write("<tr>\r\n                                        ");
      out.write("<td colspan=\"2\" nowrap>\r\n                                            ");

                                                SimpleDateFormat sdf = new SimpleDateFormat("MMM. d, yyyy HH:mm:ss");
                                                sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
                                            
      out.write("\r\n                                            ");
      out.write("<b>\r\n                                                Status as of ");
      out.write("<a style=\"color: #000000\" href=\"");
      out.print(request.getRequestURL());
      out.write("\">");
      out.print(sdf.format(new java.util.Date()));
      out.write(" GMT");
      out.write("</a>\r\n                                            ");
      out.write("</b>\r\n                                            &nbsp;&nbsp;\r\n                                            ");
      out.write("<span style=\"text-align:right\">\r\n                                            ");
      out.write("<b>\r\n                                                Alerts: \r\n                                            ");
      out.write("</b>\r\n                                            ");
 if(heritrix.getAlertsCount() == 0) { 
      out.write("\r\n                                                ");
      out.write("<a style=\"color: #000000; text-decoration: none\" href=\"");
      out.print(request.getContextPath());
      out.write("/console/alerts.jsp\">no alerts");
      out.write("</a>\r\n                                            ");
 } else if(heritrix.getNewAlertsCount()>0){ 
      out.write("\r\n                                                ");
      out.write("<b>");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/console/alerts.jsp\">");
      out.print(heritrix.getAlerts().size());
      out.write(" (");
      out.print(heritrix.getNewAlertsCount());
      out.write(" new)");
      out.write("</a>");
      out.write("</b>\r\n                                            ");
 } else { 
      out.write("\r\n                                                ");
      out.write("<a style=\"color: #000000\" href=\"");
      out.print(request.getContextPath());
      out.write("/console/alerts.jsp\">");
      out.print(heritrix.getAlertsCount());
      out.write(" (");
      out.print(heritrix.getNewAlertsCount());
      out.write(" new)");
      out.write("</a>\r\n                                            ");
 } 
      out.write("\r\n                                            ");
      out.write("</span>\r\n                                        ");
      out.write("</td>\r\n                                    ");
      out.write("</tr>\r\n                                    ");
      out.write("<tr>\r\n                                        ");
      out.write("<td valign=\"top\" nowrap>\r\n\t\t\t\t\t\t\t\t\t\t");
      out.print( handler.isRunning()
										    ? "<span class='status'>Crawling Jobs</span>"
										    : "<span class='status'>Holding Jobs</span>"
										);
      out.write("<i>&nbsp;");
      out.write("</i>\r\n\t\t\t\t\t\t\t\t\t\t");
      out.write("</td>\r\n\t\t\t\t\t\t\t\t\t\t");
      out.write("<td valign=\"top\" align=\"right\" nowrap>\r\n\t\t\t\t\t\t\t\t\t\t");

										if(handler.isRunning() || handler.isCrawling()) {
										    if(handler.getCurrentJob() != null)
										    {
      out.write("\r\n\t\t\t\t\t\t\t\t\t\t");
      out.write("<span class='status'>\r\n\t\t\t\t\t\t\t\t\t\t");
      out.print( shortJobStatus );
      out.write("</span> job:\r\n\t\t\t\t\t\t\t\t\t\t");
      out.write("<i>");
      out.print( handler.getCurrentJob().getJobName() );
      out.write("</i>\r\n\t\t\t\t\t\t\t\t\t\t");

										    } else {
										        out.println("No job ready <a href=\"");
										        out.println(request.getContextPath());
										        out.println("/jobs.jsp\" style='color: #000000'>(create new)</a>");
										     }
										 }
										
      out.write("\r\n\t\t\t\t\t\t\t\t\t\t");
      out.write("</td>\r\n                                    ");
      out.write("</tr>\r\n                                    ");
      out.write("<tr>\r\n                                        ");
      out.write("<td nowrap>\r\n                                            ");
      out.print(handler.getPendingJobs().size());
      out.write("\r\n                                            jobs\r\n                                            ");
      out.write("<a style=\"color: #000000\" href=\"");
      out.print(request.getContextPath());
      out.write("/jobs.jsp#pending\">pending");
      out.write("</a>,\r\n                                            ");
      out.print(handler.getCompletedJobs().size());
      out.write("\r\n                                            ");
      out.write("<a style=\"color: #000000\" href=\"");
      out.print(request.getContextPath());
      out.write("/jobs.jsp#completed\">completed");
      out.write("</a>\r\n                                            &nbsp;\r\n                                        ");
      out.write("</td>\r\n                                        ");
      out.write("<td nowrap align=\"right\">\r\n                                            ");
 if(handler.isCrawling()){ 
      out.write("\r\n                                                    ");
      out.print((stats != null)? stats.successfullyFetchedCount(): 0);
      out.write(" URIs in \r\n\t\t                                            ");
      out.print( ArchiveUtils.formatMillisecondsToConventional( 
		                                            		((stats != null) 
		                                            		  	? (stats.getCrawlerTotalElapsedTime())
		                                            		  	: 0),
		                                            		false
		                                            	)
		                                            );
      out.write("\r\n\t\t                                            (");
      out.print(ArchiveUtils.doubleToString(((stats != null)? stats.currentProcessedDocsPerSec(): 0),2));
      out.write("/sec)\r\n                                            ");
 } 
      out.write("\r\n                                        ");
      out.write("</td>\r\n                                    ");
      out.write("</tr>\r\n                                ");
      out.write("</table>\r\n                            ");
      out.write("</td>\r\n                        ");
      out.write("</tr>\r\n                    ");
      out.write("</table>\r\n                ");
      out.write("</td>\r\n                ");
      out.write("<td width=\"100%\" nowrap>\r\n                    &nbsp;\r\n                ");
      out.write("</td>\r\n            ");
      out.write("</tr>\r\n            ");
      out.write("<tr>\r\n                ");
      out.write("<td bgcolor=\"#0000FF\" height=\"1\" colspan=\"4\">\r\n                ");
      out.write("</td>\r\n            ");
      out.write("</tr>\r\n            ");
      out.write("<tr>\r\n                ");
      out.write("<td colspan=\"4\" height=\"20\">\r\n                    ");
      out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" height=\"20\">\r\n                        ");
      out.write("<tr>\r\n                            ");
      out.write("<td class=\"tab_seperator\">&nbsp;");
      out.write("</td>\r\n                            ");
      out.write("<td class=\"tab");
      out.print(tab==0?"_selected":"");
      out.write("\">\r\n                                ");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/index.jsp\" class=\"tab_text");
      out.print(tab==0?"_selected":"");
      out.write("\">Console");
      out.write("</a>\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td class=\"tab_seperator\">&nbsp;");
      out.write("</td>\r\n                            ");
      out.write("<td class=\"tab");
      out.print(tab==1?"_selected":"");
      out.write("\">\r\n                                ");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/jobs.jsp\" class=\"tab_text");
      out.print(tab==1?"_selected":"");
      out.write("\">Jobs");
      out.write("</a>\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td class=\"tab_seperator\">&nbsp;");
      out.write("</td>\r\n                            ");
      out.write("<td class=\"tab");
      out.print(tab==2?"_selected":"");
      out.write("\">\r\n                                ");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/profiles.jsp\" class=\"tab_text");
      out.print(tab==2?"_selected":"");
      out.write("\">Profiles");
      out.write("</a>\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td class=\"tab_seperator\">&nbsp;");
      out.write("</td>\r\n                            ");
      out.write("<td class=\"tab");
      out.print(tab==3?"_selected":"");
      out.write("\">\r\n                                ");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/logs.jsp\" class=\"tab_text");
      out.print(tab==3?"_selected":"");
      out.write("\">Logs");
      out.write("</a>\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td class=\"tab_seperator\">&nbsp;");
      out.write("</td>\r\n                            ");
      out.write("<td class=\"tab");
      out.print(tab==4?"_selected":"");
      out.write("\">\r\n                                ");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/reports.jsp\" class=\"tab_text");
      out.print(tab==4?"_selected":"");
      out.write("\">Reports");
      out.write("</a>\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td class=\"tab_seperator\">&nbsp;");
      out.write("</td>\r\n                            ");
      out.write("<td class=\"tab");
      out.print(tab==5?"_selected":"");
      out.write("\">\r\n                                ");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/setup.jsp\" class=\"tab_text");
      out.print(tab==5?"_selected":"");
      out.write("\">Setup");
      out.write("</a>\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td class=\"tab_seperator\">&nbsp;");
      out.write("</td>\r\n                            ");
      out.write("<td class=\"tab");
      out.print(tab==6?"_selected":"");
      out.write("\">\r\n                                ");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/help.jsp\" class=\"tab_text");
      out.print(tab==6?"_selected":"");
      out.write("\">Help");
      out.write("</a>\r\n                             ");
      out.write("</td>\r\n                            ");
      out.write("<td width=\"100%\">\r\n                            ");
      out.write("</td>\r\n                        ");
      out.write("</tr>\r\n                    ");
      out.write("</table>\r\n                ");
      out.write("</td>\r\n            ");
      out.write("</tr>\r\n            ");
      out.write("<tr>\r\n                ");
      out.write("<td bgcolor=\"#0000FF\" height=\"1\" colspan=\"4\">");
      out.write("</td>\r\n            ");
      out.write("</tr>\r\n         ");
      out.write("</table>\r\n                    ");
      out.write("<!-- MAIN BODY -->\r\n");
      out.write("\r\n    \r\n    ");
      out.write("<script type=\"text/javascript\">\r\n        function doTerminateCurrentJob(){\r\n            if(confirm(\"Are you sure you wish to terminate the job currently being crawled?\")){\r\n                document.location = '");
out.print(request.getContextPath());
      out.write("/console/action.jsp?action=terminate';\r\n            }\r\n        }    \r\n    ");
      out.write("</script>\r\n    \r\n    ");
      out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
      out.write("<tr>");
      out.write("<td>\r\n    ");
      out.write("<fieldset style=\"width: 750px\">\r\n        ");
      out.write("<legend> \r\n        ");
      out.write("<b>");
      out.write("<span class=\"legendTitle\">Crawler Status:");
      out.write("</span> \r\n        ");
      out.print( handler.isRunning() 
            ? "<span class='status crawling'>CRAWLING JOBS</span></b> | "
              +"<a href='"+request.getContextPath()+"/console/action.jsp?action=stop'>Hold</a>"
            : "<span class='status holding'>HOLDING JOBS</span></b> | "
              +"<a href='"+request.getContextPath()+"/console/action.jsp?action=start'>Start</a>"
        );
      out.write(" ");
      out.write("</b>\r\n        ");
      out.write("</legend>\r\n        ");
      out.write("<div style=\"float:right;padding-right:50px;\">\r\n\t        ");
      out.write("<b>Memory");
      out.write("</b>");
      out.write("<br>\r\n\t        ");
      out.write("<div style=\"padding-left:20px\">\r\n\t\t        ");
      out.print((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024);
      out.write(" KB \r\n\t\t        used");
      out.write("<br>\r\n\t\t        ");
      out.print((Runtime.getRuntime().totalMemory())/1024);
      out.write(" KB\r\n\t\t        current heap");
      out.write("<br>\r\n\t\t        ");
      out.print((Runtime.getRuntime().maxMemory())/1024);
      out.write(" KB\r\n\t\t        max heap\r\n\t        ");
      out.write("</div>\r\n\t    ");
      out.write("</div>\r\n        ");
      out.write("<b>Jobs");
      out.write("</b>\r\n        ");
      out.write("<div style=\"padding-left:20px\">\r\n\t\t\t");
      out.print( handler.getCurrentJob()!=null
			    ? shortJobStatus+": <i>"
			      +handler.getCurrentJob().getJobName()+"</i>"
			    : ((handler.isRunning()) ? "None available" : "None running")
			 );
      out.write("<br>\r\n\t        ");
      out.print( handler.getPendingJobs().size() );
      out.write(" pending,\r\n\t        ");
      out.print( handler.getCompletedJobs().size() );
      out.write(" completed\r\n        ");
      out.write("</div>\r\n\r\n        ");
      out.write("<b>Alerts:");
      out.write("</b>\r\n\t        ");
      out.write("<a style=\"color: #000000\" \r\n\t            href=\"");
      out.print(request.getContextPath());
      out.write("/console/alerts.jsp\">\r\n\t            ");
      out.print(heritrix.getAlertsCount());
      out.write(" (");
      out.print(heritrix.getNewAlertsCount());
      out.write(" new)\r\n\t        ");
      out.write("</a>\r\n\t        \r\n         ");
      out.write("</fieldset>\r\n            ");

            	long begin, end;
	            if(stats != null) {
	                begin = stats.successfullyFetchedCount();
	                end = stats.totalCount();
	                if(end < 1) {
	                    end = 1;
	                }
	            } else {
                    begin = 0;
                    end = 1;
	            }
                
                if(handler.getCurrentJob() != null)
                {
                    final long timeElapsed, timeRemain;
                    if(stats == null) {
                        timeElapsed= 0;
                        timeRemain = -1;
                    } else {
	                    timeElapsed = (stats.getCrawlerTotalElapsedTime());
	                    if(begin == 0) {
	                        timeRemain = -1;
	                    } else {
	                        timeRemain = ((long)(timeElapsed*end/(double)begin))-timeElapsed;
	                    }
                    }
            
      out.write("\r\n            ");
      out.write("<fieldset style=\"width: 750px\">\r\n               ");
      out.write("<legend>\r\n               ");
      out.write("<b>");
      out.write("<span class=\"legendTitle\">Job Status:");
      out.write("</span>\r\n               ");
      out.print( 
               "<span class='status "
               +shortJobStatus+"'>"
               +shortJobStatus+"</span>"
               );
      out.write("\r\n               ");
      out.write("</b> \r\n");
      
    if(handler.isCrawling()) {
	    if ((handler.getCurrentJob().getStatus().
                equals(CrawlJob.STATUS_PAUSED) ||
            handler.getCurrentJob().getStatus().
			    equals(CrawlJob.STATUS_WAITING_FOR_PAUSE))) {
            out.println("| <a href='/console/action.jsp?action=resume'>" +
                "Resume</a>");
            out.println(" | ");
            out.println("<a href=\"");
            out.println(request.getContextPath());
            out.println("/console/action.jsp?action=checkpoint\">" +
                "Checkpoint</a>");
        } else if (!handler.getCurrentJob().isCheckpointing()) {
            out.println("| <a href=\"");
            out.println(request.getContextPath());
            out.println("/console/action.jsp?action=pause\">Pause</a> ");
            if (!handler.getCurrentJob().getStatus().
                   equals(CrawlJob.STATUS_PENDING)) {
                out.println(" | ");
                out.println("<a href=\"");
                out.println(request.getContextPath());
                out.println("/console/action.jsp?action=checkpoint\">" +
                    "Checkpoint</a>");
            }
        }
        out.println(" | <a href='javascript:doTerminateCurrentJob()'>" +
            "Terminate</a>");
    }

      out.write("\r\n               ");
      out.write("</legend>\r\n\r\n                ");

                  if(handler.isCrawling() && stats != null)
                  {
                
      out.write("\r\n                \t");
      out.write("<div style=\"float:right; padding-right:50px;\">\r\n                \t    ");
      out.write("<b>Load");
      out.write("</b>\r\n            \t\t\t");
      out.write("<div style=\"padding-left:20px\">\r\n\t\t\t            \t");
      out.print(stats.activeThreadCount());
      out.write(" active of ");
      out.print(stats.threadCount());
      out.write(" threads\r\n\t\t\t            \t");
      out.write("<br>\r\n\t\t\t            \t");
      out.print(ArchiveUtils.doubleToString((double)stats.congestionRatio(),2));
      out.write("\r\n\t\t\t            \tcongestion ratio\r\n\t\t\t            \t");
      out.write("<br>\r\n\t\t\t            \t");
      out.print(stats.deepestUri());
      out.write(" deepest queue\r\n\t\t\t            \t");
      out.write("<br>\r\n\t\t\t            \t");
      out.print(stats.averageDepth());
      out.write(" average depth\r\n\t\t\t\t\t\t");
      out.write("</div>\r\n\t\t\t\t\t");
      out.write("</div>\r\n\t                ");
      out.write("<b>Rates");
      out.write("</b>\r\n\t                ");
      out.write("<div style=\"padding-left:20px\">\r\n\t\t                ");
      out.print(ArchiveUtils.doubleToString(stats.currentProcessedDocsPerSec(),2));
      out.write(" \t\t                \r\n\t\t                URIs/sec\r\n\t\t                (");
      out.print(ArchiveUtils.doubleToString(stats.processedDocsPerSec(),2));
      out.write(" avg)\r\n\t\t                ");
      out.write("<br>\r\n\t\t                ");
      out.print(stats.currentProcessedKBPerSec());
      out.write("\r\n\t\t\t\t\t\tKB/sec\r\n\t\t\t\t\t\t(");
      out.print(stats.processedKBPerSec());
      out.write(" avg)\r\n\t\t\t\t\t");
      out.write("</div>\r\n\r\n                    ");
      out.write("<b>Time");
      out.write("</b>\r\n                    ");
      out.write("<div class='indent'>\r\n\t                    ");
      out.print( ArchiveUtils.formatMillisecondsToConventional(timeElapsed,false) );
      out.write("\r\n\t\t\t\t\t\telapsed\r\n\t\t\t\t\t\t");
      out.write("<br>\r\n\t                    ");

	                       if(timeRemain != -1) {
	                    
      out.write("\r\n\t\t                    ");
      out.print( ArchiveUtils.formatMillisecondsToConventional(timeRemain,false) );
      out.write("\r\n\t\t                    remaining (estimated)\r\n\t\t               \t");

	                       }
                   		
      out.write("\r\n\t\t\t\t\t");
      out.write("</div>\r\n                    ");
      out.write("<b>Totals");
      out.write("</b>\r\n                \t");

                          }
                }
                if(stats != null)
                {
	                int ratio = (int) (100 * begin / end);
            
      out.write("\r\n                            ");
      out.write("<center>\r\n                            ");
      out.write("<table border=\"0\" cellpadding=\"0\" cellspacing= \"0\" width=\"600\"> \r\n                                ");
      out.write("<tr>\r\n                                    ");
      out.write("<td align='right' width=\"25%\">downloaded ");
      out.print( begin );
      out.write("&nbsp;");
      out.write("</td>\r\n                                    ");
      out.write("<td class='completedBar' width=\"");
      out.print( (int)ratio/2 );
      out.write("%\" align=\"right\">\r\n                                    ");
      out.print( ratio > 50 ? "<b>"+ratio+"</b>%&nbsp;" : "" );
      out.write("\r\n                                    ");
      out.write("</td>\r\n                                    ");
      out.write("<td class='queuedBar' align=\"left\" width=\"");
      out.print( (int) ((100-ratio)/2) );
      out.write("%\">\r\n                                    ");
      out.print( ratio <= 50 ? "&nbsp;<b>"+ratio+"</b>%" : "" );
      out.write("\r\n                                    ");
      out.write("</td>\r\n                                    ");
      out.write("<td width=\"25%\" nowrap>&nbsp;");
      out.print( stats.queuedUriCount() );
      out.write(" queued");
      out.write("</td>\r\n                                ");
      out.write("</tr>\r\n                            ");
      out.write("</table>\r\n                            ");
      out.print( end );
      out.write(" total downloaded and queued");
      out.write("<br>      \r\n                    \t\t");
      out.print(stats.crawledBytesSummary());
      out.write("\r\n                            ");
      out.write("</center>\r\n            ");

                }
                if (handler.getCurrentJob() != null &&
                	handler.getCurrentJob().getStatus().equals(CrawlJob.STATUS_PAUSED)) {
            
      out.write("\r\n            \t\t");
      out.write("<b>Paused Operations");
      out.write("</b>\r\n            \t\t");
      out.write("<div class='indent'>\r\n\t                \t");
      out.write("<a href='");
      out.print( request.getContextPath() );
      out.write("/console/frontier.jsp'>View or Edit Frontier URIs");
      out.write("</a>\r\n\t                ");
      out.write("</div>\r\n\t        ");

            	}
            
      out.write("\r\n    ");
      out.write("</fieldset>\r\n    ");
      out.write("</td>");
      out.write("</tr>\r\n    ");
      out.write("<tr>");
      out.write("<td>\r\n    \r\n\t");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/\">Refresh");
      out.write("</a>\r\n    ");
      out.write("</td>");
      out.write("</tr>\r\n    ");
      out.write("<tr>");
      out.write("<td>\r\n        ");
      out.write("<p>\r\n            &nbsp;\r\n        ");
      out.write("<p>\r\n            &nbsp;\r\n    ");
      out.write("</td>");
      out.write("</tr>\r\n    ");
      out.write("<tr>");
      out.write("<td>\r\n        ");
 if (heritrix.isCommandLine()) {  
            // Print the shutdown only if we were started from command line.
            // It makes no sense when in webcontainer mode.
         
      out.write("\r\n        ");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/console/shutdown.jsp\">Shut down Heritrix software");
      out.write("</a> |\r\n        ");
 } 
      out.write("\r\n        ");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/index.jsp?action=logout\">Logout");
      out.write("</a>\r\n    ");
      out.write("</td>");
      out.write("</tr>");
      out.write("</table>\r\n");

    /**
     * An include file that handles the "look" and navigation of a web page. 
     * Wrapps up things begun in the "head.jsp" include file.  See it for
     * more details.
     *
     * @author Kristinn Sigurdsson
     */

      out.write("\r\n");
      out.write("<br/>\r\n");
      out.write("<br/>\r\n        ");
      out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\">\r\n            ");
      out.write("<tr>\r\n            ");
      out.write("<td bgcolor=\"#0000FF\" height=\"1\" colspan=\"4\">");
      out.write("</td>\r\n            ");
      out.write("</tr>\r\n            ");
      out.write("<tr>\r\n            ");
      out.write("<td class=\"instance_name\">Identifier: ");
      out.print(currentHeritrixName);
      out.write("</td>\r\n            ");
      out.write("</tr>\r\n        ");
      out.write("</table>\r\n                    ");
      out.write("<!-- END MAIN BODY -->\r\n    ");
      out.write("</body>\r\n");
      out.write("</html>");
      out.write("\r\n");
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
