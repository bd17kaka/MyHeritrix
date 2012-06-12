package org.bd17kaka.crawler.extractor;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.archive.crawler.datamodel.CoreAttributeConstants;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.extractor.Extractor;
import org.archive.crawler.extractor.PDFParser;
import org.archive.crawler.framework.ToeThread;

public class ExtractorPic extends Extractor implements CoreAttributeConstants {

	private static final long serialVersionUID = 2719832447893965434L;

	private static Logger logger = Logger
			.getLogger("org.archive.crawler.extractor.ExtractorDOC");

	private static int numOfPic = 0;
	
	public ExtractorPic(String name) {
		super(name, "Pic Extractor. Extracts picture from all files");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void extract(CrawlURI curi) {

		if (!isHttpTransactionContentToProcess(curi)
				|| (!isExpectedMimeType(curi.getContentType(), "image/gif"))
				&& (!isExpectedMimeType(curi.getContentType(), "image/jpeg")) 
				&& (!isExpectedMimeType(curi.getContentType(), "image/png"))) {
			return;
		}
		//curi.getHttpRecorder().getRecordedInput();
		//String uri = curi.getCrawlURIString();
		
		String picName = "";
		File file = null;
		try {
			picName = curi.toString().substring(curi.toString().lastIndexOf('/') + 1);	
			file = new File("f:\\pic\\" + numOfPic + "_" + picName);
			
            curi.getHttpRecorder().getRecordedInput().
                copyContentBodyTo(file);
        } catch(Exception e){
        	System.out.println(e.toString());
        	return;
        }
		finally{
        	++numOfPic;
        }
		System.out.println("Extracts picture from uri : " + curi.toString());
	}
}
