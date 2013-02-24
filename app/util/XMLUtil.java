package util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nu.xom.ValidityException;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import play.Logger;

import com.sun.mail.iap.ParsingException;


public class XMLUtil {

	public static List getElementsByTagName(File file, String tag) {
	   List elements = new ArrayList();
	   try {      
	      SAXBuilder bob = new SAXBuilder();
	      Document doc = bob.build(file);
	      List temp = doc.getRootElement().getChildren(tag);
	      if(temp!=null){
	         elements = temp;
	      }
	      
	      // ...
	    }
	    catch (IOException e) {
         Logger.error(e, e.getMessage());
      } catch (JDOMException e) {
         Logger.error(e, e.getMessage());
      }
	   return elements;
	}
	
	
}
