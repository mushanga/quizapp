package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Util {

	public static boolean isListValid(List<?> list) {

		return isCollectionValid(list);

	}
	private static boolean isCollectionValid(Collection<?> coll) {

		return coll != null && coll.size() > 0;

	}
   public static boolean isSetValid(Set<?> set) {
      return isCollectionValid(set);

   }
   public static boolean isValid(Collection<?> col) {
      return isCollectionValid(col);

   }
   public static boolean isValid(Object[] col) {
      return col != null && col.length>0;

   }
	
	public static boolean isStringValid(String str) {

		return str != null && str.length() > 0;

	}
   
 public static String readFileAsString(String filePath) throws java.io.IOException
 {
     BufferedReader reader = new BufferedReader(new FileReader(filePath));
     String line, results = "";
     while( ( line = reader.readLine() ) != null)
     {
         results += "\n"+line;
     }
     reader.close();
     return results;
 }   
 public static List<String> readFileAsLineList(String filePath) throws java.io.IOException
 {
     BufferedReader reader = new BufferedReader(new FileReader(filePath));
     String line;
     List<String> results = new ArrayList<String>();
     while( ( line = reader.readLine() ) != null)
     {
        results.add(line);
     }
     reader.close();
     return results;
 }

   public static String getIdListAsCommaSeparatedString(List<Long> idList){
      String idListStr = "";
      for(int i= 0;i <idList.size(); i++){
         if(i!=0){
            idListStr = idListStr +",";
         }
         idListStr = idListStr + String.valueOf(idList.get(i) );
      }
      return idListStr;
   }
   public static List<String> parse(String sourceString, String prefix, String suffix, String contentStartString, String contentEndString, String itemSeparator, String itemSplitter, boolean removeTags) {

      String[] returnArr = new String[0];
      String returnValue = "";
      if (contentStartString != null && contentStartString != "") {
         String[] result = sourceString.split(contentStartString);

         if (result.length > 1) {
            sourceString = result[1];
         }
      }
      if (contentEndString != null && contentEndString != "") {
         String[] result = sourceString.split(contentEndString);

         if (result.length > 1) {
            sourceString = result[0];
         }
      }

      ArrayList<String> retList = new ArrayList<String>();
      if (isStringValid(itemSeparator)) {
         String[] itemArr = sourceString.split(itemSeparator);
         for (int i = 0; i < itemArr.length; i++) {
            if (i == 0)
               continue;

            String value = null;
            if(prefix!=null){
               value = getBetween(itemArr[i], prefix, suffix, removeTags);
            }else{
               value = itemArr[i];
            }
            if (isStringValid(value)) {

               retList.add(value);
            }
         }
      } else {
         retList.add(getBetween(sourceString, prefix, suffix, removeTags));
      }

      return retList;
   }
   public static String getBetween(String stringToParse, String prefix, String suffix, boolean removeTags) {

      String temp1 = getBetweenBasic(stringToParse, prefix, suffix);
      String temp2 = "";
      if(removeTags){
         temp2= removeTags(temp1);
      }else{
         temp2 =temp1;
               
      }
       
      temp2 = temp2.trim();

      return temp2;
   }
   public static String removeTags(String text) {
      int indexOfAB1;
      int indexOfAB2;
      String editText = new String(text);
      boolean tagFound = true;

      while (tagFound) {
         indexOfAB1 = editText.indexOf("<");
         indexOfAB2 = editText.indexOf(">");

         if (indexOfAB1 < 0 && indexOfAB2 >= 0 && editText.length() - 1 > indexOfAB2 || indexOfAB1 > indexOfAB2 && indexOfAB2 >= 0) {

            editText = editText.substring(indexOfAB2 + 1);
            tagFound = true;

         } else if (indexOfAB1 >= 0 && indexOfAB1 < indexOfAB2) {
            tagFound = true;
            editText = editText.substring(0, indexOfAB1) + editText.substring(indexOfAB2 + 1);
         } else {
            tagFound = false;
         }
      }
      return editText;
   }
   public static String getBetweenBasic(String stringToParse, String prefix, String suffix) {

      String temp1 = "";

      if (stringToParse.split(prefix).length > 1) {
         temp1 = stringToParse.split(prefix)[1].split(suffix)[0];
      }

      return temp1;
   }
	
}
