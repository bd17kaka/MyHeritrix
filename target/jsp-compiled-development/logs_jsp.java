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
import org.archive.crawler.settings.SettingsHandler;
import org.archive.crawler.settings.XMLSettingsHandler;
import org.archive.crawler.admin.CrawlJob;
import org.archive.crawler.util.LogReader;
import java.io.File;
import org.archive.util.TextUtils;
import java.text.SimpleDateFormat;
import org.archive.crawler.Heritrix;
import org.archive.crawler.admin.CrawlJob;
import org.archive.util.ArchiveUtils;
import org.archive.util.TextUtils;
import org.archive.crawler.admin.StatisticsTracker;
import org.archive.crawler.Heritrix;
import java.util.Map;
import java.util.Iterator;

public class logs_jsp extends HttpJspBase {


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
      out.write("\r\n\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n\r\n");

    /* Various settings with default values (where applicable) */
    String mode = request.getParameter("mode");
    String[] log = null;
    String logText = "";
    String logInfo = "";
    int linesToShow = 50;
    int iTime = -1;
    int linenumber = 1;
    String timestamp = null;
    String regexpr = null;
    boolean ln = false;
    boolean grep = false;
    boolean indent = false;
    int linesToSkip = 1;
        
    /* Which log to display */
    String fileName = request.getParameter("log");
    if(fileName == null || fileName.length() <= 0) {
        fileName = "crawl.log";
    }
    
    if(request.getParameter("linesToShow") != null && request.getParameter("linesToShow").length()>0 ){
        try{
            linesToShow = Integer.parseInt(request.getParameter("linesToShow"));
        } catch(java.lang.NumberFormatException e){
            linesToShow = 50;
        }
    }

    /* Location of logs */
    SettingsHandler settingsHandler = null;
    CrawlJob theJob = null;
    if(request.getParameter("job") != null && request.getParameter("job").length() > 0){
        //Get logs for specific job. This assumes that the logs for each job are stored in a unique location.
        theJob = handler.getJob(request.getParameter("job"));
    }else{
        if(handler.getCurrentJob() != null){
            // If no specific job then assume current one
            theJob = handler.getCurrentJob();
        } else if(handler.getCompletedJobs().size() > 0){
            // If no current job, use the latest completed job.
            theJob = (CrawlJob)handler.getCompletedJobs().get(handler.getCompletedJobs().size()-1);
        }
    }
    
    if(theJob != null) {
        // Got a valid crawl order, find it's logs
        if(mode != null && mode.equalsIgnoreCase("number")) {
            /* Get log by line number */
            try {
                linenumber = Integer.
                    parseInt(request.getParameter("linenumber"));
            }
            catch(Exception e){/*Ignore*/}
            log = LogReader.getFromSeries(theJob.getLogPath(fileName),
                linenumber, linesToShow);
        } else if(mode != null && mode.equalsIgnoreCase("time")) {
            /* View by timestamp */
            timestamp = request.getParameter("timestamp");
        
            if(timestamp == null || timestamp.length() < 1)
            {
                // No data
                logText = "No timestamp!";
            }    
            else
            {
                int timestampLinenumber = LogReader.
                    findFirstLineBeginningFromSeries(theJob.getLogPath(fileName),
                        timestamp);
                log =  LogReader.getFromSeries(theJob.getLogPath(fileName),
                    timestampLinenumber, linesToShow);
            }
        }
        else if(mode != null && mode.equalsIgnoreCase("regexpr"))
        {
            /* View by regexpr */
            try
            {
                linesToSkip = Integer.parseInt(request.getParameter("linesToSkip"));
            }
            catch(Exception e){/*Ignore*/}
            
            regexpr = request.getParameter("regexpr");
            
            if(regexpr == null)
            {
                logText = "No regular expression";
            }
            else
            {
                ln = request.getParameter("ln")!=null&&request.getParameter("ln").equalsIgnoreCase("true");
                grep = request.getParameter("grep")!=null&&request.getParameter("grep").equalsIgnoreCase("true");
                indent = request.getParameter("indent")!=null&&request.getParameter("indent").equalsIgnoreCase("true");
                
                if(grep){
                    regexpr = ".*" + regexpr + ".*";
                }
                
                if(indent) {
                    log = LogReader.
                        getByRegExprFromSeries(theJob.getLogPath(fileName),
                            regexpr, " ", ln,linesToSkip-1, linesToShow);
                } else {
                    log = LogReader.
                        getByRegExprFromSeries(theJob.getLogPath(fileName),
                            regexpr, 0, ln,linesToSkip-1, linesToShow);
                }
            }
        } else {
            /* View by tail (default) */
            mode = "tail";
    
            try
            {
                iTime = Integer.parseInt(request.getParameter("time"));
            }
            catch(Exception e){/* Ignore - default value will do */}
            log = LogReader.tail(theJob.getLogPath(fileName), linesToShow);
        }
    } 
    else 
    {
        mode = "tail";
        log = new String[]{"Invalid or missing crawl order",""};
    }
    
