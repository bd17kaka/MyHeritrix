package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.apache.jasper.runtime.*;
import org.archive.crawler.admin.CrawlJobHandler;
import org.archive.crawler.admin.CrawlJob;
import org.archive.crawler.Heritrix;
import org.archive.crawler.framework.CrawlController;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;
import org.archive.util.TextUtils;
import org.archive.crawler.settings.ModuleAttributeInfo;
import org.archive.crawler.settings.ModuleAttributeInfo;
import org.archive.crawler.settings.ModuleType;
import org.archive.crawler.settings.ComplexType;
import org.archive.crawler.settings.CrawlerSettings;
import org.archive.crawler.settings.MapType;
import org.archive.crawler.framework.Processor;
import org.archive.crawler.framework.CrawlScope;
import org.archive.crawler.framework.Frontier;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import org.archive.crawler.admin.CrawlJob;
import org.archive.crawler.admin.ui.JobConfigureUtils;
import org.archive.crawler.datamodel.CrawlOrder;
import org.archive.crawler.settings.*;
import org.archive.crawler.framework.CrawlController;
import org.archive.crawler.framework.Frontier;
import org.archive.crawler.framework.CrawlScope;
import org.archive.crawler.framework.Processor;
import org.archive.crawler.framework.StatisticsTracking;
import org.archive.util.TextUtils;
import java.io.*;
import java.lang.Boolean;
import java.util.ArrayList;
import javax.management.MBeanInfo;
import javax.management.Attribute;
import javax.management.MBeanAttributeInfo;
import javax.management.AttributeNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import java.text.SimpleDateFormat;
import org.archive.crawler.Heritrix;
import org.archive.crawler.admin.CrawlJob;
import org.archive.util.ArchiveUtils;
import org.archive.util.TextUtils;
import org.archive.crawler.admin.StatisticsTracker;
import org.archive.crawler.Heritrix;
import java.util.Map;
import java.util.Iterator;

public class modules_jsp extends HttpJspBase {


