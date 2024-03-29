package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.apache.jasper.runtime.*;
import org.archive.crawler.admin.CrawlJobHandler;
import org.archive.crawler.admin.CrawlJob;
import org.archive.crawler.Heritrix;
import org.archive.crawler.framework.CrawlController;
import org.archive.crawler.datamodel.CrawlOrder;
import org.archive.crawler.admin.CrawlJob;
import java.util.List;
import java.text.SimpleDateFormat;
import org.archive.crawler.Heritrix;
import org.archive.crawler.admin.CrawlJob;
import org.archive.util.ArchiveUtils;
import org.archive.util.TextUtils;
import org.archive.crawler.admin.StatisticsTracker;
import org.archive.crawler.Heritrix;
import java.util.Map;
import java.util.Iterator;

public class jobs_jsp extends HttpJspBase {


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
      out.write("\r\n\r\n");

    String sAction = request.getParameter("action");
    if(sAction != null){
        // Need to handle an action    
        if(sAction.equalsIgnoreCase("delete")){
            handler.deleteJob(request.getParameter("job"));
        }
    }    

    String title = "Crawl jobs";
    int tab = 1;

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
      out.write("\r\n\r\n");
 
    if(request.getParameter("message") != null &&
        request.getParameter("message").length() > 0) {

      out.write("\r\n    ");
      out.write("<p>\r\n        ");
      out.write("<span class=\"flashMessage\">");
      out.write("<b>");
      out.print(request.getParameter("message"));
      out.write("</b>");
      out.write("</span>\r\n");
 } 
      out.write("\r\n\r\n");
 if(handler.isCrawling()){ 
      out.write("\r\n    ");
      out.write("<h2>Active Job - ");
      out.write("<i>");
      out.print(handler.getCurrentJob().getJobName());
      out.write("</i>");
      out.write("</h2>\r\n    ");
      out.write("<ul>\r\n        ");
      out.write("<li>");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/jobs/configure.jsp?job=");
      out.print(handler.getCurrentJob().getUID());
      out.write("\">\r\n        Edit configuration");
      out.write("</a>\r\n\r\n\r\n        ");
      out.write("<li>");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/jobs/clearperhost.jsp\">\r\n        Clear cached per-host settings");
      out.write("</a>\r\n        \r\n        ");
      out.write("<li>");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/jobs/journal.jsp?job=");
      out.print(handler.getCurrentJob().getUID());
      out.write("\">\r\n        Journal");
      out.write("</a>\r\n        \r\n        ");
      out.write("<li>View:");
      out.write("<br> \r\n        ");
      out.write("<ul>\r\n        ");
      out.write("<li>");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/reports/crawljob.jsp\">\r\n            Crawl report");
      out.write("</a>");
      out.write("</li>\r\n        ");
      out.write("<li>");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/reports/seeds.jsp\">\r\n            Seeds report");
      out.write("</a>");
      out.write("</li>\r\n        ");
      out.write("<li>");
      out.write("<a target=\"_blank\" href=\"");
      out.print(request.getContextPath());
      out.write("/jobs/vieworder.jsp?job=");
      out.print(handler.getCurrentJob().getUID());
      out.write("\">\r\n            Crawl order (raw xml)");
      out.write("</a>");
      out.write("</li>\r\n        ");
      out.write("</ul>\r\n    ");
      out.write("</ul>\r\n");
 } 
      out.write("\r\n\r\n");
      out.write("<h2>Create New Job");
      out.write("</h2>\r\n    ");
      out.write("<ul>\r\n\t");
      out.write("<li>");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/jobs/basedon.jsp?type=jobs\">\r\n\tBased on existing job");
      out.write("</a>");
      out.write("</li>\r\n\t");
      out.write("<li>");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/jobs/recovery.jsp\">\r\n\tBased on a recovery");
      out.write("</a>");
      out.write("</li>\r\n\t");
      out.write("<li>");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/jobs/basedon.jsp\">\r\n\tBased on a profile");
      out.write("</a>");
      out.write("</li>\r\n\t");
      out.write("<li>");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/jobs/new.jsp\">\r\n\tWith defaults");
      out.write("</a>");
      out.write("</li>\r\n    ");
      out.write("</ul>\r\n\t\r\n");
      out.write("<h2>");
      out.write("<a name=\"pending\" />Pending\r\nJobs (");
      out.print(handler.getPendingJobs().size());
      out.write(")");
      out.write("</h2>\r\n\r\n");
  
    List jobs = handler.getPendingJobs();
    boolean alt = true;
    // If no pending jobs, don't show table headings.
    if (jobs.size() > 0) {

      out.write("\r\n        ");
      out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"1\">\r\n            ");
      out.write("<tr>\r\n                ");
      out.write("<th>\r\n                    Name\r\n                ");
      out.write("</th>\r\n                ");
      out.write("<th>\r\n                    Status\r\n                ");
      out.write("</th>\r\n                ");
      out.write("<th>\r\n                    Options\r\n                ");
      out.write("</th>\r\n            ");
      out.write("</tr>\r\n            ");

                alt = true;
                for(int i=0 ; i    < jobs.size() ; i++)
                {
                    CrawlJob job = (CrawlJob)jobs.get(i);
            
      out.write("        \r\n                    ");
      out.write("<tr bgcolor='");
      out.print(alt?"#DDDDFF":"#EEEEFF");
      out.write("'>\r\n                        ");
      out.write("<td>\r\n                            ");
      out.print(job.getJobName());
      out.write("&nbsp;&nbsp;\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td>\r\n                            ");
      out.write("<i>");
      out.print(job.getStatus());
      out.write("</i>&nbsp;&nbsp;\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td>\r\n                            ");
      out.write("<a target=\"_blank\" href=\"");
      out.print(request.getContextPath());
      out.write("/jobs/vieworder.jsp?job=");
      out.print(job.getUID());
      out.write("\">View order");
      out.write("</a>\r\n                            &nbsp;\r\n                            ");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/jobs/configure.jsp?job=");
      out.print(job.getUID());
      out.write("\">Edit configuration");
      out.write("</a>\r\n                            &nbsp;\r\n                            ");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/jobs/journal.jsp?job=");
      out.print(job.getUID());
      out.write("\">Journal");
      out.write("</a>\r\n                            &nbsp;\r\n                            ");
      out.write("<a href=\"?action=delete&job=");
      out.print(job.getUID());
      out.write("\">Delete");
      out.write("</a>\r\n                            &nbsp;\r\n                        ");
      out.write("</td>\r\n                    ");
      out.write("</tr>\r\n            ");

                    alt = !alt;
                }
            
      out.write("\r\n        ");
      out.write("</table>\r\n\r\n\r\n");

    // End of if block that tests that there are pending jobs to show.
    }

      out.write("\r\n\r\n\r\n");
      out.write("<h2>");
      out.write("<a name=\"completed\" />Completed\r\nJobs(");
      out.print(handler.getCompletedJobs().size());
      out.write(")");
      out.write("</h2>\r\n\r\n");
  
    jobs = handler.getCompletedJobs();
    // If no completed jobs, don't show table headings.
    if (jobs.size() > 0) {

      out.write("\r\n        ");
      out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"1\"> \r\n            ");
      out.write("<tr>\r\n                ");
      out.write("<th>\r\n                    UID\r\n                ");
      out.write("</th>\r\n                ");
      out.write("<th>\r\n                    Name\r\n                ");
      out.write("</th>\r\n                ");
      out.write("<th>\r\n                    Status\r\n                ");
      out.write("</th>\r\n                ");
      out.write("<th>\r\n                    Options\r\n                ");
      out.write("</th>\r\n            ");
      out.write("</tr>\r\n            ");

                alt = true;
                for(int i=jobs.size()-1 ; i >= 0  ; i--)
                {
                    CrawlJob job = (CrawlJob)jobs.get(i);
            
      out.write("        \r\n                    ");
      out.write("<tr bgcolor='");
      out.print(alt?"#DDDDFF":"#EEEEFF");
      out.write("'>\r\n                        ");
      out.write("<td>\r\n                            ");
      out.write("<code>");
      out.print(job.getUID());
      out.write("</code>&nbsp;&nbsp;\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td>\r\n                            ");
      out.print(job.getJobName());
      out.write("&nbsp;&nbsp;\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td>\r\n                            ");
      out.write("<i>");
      out.print(job.getStatus());
      out.write("</i>&nbsp;&nbsp;&nbsp;\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td>\r\n                            ");
      out.write("<a style=\"color: #003399;\" target=\"_blank\" href=\"");
      out.print(request.getContextPath());
      out.write("/jobs/vieworder.jsp?job=");
      out.print(job.getUID());
      out.write("\">Crawl order");
      out.write("</a>\r\n                            &nbsp;\r\n                            ");
      out.write("<a style=\"color: #003399;\" href=\"");
      out.print(request.getContextPath());
      out.write("/reports/crawljob.jsp?job=");
      out.print(job.getUID());
      out.write("&nav=3\">Crawl report");
      out.write("</a>\r\n                            &nbsp;\r\n                            ");
      out.write("<a style=\"color: #003399;\" href=\"");
      out.print(request.getContextPath());
      out.write("/reports/seeds.jsp?job=");
      out.print(job.getUID());
      out.write("&nav=3\">Seeds report");
      out.write("</a>\r\n                            &nbsp;\r\n                            ");
      out.write("<a style=\"color: #003399;\" href=\"");
      out.print(request.getContextPath());
      out.write("/jobs/viewseeds.jsp?job=");
      out.print(job.getUID());
      out.write("\">Seed file");
      out.write("</a>\r\n                            &nbsp;\r\n                            ");
      out.write("<a style=\"color: #003399;\" href=\"");
      out.print(request.getContextPath());
      out.write("/logs.jsp?job=");
      out.print(job.getUID());
      out.write("&nav=3\">Logs");
      out.write("</a>\r\n                            &nbsp;\r\n                            ");
      out.write("<a style=\"color: #003399;\" href=\"");
      out.print(request.getContextPath());
      out.write("/jobs/journal.jsp?job=");
      out.print(job.getUID());
      out.write("\">Journal");
      out.write("</a>\r\n                            &nbsp;\r\n                            ");
      out.write("<a style=\"color: #003399;\" href=\"?action=delete&job=");
      out.print(job.getUID());
      out.write("&nav=3\">Delete");
      out.write("</a>\r\n                            &nbsp;\r\n                        ");
      out.write("</td>\r\n                    ");
      out.write("</tr>\r\n                    ");
 if(job.getErrorMessage()!=null){ 
      out.write("\r\n                    ");
      out.write("<tr bgcolor='");
      out.print(alt?"#DDDDFF":"#EEEEFF");
      out.write("'>\r\n                        ");
      out.write("<td>");
      out.write("</td>\r\n                        ");
      out.write("<td colspan=\"3\">\r\n                            ");
      out.write("<pre>");
      out.write("<");
      out.write("<span class=\"error\">");
      out.print(job.getErrorMessage());
      out.write("</span>");
      out.write("</pre>\r\n                        ");
      out.write("</td>\r\n                    ");
      out.write("</tr>\r\n                    ");
 } 
      out.write("\r\n            ");

                    alt = !alt;
                }
            
      out.write("\r\n        ");
      out.write("</table>\r\n");

    // End of if block that tests if there are completed jobs to show.
    }

      out.write("\r\n\r\n\r\n");

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