    if(log != null && log.length>=2){
        logText = log[0];
        logInfo = log[1];
    } else {
        logText = "";
        logInfo = "";
    }
    
    
    String title = "View logs";
    int tab = 3;
    

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
      out.write("\r\n    ");
 if(iTime>0){ 
      out.write("\r\n        ");
      out.write("<meta http-equiv=Refresh content=\"");
      out.print(iTime);
      out.write(" URL=logs.jsp?time=");
      out.print(iTime);
      out.write("&log=");
      out.print(fileName);
      out.write("&linesToShow=");
      out.print(linesToShow);
      out.write("\">\r\n    ");
 } 
      out.write("\r\n    \r\n    ");
 
        if(theJob == null){
            out.println("<br /><b>No job selected/available</b>");
            return;
        } 
    
      out.write("\r\n    ");
      out.write("<script type=\"text/javascript\">\r\n        function viewLog(log)\r\n        {\r\n            document.frmLogs.log.value = log;\r\n            document.frmLogs.submit();\r\n        }\r\n        \r\n        function changeMode(mode)\r\n        {\r\n            document.frmLogs.mode.value = mode;\r\n            document.frmLogs.submit();\r\n        }\r\n    ");
      out.write("</script>\r\n\r\n    ");
      out.write("<form method=\"get\" action=\"logs.jsp\" name=\"frmLogs\">\r\n        ");
      out.write("<input type=\"hidden\" name=\"job\" value=\"");
      out.print(theJob.getUID());
      out.write("\">\r\n        ");
      out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n            ");
      out.write("<tr>\r\n                ");
      out.write("<td height=\"3\">");
      out.write("</td>\r\n            ");
      out.write("</tr>\r\n            ");
      out.write("<tr>\r\n                ");
      out.write("<td valign=\"top\" width=\"210\">\r\n                    ");
      out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n                        ");
      out.write("<tr>\r\n                            ");
      out.write("<td width=\"50\" align=\"right\" valign=\"top\">\r\n                                &nbsp;");
      out.write("<b>View:");
      out.write("</b>&nbsp;\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td align=\"left\" valign=\"top\" width=\"160\">\r\n                                ");
      out.write("<a href=\"javascript:viewLog('crawl.log')\" ");
      out.print(fileName.equalsIgnoreCase("crawl.log")?"style='text-decoration: none; color: #000000'":"");
      out.write(">crawl.log");
      out.write("</a>");
      out.write("<br>\r\n                                ");
      out.write("<a href=\"javascript:viewLog('local-errors.log')\" ");
      out.print(fileName.equalsIgnoreCase("local-errors.log")?"style='text-decoration: none; color: #000000'":"");
      out.write(">local-errors.log");
      out.write("</a>");
      out.write("<br>\r\n                                ");
      out.write("<a href=\"javascript:viewLog('progress-statistics.log')\" ");
      out.print(fileName.equalsIgnoreCase("progress-statistics.log")?"style='text-decoration: none; color: #000000'":"");
      out.write(">progress-statistics.log");
      out.write("</a>");
      out.write("<br>\r\n                                ");
      out.write("<a href=\"javascript:viewLog('runtime-errors.log')\" ");
      out.print(fileName.equalsIgnoreCase("runtime-errors.log")?"style='text-decoration: none; color: #000000'":"");
      out.write(">runtime-errors.log");
      out.write("</a>");
      out.write("<br>\r\n                                ");
      out.write("<a href=\"javascript:viewLog('uri-errors.log')\" ");
      out.print(fileName.equalsIgnoreCase("uri-errors.log")?"style='text-decoration: none; color: #000000'":"");
      out.write(">uri-errors.log");
      out.write("</a>");
      out.write("<br>\r\n                                ");
      out.write("<input type=\"hidden\" name=\"log\" value=\"");
      out.print(fileName);
      out.write("\">\r\n                            ");
      out.write("</td>\r\n                        ");
      out.write("</tr>\r\n                    ");
      out.write("</table>\r\n                ");
      out.write("</td>\r\n                ");
      out.write("<td valign=\"top\" width=\"170\">\r\n                    ");
      out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n                        ");
      out.write("<tr>\r\n                            ");
      out.write("<td width=\"20\" align=\"right\" valign=\"top\">\r\n                                &nbsp;");
      out.write("<b>By:");
      out.write("</b>&nbsp;\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td align=\"left\" valign=\"top\" width=\"150\">\r\n                                ");
      out.write("<a href=\"javascript:changeMode('number')\" ");
      out.print(mode.equalsIgnoreCase("number")?"style='text-decoration: none; color: #000000'":"");
      out.write(">Line number");
      out.write("</a>");
      out.write("<br>\r\n                                ");
      out.write("<a href=\"javascript:changeMode('time')\" ");
      out.print(mode.equalsIgnoreCase("time")?"style='text-decoration: none; color: #000000'":"");
      out.write(">Time stamp");
      out.write("</a>");
      out.write("<br>\r\n                                ");
      out.write("<a href=\"javascript:changeMode('regexpr')\" ");
      out.print(mode.equalsIgnoreCase("regexpr")?"style='text-decoration: none; color: #000000'":"");
      out.write(">Regular expression");
      out.write("</a>");
      out.write("<br>\r\n                                ");
      out.write("<a href=\"javascript:changeMode('tail')\" ");
      out.print(mode.equalsIgnoreCase("tail")?"style='text-decoration: none; color: #000000'":"");
      out.write(">Tail");
      out.write("</a>");
      out.write("<br>\r\n                                ");
      out.write("<input type=\"hidden\" name=\"mode\" value=\"");
      out.print(mode);
      out.write("\">\r\n                            ");
      out.write("</td>\r\n                        ");
      out.write("</tr>\r\n                    ");
      out.write("</table>\r\n                ");
      out.write("</td>\r\n                ");
      out.write("<td valign=\"top\">\r\n                ");
 if(mode.equalsIgnoreCase("tail")){ 
      out.write("\r\n                    ");
      out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n                        ");
      out.write("<tr>\r\n                            ");
      out.write("<td>\r\n                                ");
      out.write("<b>Refresh time:");
      out.write("</b>&nbsp;\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td>\r\n                                ");
      out.write("<select name=\"time\" onChange=\"document.frmLogs.submit()\" >\r\n                                    ");
      out.write("<option value=\"-1\" ");
      out.print(iTime==-1?"selected":"");
      out.write(">No refresh");
      out.write("</option>\r\n                                    ");
      out.write("<option value=\"2\" ");
      out.print(iTime==2?"selected":"");
      out.write(">2 sec.");
      out.write("</option>\r\n                                    ");
      out.write("<option value=\"5\" ");
      out.print(iTime==5?"selected":"");
      out.write(">5 sec.");
      out.write("</option>\r\n                                    ");
      out.write("<option value=\"10\" ");
      out.print(iTime==10?"selected":"");
      out.write(">10 sec.");
      out.write("</option>\r\n                                    ");
      out.write("<option value=\"20\" ");
      out.print(iTime==20?"selected":"");
      out.write(">20 sec.");
      out.write("</option>\r\n                                    ");
      out.write("<option value=\"30\" ");
      out.print(iTime==30?"selected":"");
      out.write(">30 sec.");
      out.write("</option>\r\n                                ");
      out.write("</select>\r\n                            ");
      out.write("</td>\r\n                        ");
      out.write("</tr>\r\n                        ");
      out.write("<tr>\r\n                            ");
      out.write("<td>\r\n                                ");
      out.write("<b>Lines to show:");
      out.write("</b>&nbsp;\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td>\r\n                                ");
      out.write("<input size=\"4\" name=\"linesToShow\" value=\"");
      out.print(linesToShow);
      out.write("\">\r\n                            ");
      out.write("</td>\r\n                        ");
      out.write("</tr>\r\n                    ");
      out.write("</table>\r\n                ");
 } else if(mode.equalsIgnoreCase("number")){ 
      out.write("\r\n                    ");
      out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n                        ");
      out.write("<tr>\r\n                            ");
      out.write("<td nowrap align=\"right\">\r\n                                ");
      out.write("<b>Line number:");
      out.write("</b>&nbsp;\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td>\r\n                                ");
      out.write("<input size=\"4\" value=\"");
      out.print(linenumber);
      out.write("\" name=\"linenumber\">&nbsp;");
      out.write("<input type=\"submit\" value=\"Get\">\r\n                            ");
      out.write("</td>\r\n                        ");
      out.write("</tr>\r\n                        ");
      out.write("<tr>\r\n                            ");
      out.write("<td>\r\n                                ");
      out.write("<b>Lines to show:");
      out.write("</b>&nbsp;\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td>\r\n                                ");
      out.write("<input size=\"4\" name=\"linesToShow\" value=\"");
      out.print(linesToShow);
      out.write("\">\r\n                            ");
      out.write("</td>\r\n                        ");
      out.write("</tr>\r\n                    ");
      out.write("</table>\r\n                ");
 } else if(mode.equalsIgnoreCase("time")){ 
      out.write("\r\n                    ");
      out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n                        ");
      out.write("<tr>\r\n                            ");
      out.write("<td nowrap align=\"right\" valign=\"top\">\r\n                                ");
      out.write("<b>Timestamp:");
      out.write("</b>&nbsp;\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td>\r\n                                ");
      out.write("<input value=\"");
      out.print(request.getParameter("timestamp")==null?"":request.getParameter("timestamp"));
      out.write("\" name=\"timestamp\" align=\"absmiddle\" size=\"21\">&nbsp;");
      out.write("<input type=\"submit\" value=\"Get\">");
      out.write("<br>\r\n                                (YYYY-MM-DDTHH:MM:SS.SSS)\r\n                            ");
      out.write("</td>\r\n                        ");
      out.write("</tr>\r\n                        ");
      out.write("<tr>\r\n                            ");
      out.write("<td>\r\n                                ");
      out.write("<b>Lines to show:");
      out.write("</b>&nbsp;\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td>\r\n                                ");
      out.write("<input size=\"4\" name=\"linesToShow\" value=\"");
      out.print(linesToShow);
      out.write("\">\r\n                            ");
      out.write("</td>\r\n                        ");
      out.write("</tr>\r\n                    ");
      out.write("</table>\r\n                ");
 } else if(mode.equalsIgnoreCase("regexpr")){ 
      out.write("\r\n                    ");
      out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n                        ");
      out.write("<tr>\r\n                            ");
      out.write("<td nowrap align=\"right\">\r\n                                ");
      out.write("<b>Regular expression:");
      out.write("</b>&nbsp;\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td width=\"100%\" colspan=\"4\">\r\n                                ");
      out.write("<input size=\"50\" name=\"regexpr\" value=\"");
      out.print(request.getParameter("regexpr")==null?"":request.getParameter("regexpr"));
      out.write("\" align=\"absmiddle\">&nbsp;");
      out.write("<input type=\"submit\" value=\"Get\">\r\n                            ");
      out.write("</td>\r\n                        ");
      out.write("</tr>\r\n                        ");
      out.write("<tr>\r\n                            ");
      out.write("<td align=\"right\">\r\n                                ");
      out.write("<font size=\"-2\">(");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/help/regexpr.jsp\">about java reg.expr.");
      out.write("</a>)");
      out.write("</font>&nbsp;\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td nowrap colspan=\"2\">\r\n                                Start at match:&nbsp; ");
      out.write("<input size=\"4\" name=\"linesToSkip\" value=\"");
      out.print(linesToSkip);
      out.write("\">\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td nowrap colspan=\"2\" width=\"100%\">\r\n                                &nbsp;&nbsp;Show matches:&nbsp;");
      out.write("<input size=\"4\" name=\"linesToShow\" value=\"");
      out.print(linesToShow);
      out.write("\"> (0 = all)\r\n                            ");
      out.write("</td>\r\n                        ");
      out.write("</tr>\r\n                        ");
      out.write("<tr>\r\n                            ");
      out.write("<td>");
      out.write("</td>\r\n                            ");
      out.write("<td nowrap>\r\n                                ");
      out.write("<input name=\"ln\" value=\"true\" type=\"checkbox\" ");
      out.print(request.getParameter("ln")!=null&&request.getParameter("ln").equalsIgnoreCase("true")?"checked":"");
      out.write(">");
      out.write("<input type=\"hidden\" name=\"linesToShow\" value=\"");
      out.print(linesToShow);
      out.write("\">\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td nowrap>\r\n                                &nbsp;Line numbers&nbsp;&nbsp;\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td nowrap>\r\n                                &nbsp;");
      out.write("<input name=\"grep\" value=\"true\" type=\"checkbox\" ");
      out.print(request.getParameter("grep")!=null&&request.getParameter("grep").equalsIgnoreCase("true")?"checked":"");
      out.write(">\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td width=\"100%\">\r\n                                &nbsp;Grep style&nbsp;&nbsp;\r\n                            ");
      out.write("</td>\r\n                        ");
      out.write("</tr>\r\n                        ");
      out.write("<tr>\r\n                            ");
      out.write("<td>\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td nowrap>\r\n                                ");
      out.write("<input name=\"indent\" value=\"true\" type=\"checkbox\" ");
      out.print(request.getParameter("indent")!=null&&request.getParameter("indent").equalsIgnoreCase("true")?"checked":"");
      out.write(">\r\n                            ");
      out.write("</td>\r\n                            ");
      out.write("<td width=\"100%\" colspan=\"3\">\r\n                                &nbsp;Include following indented lines&nbsp;&nbsp;\r\n                            ");
      out.write("</td>\r\n                        ");
      out.write("</tr>\r\n                    ");
      out.write("</table>\r\n                ");
 } 
      out.write("\r\n                ");
      out.write("</td>\r\n            ");
      out.write("</tr>\r\n        ");
      out.write("</table>\r\n    ");
      out.write("</form>\r\n    ");
      out.write("<p>\r\n    ");
      out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n        ");
      out.write("<tr>\r\n            ");
      out.write("<td colspan=\"2\">");
      out.write("</td>\r\n            ");
      out.write("<td height=\"5\" colspan=\"3\">\r\n            ");
      out.write("</td>\r\n            ");
      out.write("<td colspan=\"2\">");
      out.write("</td>\r\n        ");
      out.write("</tr>\r\n        ");
      out.write("<tr>\r\n            ");
      out.write("<td rowspan=\"5\" width=\"3\" nowrap >\r\n            ");
      out.write("<td rowspan=\"5\" width=\"1\" nowrap bgcolor=\"#0c0c0c\">\r\n            ");
      out.write("</td>\r\n            ");
      out.write("<td height=\"1\" colspan=\"4\" bgcolor=\"#0c0c0c\">\r\n            ");
      out.write("</td>\r\n            ");
      out.write("<td rowspan=\"5\" width=\"1\" nowrap bgcolor=\"#0c0c0c\">\r\n            ");
      out.write("<td rowspan=\"5\" width=\"3\" nowrap >\r\n        ");
      out.write("</tr>\r\n        ");
      out.write("<tr>\r\n            ");
      out.write("<td colspan=\"2\">\r\n                &nbsp;");
      out.print(fileName);
      out.write(" for ");
      out.print(theJob.getJobName());
      out.write("\r\n            ");
      out.write("</td>\r\n            ");
      out.write("<td colspan=\"1\" align=\"right\">\r\n                ");
      out.print(logInfo);
      out.write("\r\n            ");
      out.write("</td>\r\n            ");
      out.write("<td>&nbsp;\r\n            ");
      out.write("</td>\r\n        ");
      out.write("</tr>\r\n        ");
      out.write("<tr>\r\n            ");
      out.write("<td height=\"1\" colspan=\"4\" bgcolor=\"#0c0c0c\">\r\n            ");
      out.write("</td>\r\n        ");
      out.write("</tr>\r\n        ");
      out.write("<tr>\r\n            ");
      out.write("<td colspan=\"4\" class=\"main\" width=\"400\" height=\"100\" valign=\"top\">\r\n                    ");
      out.write("<pre>");
 TextUtils.writeEscapedForHTML(logText,out); 
      out.write("</pre>\r\n            ");
      out.write("</td>\r\n        ");
      out.write("</tr>\r\n        ");
      out.write("<tr>\r\n            ");
      out.write("<td height=\"1\" colspan=\"4\" bgcolor=\"#0c0c0c\">\r\n            ");
      out.write("</td>\r\n        ");
      out.write("</tr>\r\n        ");
      out.write("<tr>\r\n            ");
      out.write("<td height=\"5\" colspan=\"4\">\r\n            ");
      out.write("</td>\r\n        ");
      out.write("</tr>\r\n    ");
      out.write("</table>\r\n\r\n");

    if (handler.getCurrentJob() != null &&
           handler.getCurrentJob().getStatus().equals(CrawlJob.STATUS_PAUSED)) {
        out.print("<a href=\"" + request.getContextPath() +
            "/console/action.jsp?action=rotateLogs\">Rotate crawler logs</a>");
    }

      out.write("\r\n\r\n");

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