    /**
     * Generates the HTML code to display and allow manipulation of all
     * MapTypes which include ModuleTypes with multiple options (except
     * Processors). 
     *
     * Will work it's way recursively down the crawlorder.
     *
     * @param mbean The ComplexType representing the crawl order or one
     * of it's subcomponents.
     * @param inMap
     * @param parent The absolute name of the ComplexType that contains the
     * current ComplexType (i.e. parent).
     * @return The variable part of the HTML code for selecting filters.
     * @throws Exception
     */
    public static String printAllMaps(ComplexType mbean,
            CrawlerSettings settings, boolean inMap,
            boolean edittable, String parent) throws Exception {
        if (mbean.isTransient()) {
            return "";
        }
        MBeanInfo info = mbean.getMBeanInfo(settings);
        MBeanAttributeInfo a[] = info.getAttributes();
        StringBuffer p = new StringBuffer();

        boolean subMap = false;
        boolean processorsMap = false;
        MapType thisMap = null;
        List availableOptions = Collections.EMPTY_LIST;
        if (mbean instanceof MapType) {
            thisMap = (MapType)mbean;
            if (thisMap.getContentType() != Processor.class) {
                // only if a maptype, with moduletype entries, with
                // multiple options, and not Processor, will this
                // get map treatment
                subMap = true;
            } else {
                processorsMap = true;
            }
            if (ModuleType.class.isAssignableFrom(thisMap.getContentType())) {
                availableOptions = getOptionsForType(thisMap.getContentType());
            }
            if (availableOptions.size() == 0) {
                subMap = false;
            }
        }

        String description = TextUtils.escapeForMarkupAttribute(mbean
                .getDescription());
        if (inMap) {
            p.append("<tr><td>" + mbean.getName() + "</td>");
            if(edittable) {
	            p.append("<td nowrap><a href=\"javascript:doMoveUp('"
	                    + mbean.getName() + "','" + parent
	                    + "')\">Up</a></td>");
	            p.append("<td nowrap><a href=\"javascript:doMoveDown('"
	                    + mbean.getName() + "','" + parent
	                    + "')\">Down</a></td> ");
	            p.append("<td><a href=\"javascript:doRemove('" + mbean.getName()
	                    + "','" + parent + "')\">Remove</a></td>");
	        } else {
	            p.append("<td colspan=\"3\">(inherited)</td>");
	        }
            p.append("<td title='" + description + "'>");
            p.append("<i><font size=\"-1\">" + mbean.getClass().getName() +
                    "</font></i>");
            p.append("&nbsp;<a href='javascript:alert(\"" + description
                    + "\")'>?</a>");
            p.append("</td></tr>\n");
        } else {
            p.append("<div class='SettingsBlock'>\n");
            p.append("<b title='" + description + "'>" + mbean.getName());
            p.append("</b>\n");
            Class type = mbean.getLegalValueType();
            if (CrawlScope.class.isAssignableFrom(type)
                    || Frontier.class.isAssignableFrom(type) || processorsMap) {
                p.append("<font size=\"-1\">" + description + "To change "
                        + mbean.getName() + ", go to the "
                        + "<i>Modules</i> tab.</font>");
            }
            p.append("<br/>\n");
        }

        if (subMap) {
            p.append("<table>\n");
        }

        for (int n = 0; n < a.length; n++) {
            if (a[n] == null) {
                p.append("  ERROR: null attribute");
            } else {
                Object currentAttribute = null;
                Object localAttribute = null;
                //The attributes of the current attribute.
                ModuleAttributeInfo att = (ModuleAttributeInfo)a[n];
                try {
                    currentAttribute = mbean.getAttribute(settings, att.getName());
                    localAttribute = mbean.getLocalAttribute(settings, att.getName());
                } catch (Exception e1) {
                    String error = e1.toString() + " " + e1.getMessage();
                    return error;
                }
                boolean locallyEdittable = (localAttribute != null);
                if (currentAttribute instanceof ComplexType) {
                    if (inMap) {
                        p.append("<tr><td colspan='5'>");
                    }
                    p.append(printAllMaps((ComplexType)currentAttribute,
                            settings, subMap, locallyEdittable,
                            mbean.getAbsoluteName()));
                    if (inMap) {
                        p.append("</td></tr>\n");
                    }
                }
            }
        }
        if (subMap) {
            p.append("</table>\n");
        }

        if (subMap) {
            // ordered list of options; append add controls
            if (availableOptions != null) {
                p.append("Name: <input size='8' name='"
                        + mbean.getAbsoluteName() + ".name' id='"
                        + mbean.getAbsoluteName() + ".name'>\n");
                p.append("Type: <select name='" + mbean.getAbsoluteName()
                        + ".class'>\n");
                for (int i = 0; i < availableOptions.size(); i++) {
                    p.append("<option value='" + availableOptions.get(i) + "'>"
                            + availableOptions.get(i) + "</option>\n");
                }
                p.append("</select>\n");
                p.append("<input type='button' value='Add'"
                        + " onClick=\"doAdd('" + mbean.getAbsoluteName()
                        + "')\">\n");
                p.append("<br/>");
            }
        }

        if (!inMap) {
            p.append("\n</div>\n");
        }
        return p.toString();
    }
    
    private static List getOptionsForType(Class type) {
        String typeName = type.getName();
        String simpleName = typeName.substring(typeName.lastIndexOf(".")+1);
        String optionsFilename = simpleName+".options";
        try {
            return CrawlJobHandler.loadOptions(optionsFilename);
        } catch (IOException e) {
            return new ArrayList();
        }
    }

    /**
     * Builds the HTML for selecting an implementation of a specific crawler module
     *
     * MOVED FROM webapps/admin/jobs/modules.jsp
     * @param module The MBeanAttributeInfo on the currently set module
     * @param availibleOptions A list of the availibe implementations (full class names as Strings)
     * @param name The name of the module
     *
     * @return the HTML for selecting an implementation of a specific crawler module
     */
    public static String buildModuleSetter(MBeanAttributeInfo module, Class allowableType, String name, String currentDescription){
        StringBuffer ret = new StringBuffer();

        List availableOptions = getOptionsForType(allowableType);

        ret.append("<table><tr><td>&nbsp;Current selection:</td><td>");
        ret.append(module.getType());
        ret.append("</td><td></td></tr>");
        ret.append("<tr><td></td><td width='100' colspan='2'><i>" + currentDescription + "</i></td>");

        if(availableOptions.size()>0){
            ret.append("<tr><td>&nbsp;Available alternatives:</td><td>");
            ret.append("<select name='cbo" + name + "'>");
            for(int i=0 ; i<availableOptions.size() ; i++){
                ret.append("<option value='"+availableOptions.get(i)+"'>");
                ret.append(availableOptions.get(i)+"</option>");
            }
            ret.append("</select>");
            ret.append("</td><td>");
            ret.append("<input type='button' value='Change' onClick='doSetModule(\"" + name + "\")'>");
            ret.append("</td></tr>");
        }
        ret.append("</table>");
        return ret.toString();
    }

    /**
     *
     * Builds the HTML to edit a map of modules
     *
     * MOVED FROM webapps/admin/jobs/modules.jsp
     * @param map The map to edit
     * @param allowableType
     * @param name A short name for the map (only alphanumeric chars.)
     *
     * @return the HTML to edit the specified modules map
     */
    public static String buildModuleMap(ComplexType map,
            Class allowableType, String name){
        StringBuffer ret = new StringBuffer();

        List availableOptions = getOptionsForType(allowableType);

        ret.append("<table cellspacing='0' cellpadding='2'>");

        ArrayList unusedOptions = new ArrayList();
        MBeanInfo mapInfo = map.getMBeanInfo();
        MBeanAttributeInfo m[] = mapInfo.getAttributes();

        // Printout modules in map.
        boolean alt = false;
        for(int n=0; n<m.length; n++) {
            ModuleAttributeInfo att = (ModuleAttributeInfo)m[n]; //The attributes of the current attribute

            ret.append("<tr");
            if(alt){
                ret.append(" bgcolor='#EEEEFF'");
            }
            ret.append("><td>&nbsp;"+att.getType()+"</td>");
            if(n!=0){
                ret.append("<td><a href=\"javascript:doMoveMapItemUp('" + name + "','"+att.getName()+"')\">Up</a></td>");
            } else {
                ret.append("<td></td>");
            }
            if(n!=m.length-1){
                ret.append("<td><a href=\"javascript:doMoveMapItemDown('" + name + "','"+att.getName()+"')\">Down</a></td>");
            } else {
                ret.append("<td></td>");
            }
            ret.append("<td><a href=\"javascript:doRemoveMapItem('" + name + "','"+att.getName()+"')\">Remove</a></td>");
            ret.append("<td><a href=\"javascript:alert('");
            ret.append(TextUtils.escapeForMarkupAttribute(att.getDescription()));
            ret.append("')\">Info</a></td>\n");
            ret.append("</tr>");
            alt = !alt;
        }

        // Find out which aren't being used.
        for(int i=0 ; i<availableOptions.size() ; i++){
            boolean isIncluded = false;

            for(int n=0; n<m.length; n++) {
                ModuleAttributeInfo att = (ModuleAttributeInfo)m[n]; //The attributes of the current attribute.

                try {
                    map.getAttribute(att.getName());
                } catch (Exception e1) {
                    ret.append(e1.toString() + " " + e1.getMessage());
                }
                String typeAndName = att.getType()+"|"+att.getName();
                if(typeAndName.equals(availableOptions.get(i))){
                    //Found it
                    isIncluded = true;
                    break;
                }
            }
            if(isIncluded == false){
                // Yep the current one is unused.
                unusedOptions.add(availableOptions.get(i));
            }
        }
        if(unusedOptions.size() > 0 ){
            ret.append("<tr><td>");
            ret.append("<select name='cboAdd" + name + "'>");
            for(int i=0 ; i<unusedOptions.size() ; i++){
                String curr = (String)unusedOptions.get(i);
                int index = curr.indexOf("|");
                if (index < 0) {
                    throw new RuntimeException("Failed to find '|' required" +
                        " divider in : " + curr + ". Repair modules file.");

                }
                ret.append("<option value='" + curr + "'>" +
                    curr.substring(0, index) + "</option>");
            }
            ret.append("</select>");
            ret.append("</td><td>");
            ret.append("<input type='button' value='Add' onClick=\"doAddMapItem('" + name + "')\">");
            ret.append("</td></tr>");
        }
        ret.append("</table>");
        return ret.toString();
    }


  private static java.util.Vector _jspx_includes;

  static {
    _jspx_includes = new java.util.Vector(7);
    _jspx_includes.add("/include/handler.jsp");
    _jspx_includes.add("/include/modules.jsp");
    _jspx_includes.add("/include/head.jsp");
    _jspx_includes.add("/include/stats.jsp");
    _jspx_includes.add("/include/jobnav.jsp");
    _jspx_includes.add("/include/jobnav.jsp");
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
      out.write("\r\n\r\n");
      out.write("\r\n");
      out.write("\r\n\r\n");

    // Get the default settings.
    
    CrawlJob theJob = handler.getJob(request.getParameter("job"));
    
    
    if(theJob == null)
    {
        // Didn't find any job with the given UID or no UID given.
        response.sendRedirect(request.getContextPath() +
            "/jobs.jsp?message=No job selected");
        return;
    } else if(theJob.isReadOnly() || theJob.isRunning()){
        // Can't edit this job.
        response.sendRedirect(request.getContextPath() +
            "/jobs.jsp?message=Can't edit modules on a running or read only" +
            " job");
        return;
    }

    XMLSettingsHandler settingsHandler = (XMLSettingsHandler)theJob.getSettingsHandler();

    if(request.getParameter("action") != null){
        // Need to take some action.
        String action = request.getParameter("action");
        if(action.equals("done")){
            // Ok, done editing.
            if(theJob.isNew()){            
                handler.addJob(theJob);
                response.sendRedirect(request.getContextPath() +
                    "/jobs.jsp?message=Job created");
            }else{
                if(theJob.isRunning()){
                    handler.kickUpdate();
                }
                if(theJob.isProfile()){
                    response.sendRedirect(request.getContextPath() +
                        "/profiles.jsp?message=Profile modified");
                }else{
                    response.sendRedirect(request.getContextPath() +
                        "/jobs.jsp?message=Job modified");
                }
            }
            return;
        }else if(action.equals("goto")){
            // Goto another page of the job/profile settings
            response.sendRedirect(request.getParameter("subaction"));
            return;
        }else if(action.equals("module")){
            // Setting a module
            String item = request.getParameter("item");
            String className = request.getParameter("cbo"+item);
                        
            ModuleType tmp = null;
            if(item.equals("Frontier")){
                // Changing URI frontier
                tmp = SettingsHandler.instantiateModuleTypeFromClassName("frontier",className);
            } else if(item.equals("Scope")){
                // Changing Scope
                tmp = SettingsHandler.instantiateModuleTypeFromClassName(org.archive.crawler.framework.CrawlScope.ATTR_NAME,className);
            } 
            if(tmp != null){
                // If tmp is null then something went wrong but we'll ignore it.
                settingsHandler.getOrder().setAttribute(tmp);
                // Write changes
                settingsHandler.writeSettingsObject(settingsHandler.getSettings(null));
            }
        }else if(action.equals("map")){
            //Doing something with a map
            String subaction = request.getParameter("subaction");
            String item = request.getParameter("item");
            if(subaction != null && item != null){
                // Do common stuff
                String subitem = request.getParameter("subitem");
                MapType map = null;
                if(item.equals("PreFetchProcessors")){
                    // Editing processors map
                    map = ((MapType)settingsHandler.getOrder().getAttribute(CrawlOrder.ATTR_PRE_FETCH_PROCESSORS));
                } else if(item.equals("Fetchers")){
                    // Editing processors map
                    map = ((MapType)settingsHandler.getOrder().getAttribute(CrawlOrder.ATTR_FETCH_PROCESSORS));
                } else if(item.equals("Extractors")){
                    // Editing processors map
                    map = ((MapType)settingsHandler.getOrder().getAttribute(CrawlOrder.ATTR_EXTRACT_PROCESSORS));
                } else if(item.equals("Writers")){
                    // Editing processors map
                    map = ((MapType)settingsHandler.getOrder().getAttribute(CrawlOrder.ATTR_WRITE_PROCESSORS));
                } else if(item.equals("Postprocessors")){
                    // Editing processors map
                    map = ((MapType)settingsHandler.getOrder().getAttribute(CrawlOrder.ATTR_POST_PROCESSORS));
                } else if(item.equals("StatisticsTracking")){
                    // Editing Statistics Tracking map
                    map = ((MapType)settingsHandler.getOrder().getAttribute("loggers"));
                }
                if(map != null){
                    // Figure out what to do
                    if(subaction.equals("up")){
                        // Move selected processor up
                        map.moveElementUp(settingsHandler.getSettings(null),subitem);
                    }else if(subaction.equals("down")){
                        // Move selected processor down            
                        map.moveElementDown(settingsHandler.getSettings(null),subitem);
                    }else if(subaction.equals("remove")){
                        // Remove selected processor
                        map.removeElement(settingsHandler.getSettings(null),subitem);
                    }else if(subaction.equals("add")){
                        String className = request.getParameter("cboAdd"+item);
                        String typeName = className.substring(className.indexOf("|")+1);
                        className = className.substring(0,className.indexOf("|"));
    
                        map.addElement(settingsHandler.getSettings(null),
                                         SettingsHandler.instantiateModuleTypeFromClassName(typeName,className));
                    }
                    // Write changes
                    settingsHandler.writeSettingsObject(settingsHandler.getSettings(null));
                }
            }
        }        
    }

    String title = "Adjust modules";
    int tab = theJob.isProfile()?2:1;
    int jobtab = 0;

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
      out.write("\r\n");
      out.write("<script type=\"text/javascript\">\r\n    function doSubmit(){\r\n        document.frmModules.submit();\r\n    }\r\n    \r\n    function doGoto(where){\r\n        document.frmModules.action.value=\"goto\";\r\n        document.frmModules.subaction.value = where;\r\n        doSubmit();\r\n    }\r\n    \r\n    function doSetModule(name){\r\n        document.frmModules.action.value=\"module\";\r\n        document.frmModules.item.value=name;\r\n        doSubmit();\r\n    }\r\n    \r\n    function doMoveMapItemUp(name, item){\r\n        document.frmModules.action.value=\"map\";\r\n        document.frmModules.subaction.value=\"up\";\r\n        document.frmModules.item.value=name;\r\n        document.frmModules.subitem.value=item;\r\n        doSubmit();\r\n    }\r\n\r\n    function doMoveMapItemDown(name, item){\r\n        document.frmModules.action.value=\"map\";\r\n        document.frmModules.subaction.value=\"down\";\r\n        document.frmModules.item.value=name;\r\n        document.frmModules.subitem.value=item;\r\n        doSubmit();\r\n    }\r\n    \r\n    function doRemoveMapItem(name, item){\r\n");
      out.write("        document.frmModules.action.value=\"map\";\r\n        document.frmModules.subaction.value=\"remove\";\r\n        document.frmModules.item.value=name;\r\n        document.frmModules.subitem.value=item;\r\n        doSubmit();\r\n    }\r\n    \r\n    function doAddMapItem(name){\r\n        document.frmModules.action.value=\"map\";\r\n        document.frmModules.subaction.value=\"add\";\r\n        document.frmModules.item.value=name;\r\n        doSubmit();\r\n    }\r\n");
      out.write("</script>\r\n\r\n    ");
      out.write("<p>\r\n        ");

    /**
     * An include file that handles the sub navigation of a 'job' page. 
     * Include where the sub navigation should be displayed.
     *
     * The following variables must exist prior to this file being included:
     *
     * String theJob - The CrawlJob being manipulated.
     * int jobtab - Which to display as 'selected'.
     *          0 - Modules
     *          SUPERCEDED BY SUBMODULES 1 - Filters
     *          2 - Settings
     *          3 - Overrides
     *          SUPERCEDED BY SUBMODULES 4 - Credentials
     *          5 - Refinements
     *          SUPERCEDED BY SUBMODULES 6 - URL (Canonicalization)
     *          7 - Submodules 
     *
     * @author Kristinn Sigurdsson
     */

      out.write("\r\n    ");
      out.write("<table cellspacing=\"0\" cellpadding=\"0\">\r\n        ");
      out.write("<tr>\r\n            ");
      out.write("<td bgcolor=\"#0000FF\" height=\"1\">\r\n            ");
      out.write("</td>\r\n        ");
      out.write("</tr>\r\n        ");
      out.write("<tr>\r\n            ");
      out.write("<td>\r\n                ");
      out.write("<table cellspacing=\"0\" cellpadding=\"0\">\r\n                    ");
      out.write("<tr>\r\n                        ");
      out.write("<td nowrap>\r\n                            ");
      out.write("<b>");
      out.print(theJob.isProfile()?"Profile":"Job");
      out.write(" ");
      out.print(theJob.getJobName());
      out.write(":");
      out.write("</b>\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td class=\"tab_seperator\">\r\n                        ");
      out.write("</td>\r\n                        ");
 if(theJob.isRunning()){ 
      out.write("\r\n                            ");
      out.write("<td class=\"tab_inactive\" nowrap>\r\n                                ");
      out.write("<a href=\"javascript:alert('Can not edit modules on running jobs!')\" class=\"tab_text_inactive\">Modules");
      out.write("</a>\r\n                            ");
      out.write("</td>\r\n                        ");
 } else { 
      out.write("\r\n                            ");
      out.write("<td class=\"tab");
      out.print(jobtab==0?"_selected":"");
      out.write("\" nowrap>\r\n                                ");
      out.write("<a href=\"javascript:doGoto('");
      out.print(request.getContextPath());
      out.write("/jobs/modules.jsp?job=");
      out.print(theJob.getUID());
      out.write("')\" class=\"tab_text");
      out.print(jobtab==0?"_selected":"");
      out.write("\">Modules");
      out.write("</a>\r\n                            ");
      out.write("</td>\r\n                        ");
 } 
      out.write("\r\n                        ");
      out.write("<td class=\"tab_seperator\">\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td class=\"tab");
      out.print(jobtab==7?"_selected":"");
      out.write("\" nowrap>\r\n                            ");
      out.write("<a href=\"javascript:doGoto('");
      out.print(request.getContextPath());
      out.write("/jobs/submodules.jsp?job=");
      out.print(theJob.getUID());
      out.write("')\" class=\"tab_text");
      out.print(jobtab==7?"_selected":"");
      out.write("\">Submodules");
      out.write("</a>\r\n                        ");
      out.write("<td class=\"tab_seperator\">\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td class=\"tab");
      out.print(jobtab==2?"_selected":"");
      out.write("\" nowrap>\r\n                            ");
      out.write("<a href=\"javascript:doGoto('");
      out.print(request.getContextPath());
      out.write("/jobs/configure.jsp?job=");
      out.print(theJob.getUID());
      out.write("')\" class=\"tab_text");
      out.print(jobtab==2?"_selected":"");
      out.write("\">Settings");
      out.write("</a>\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td class=\"tab_seperator\">\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td class=\"tab");
      out.print(jobtab==3?"_selected":"");
      out.write("\" nowrap>\r\n                            ");
      out.write("<a href=\"javascript:doGoto('");
      out.print(request.getContextPath());
      out.write("/jobs/per/overview.jsp?job=");
      out.print(theJob.getUID());
      out.write("')\" class=\"tab_text");
      out.print(jobtab==3?"_selected":"");
      out.write("\">Overrides");
      out.write("</a>\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td class=\"tab_seperator\">\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td class=\"tab");
      out.print(jobtab==5?"_selected":"");
      out.write("\" nowrap>\r\n                            ");
      out.write("<a href=\"javascript:doGoto('");
      out.print(request.getContextPath());
      out.write("/jobs/refinements/overview.jsp?job=");
      out.print(theJob.getUID());
      out.write("')\" class=\"tab_text");
      out.print(jobtab==5?"_selected":"");
      out.write("\">Refinements");
      out.write("</a>\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td class=\"tab_seperator\">\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td class=\"tab\">\r\n                            ");
      out.write("<a href=\"javascript:doSubmit()\" class=\"tab_text\">");
      out.print(theJob.isNew()?"Submit job":"Finished");
      out.write("</a>\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td class=\"tab_seperator\">\r\n                        ");
      out.write("</td>\r\n                    ");
      out.write("</tr>\r\n                ");
      out.write("</table>\r\n            ");
      out.write("</td>\r\n        ");
      out.write("</tr>\r\n        ");
      out.write("<tr>\r\n            ");
      out.write("<td bgcolor=\"#0000FF\" height=\"1\">\r\n            ");
      out.write("</td>\r\n        ");
      out.write("</tr>\r\n    ");
      out.write("</table>\r\n");
      out.write("\r\n    ");
      out.write("<form name=\"frmModules\" method=\"post\" action=\"modules.jsp\">\r\n        ");
      out.write("<input type=\"hidden\" name=\"job\" value=\"");
      out.print(theJob.getUID());
      out.write("\">\r\n        ");
      out.write("<input type=\"hidden\" name=\"action\" value=\"done\">\r\n        ");
      out.write("<input type=\"hidden\" name=\"subaction\" value=\"done\">\r\n        ");
      out.write("<input type=\"hidden\" name=\"item\" value=\"\">\r\n        ");
      out.write("<input type=\"hidden\" name=\"subitem\" value=\"\">\r\n        \r\n       ");
      out.write("<p>");
      out.write("<b>Select Modules and Add/Remove/Order Processors");
      out.write("</b>\r\n       ");
      out.write("</p>\r\n       ");
      out.write("<p>Use this page to choose the main modules Heritrix should\r\n       using crawling and to add/remove/order processors in each step\r\n       of the processing chain.  Go to the\r\n        ");
      out.write("<a href=\"javascript:doGoto('");
      out.print(request.getContextPath());
      out.write("/jobs/configure.jsp?job=");
      out.print(theJob.getUID());
      out.write("')\">Settings");
      out.write("</a>\r\n        page to complete configuration of chosen modules \r\n        and procesors.");
      out.write("</p>\r\n        ");
      out.write("<p>\r\n            ");
      out.write("<b>Select Crawl Scope");
      out.write("</b>\r\n        ");
      out.write("<p>\r\n            ");
      out.print(buildModuleSetter(
                settingsHandler.getOrder().getAttributeInfo("scope"),
                CrawlScope.class,
                "Scope",
                ((ComplexType)settingsHandler.getOrder().getAttribute("scope")).getMBeanInfo().getDescription()));
      out.write("\r\n                \r\n        ");
      out.write("<p>\r\n            ");
      out.write("<b>Select URI Frontier");
      out.write("</b>\r\n        ");
      out.write("<p>\r\n            ");
      out.print(buildModuleSetter(
                settingsHandler.getOrder().getAttributeInfo("frontier"),
                Frontier.class,
                "Frontier",
                ((ComplexType)settingsHandler.getOrder().getAttribute("frontier")).getMBeanInfo().getDescription()));
      out.write("\r\n\r\n    \r\n        ");
      out.write("<p>\r\n            ");
      out.write("<b>Select Pre Processors");
      out.write("</b> \r\n            ");
      out.write("<i>Processors that should run before any fetching");
      out.write("</i>\r\n        ");
      out.write("<p>\r\n            ");
      out.print(buildModuleMap(
                (ComplexType)settingsHandler.getOrder().getAttribute(
                    CrawlOrder.ATTR_PRE_FETCH_PROCESSORS),
                    Processor.class,
                    "PreFetchProcessors"));
      out.write("\r\n        ");
      out.write("<p>\r\n            ");
      out.write("<b>Select Fetchers");
      out.write("</b> \r\n            ");
      out.write("<i>Processors that fetch documents using various protocols");
      out.write("</i>\r\n        ");
      out.write("<p>\r\n            ");
      out.print(buildModuleMap(
                (ComplexType)settingsHandler.getOrder().getAttribute(
                    CrawlOrder.ATTR_FETCH_PROCESSORS),
                    Processor.class,
                    "Fetchers"));
      out.write("\r\n        ");
      out.write("<p>\r\n            ");
      out.write("<b>Select Extractors");
      out.write("</b> \r\n            ");
      out.write("<i>Processors that extracts links from URIs");
      out.write("</i>\r\n        ");
      out.write("<p>\r\n            ");
      out.print(buildModuleMap(
                (ComplexType)settingsHandler.getOrder().getAttribute(
                    CrawlOrder.ATTR_EXTRACT_PROCESSORS),
                    Processor.class,
                    "Extractors"));
      out.write("\r\n        ");
      out.write("<p>\r\n            ");
      out.write("<b>Select Writers");
      out.write("</b> \r\n            ");
      out.write("<i>Processors that write documents to archive files");
      out.write("</i>\r\n        ");
      out.write("<p>\r\n            ");
      out.print(buildModuleMap(
                (ComplexType)settingsHandler.getOrder().getAttribute(
                    CrawlOrder.ATTR_WRITE_PROCESSORS),
                    Processor.class,
                    "Writers"));
      out.write("\r\n        ");
      out.write("<p>\r\n            ");
      out.write("<b>Select Post Processors");
      out.write("</b> \r\n            ");
      out.write("<i>Processors that do cleanup and feed the Frontier with new URIs");
      out.write("</i>\r\n        ");
      out.write("<p>\r\n            ");
      out.print(buildModuleMap(
                (ComplexType)settingsHandler.getOrder().getAttribute(
                    CrawlOrder.ATTR_POST_PROCESSORS),
                    Processor.class,
                    "Postprocessors")
            );
      out.write("\r\n        ");
      out.write("<p>\r\n            ");
      out.write("<b>Select Statistics Tracking");
      out.write("</b>\r\n        ");
      out.write("<p>\r\n            ");
      out.print(buildModuleMap(
                (ComplexType)settingsHandler.getOrder().getAttribute("loggers"),
                StatisticsTracking.class,
                "StatisticsTracking")
            );
      out.write("\r\n       ");
      out.write("</form>\r\n    ");
      out.write("<p>\r\n        ");

    /**
     * An include file that handles the sub navigation of a 'job' page. 
     * Include where the sub navigation should be displayed.
     *
     * The following variables must exist prior to this file being included:
     *
     * String theJob - The CrawlJob being manipulated.
     * int jobtab - Which to display as 'selected'.
     *          0 - Modules
     *          SUPERCEDED BY SUBMODULES 1 - Filters
     *          2 - Settings
     *          3 - Overrides
     *          SUPERCEDED BY SUBMODULES 4 - Credentials
     *          5 - Refinements
     *          SUPERCEDED BY SUBMODULES 6 - URL (Canonicalization)
     *          7 - Submodules 
     *
     * @author Kristinn Sigurdsson
     */

      out.write("\r\n    ");
      out.write("<table cellspacing=\"0\" cellpadding=\"0\">\r\n        ");
      out.write("<tr>\r\n            ");
      out.write("<td bgcolor=\"#0000FF\" height=\"1\">\r\n            ");
      out.write("</td>\r\n        ");
      out.write("</tr>\r\n        ");
      out.write("<tr>\r\n            ");
      out.write("<td>\r\n                ");
      out.write("<table cellspacing=\"0\" cellpadding=\"0\">\r\n                    ");
      out.write("<tr>\r\n                        ");
      out.write("<td nowrap>\r\n                            ");
      out.write("<b>");
      out.print(theJob.isProfile()?"Profile":"Job");
      out.write(" ");
      out.print(theJob.getJobName());
      out.write(":");
      out.write("</b>\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td class=\"tab_seperator\">\r\n                        ");
      out.write("</td>\r\n                        ");
 if(theJob.isRunning()){ 
      out.write("\r\n                            ");
      out.write("<td class=\"tab_inactive\" nowrap>\r\n                                ");
      out.write("<a href=\"javascript:alert('Can not edit modules on running jobs!')\" class=\"tab_text_inactive\">Modules");
      out.write("</a>\r\n                            ");
      out.write("</td>\r\n                        ");
 } else { 
      out.write("\r\n                            ");
      out.write("<td class=\"tab");
      out.print(jobtab==0?"_selected":"");
      out.write("\" nowrap>\r\n                                ");
      out.write("<a href=\"javascript:doGoto('");
      out.print(request.getContextPath());
      out.write("/jobs/modules.jsp?job=");
      out.print(theJob.getUID());
      out.write("')\" class=\"tab_text");
      out.print(jobtab==0?"_selected":"");
      out.write("\">Modules");
      out.write("</a>\r\n                            ");
      out.write("</td>\r\n                        ");
 } 
      out.write("\r\n                        ");
      out.write("<td class=\"tab_seperator\">\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td class=\"tab");
      out.print(jobtab==7?"_selected":"");
      out.write("\" nowrap>\r\n                            ");
      out.write("<a href=\"javascript:doGoto('");
      out.print(request.getContextPath());
      out.write("/jobs/submodules.jsp?job=");
      out.print(theJob.getUID());
      out.write("')\" class=\"tab_text");
      out.print(jobtab==7?"_selected":"");
      out.write("\">Submodules");
      out.write("</a>\r\n                        ");
      out.write("<td class=\"tab_seperator\">\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td class=\"tab");
      out.print(jobtab==2?"_selected":"");
      out.write("\" nowrap>\r\n                            ");
      out.write("<a href=\"javascript:doGoto('");
      out.print(request.getContextPath());
      out.write("/jobs/configure.jsp?job=");
      out.print(theJob.getUID());
      out.write("')\" class=\"tab_text");
      out.print(jobtab==2?"_selected":"");
      out.write("\">Settings");
      out.write("</a>\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td class=\"tab_seperator\">\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td class=\"tab");
      out.print(jobtab==3?"_selected":"");
      out.write("\" nowrap>\r\n                            ");
      out.write("<a href=\"javascript:doGoto('");
      out.print(request.getContextPath());
      out.write("/jobs/per/overview.jsp?job=");
      out.print(theJob.getUID());
      out.write("')\" class=\"tab_text");
      out.print(jobtab==3?"_selected":"");
      out.write("\">Overrides");
      out.write("</a>\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td class=\"tab_seperator\">\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td class=\"tab");
      out.print(jobtab==5?"_selected":"");
      out.write("\" nowrap>\r\n                            ");
      out.write("<a href=\"javascript:doGoto('");
      out.print(request.getContextPath());
      out.write("/jobs/refinements/overview.jsp?job=");
      out.print(theJob.getUID());
      out.write("')\" class=\"tab_text");
      out.print(jobtab==5?"_selected":"");
      out.write("\">Refinements");
      out.write("</a>\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td class=\"tab_seperator\">\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td class=\"tab\">\r\n                            ");
      out.write("<a href=\"javascript:doSubmit()\" class=\"tab_text\">");
      out.print(theJob.isNew()?"Submit job":"Finished");
      out.write("</a>\r\n                        ");
      out.write("</td>\r\n                        ");
      out.write("<td class=\"tab_seperator\">\r\n                        ");
      out.write("</td>\r\n                    ");
      out.write("</tr>\r\n                ");
      out.write("</table>\r\n            ");
      out.write("</td>\r\n        ");
      out.write("</tr>\r\n        ");
      out.write("<tr>\r\n            ");
      out.write("<td bgcolor=\"#0000FF\" height=\"1\">\r\n            ");
      out.write("</td>\r\n        ");
      out.write("</tr>\r\n    ");
      out.write("</table>\r\n");
      out.write("\r\n");

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
      out.write("\r\n\r\n\r\n");
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
