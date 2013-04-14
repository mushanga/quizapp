package jobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Answer;
import models.Category;
import models.Question;
import models.Quiz;

import org.apache.commons.io.FileUtils;
import org.jdom.Element;

import play.Logger;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import util.Util;
import util.XMLUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

@OnApplicationStart
public class Start extends Job {
   private static String QUIZ_PATH = null;
   private static final String FS = System.getProperty("file.separator");

   public void doJob() {
      createQuizFolder();

      File quizFolder= new File(getQuizPath());

      getDataFromFolder(quizFolder, null);


   }

   private static void getDataFromFolder(File folder, Category parent){
      try {
         folder = folder.getCanonicalFile();


         if(parent==null){

            String name = Util.readFileAsString(folder.getAbsolutePath()+FS+"name.txt");
            if(Util.isStringValid(name)){
               parent = new Category();
               parent.name=name;
               parent.save();
            }       
         }
      } catch (IOException e1) {
         Logger.error(e1, e1.getMessage());
      }
      Gson gson = new Gson();

      File[] fileNames = folder.listFiles();

      for(File file : fileNames){

         try {
            if(file.getName().endsWith(".json")){

               Quiz quiz = gson.fromJson(Util.readFileAsString(file.getAbsolutePath()), Quiz.class);
               quiz.parent = parent;
               parent.quizzes.add(quiz);   
               quiz.save();

            }else if(file.isDirectory()){
               String name = Util.readFileAsString(file.getAbsolutePath()+FS+"name.txt");
               if(Util.isStringValid(name)){
                  Category childGroup = new Category();
                  childGroup.name=name;
                  childGroup.parent = parent;
                  parent.children.add(childGroup);   
                  childGroup.save();
                  getDataFromFolder(file, childGroup);
               }
            }
         }catch (Exception e) {
            Logger.error(e, e.getMessage());
         }
      }
   }

   private static void createQuizFolder() {
      String quizPath = ".." + FS +"quizzes" + FS;
      File file = new File(quizPath);
      if (!file.exists()) {
         file.mkdirs();
      }
      QUIZ_PATH = quizPath;

   }

   private static void loadQuizzesFromTextFile(){
      try {
         List<String> lineList = Util.readFileAsLineList(QUIZ_PATH+"source.txt");
         //         List<String> quizzes = Util.parse(lineList, null, null, null, null, "// quest", null, false);
         //         
         List<Quiz> quizObjs = new ArrayList<Quiz>();
         String quizName = "";
         Quiz quizObj = null;
         Question question = null;
         for(String line : lineList){    


            if(line.startsWith("//")){
               String quizNameStr = line.split(":")[2].split(" ")[1];      
               if(!quizName.equals(quizNameStr)){
                  quizObj = new Quiz();
                  quizName = quizNameStr;
                  quizName = unescapeTxtDataFormat(quizName);
                  quizObj.title = quizName;
                  quizObjs.add(quizObj);
               }
            }else if(line.startsWith("::")){

               String questionText = line.split("::")[2];
               question = new Question();
               questionText = unescapeTxtDataFormat(questionText);
               question.text = questionText.substring(0, questionText.length()-1);
               quizObj.questions.add(question);
            }else if(line.startsWith("\t")){
               line = line.replaceFirst("\t", "");

               String answerText = "";

               if(line.startsWith("=")){
                  answerText = line.substring(1);
               }else{
                  try{
                     answerText = line.split("%")[2];   
                  }catch(Exception ex){
                     answerText = line.substring(1);
                  }

               }
               Answer answer = new Answer();
               answerText = unescapeTxtDataFormat(answerText);
               answer.text = answerText;
               question.answers.add(answer);
            }

         }
         writeQuizzesIntoFile(quizObjs);



      } catch (IOException e) {
         Logger.error(e, e.getMessage());
      }
   }
   private static void loadQuizzesFromXmlFile() throws IOException{
      File file = new File(QUIZ_PATH+"source.xml");
      List<Element> questions = XMLUtil.getElementsByTagName(file, "question");
      List<Quiz> quizObjs = new ArrayList<Quiz>();
      String quizName = "";
      Quiz quizObj = null;
      for(Element questionEle:questions){
         if(!questionEle.getAttribute("type").getValue().equals("category")){

            String quizNameStr = questionEle.getChild("name").getChildText("text").split(" ")[0].replace(":","").trim();

            if(!quizName.equals(quizNameStr)){
               quizObj = new Quiz();
               quizName = quizNameStr;
               quizObj.title = quizName;
               quizObjs.add(quizObj);
            }
            String questionText = questionEle.getChild("questiontext").getChildText("text");
            Question question = new Question();
            question.text = questionText.trim();
            question.type = questionEle.getAttribute("type").getValue().trim();


            quizObj.questions.add(question);

            List<Element> answers = questionEle.getChildren("answer");

            boolean singleCorrectAnswer = false;
            for(Element answerEle:answers){
               String fractionStr = answerEle.getAttribute("fraction").getValue();
               String answerText = answerEle.getChildText("text");

               Answer answer = new Answer();
               answer.fraction = Double.valueOf(fractionStr.trim());
               if(answer.fraction==100.0){
                  singleCorrectAnswer = true;
               }
               answer.text = answerText.trim();
               question.answers.add(answer);

            }

            if(singleCorrectAnswer){
               question.type = question.type+"-single";        

            }
         }
      }
      //         String quizName = "";
      //         Quiz quizObj = null;
      //         Question question = null;
      //         for(String line : lineList){    
      //            
      //            
      //            if(line.startsWith("//")){
      //               String quizNameStr = line.split(":")[2].split(" ")[1];      

      //            }else if(line.startsWith("::")){
      //
      //               String questionText = line.split("::")[2];
      //               question = new Question();
      //               questionText = unescapeTxtDataFormat(questionText);
      //               question.text = questionText.substring(0, questionText.length()-1);
      //               quizObj.questions.add(question);
      //            }else if(line.startsWith("\t")){
      //               line = line.replaceFirst("\t", "");
      //               
      //               String answerText = "";
      //               
      //               if(line.startsWith("=")){
      //                  answerText = line.substring(1);
      //               }else{
      //                  try{
      //                     answerText = line.split("%")[2];   
      //                  }catch(Exception ex){
      //                     answerText = line.substring(1);
      //                  }
      //                  
      //               }
      //               Answer answer = new Answer();
      //               answerText = unescapeTxtDataFormat(answerText);
      //               answer.text = answerText;
      //               question.answers.add(answer);
      //            }
      //                       
      //         }
      writeQuizzesIntoFile(quizObjs);
      //   
   }

   private static void writeQuizzesIntoFile(List<Quiz> quizObjs) throws IOException{  
      Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

      for(Quiz quiz : quizObjs){

         File f = new File(QUIZ_PATH+quiz.title+".json");

         FileUtils.writeStringToFile(f, gson.toJson(quiz), "utf-8");
      }
   }

   private static String unescapeTxtDataFormat(String str){
      str = str.replace("\\:", ":");
      str = str.replace("\\=", "=");
      str = str.replace("\\{", "{");
      str = str.replace("\\}", "}");
      str = str.replace("\\#", "#");
      str = str.replace("[html]", "");
      str = str.replace("\\Bigint", "\\int");
      str = str.replace("\\n", "<br />");
      str = str.replace("<p class=\"ilc_Standard\">", "");
      str = str.replace("</p>", "");

      return str;
   }

   private static void loadQuizzesFromHTMLFile(){
      try {
         List<String> lineList = Util.readFileAsLineList(QUIZ_PATH+"source.html");
         //         List<String> quizzes = Util.parse(lineList, null, null, null, null, "// quest", null, false);
         //         
         List<Quiz> quizObjs = new ArrayList<Quiz>();
         String quizName = "";
         Quiz quizObj = null;
         Question question = null;
         for(String line : lineList){    

            String trimmedLine = line.trim();

            if(trimmedLine.startsWith("<h3>")){
               String quizNameStr =  Util.parse(trimmedLine, "<h3>", " ", null, null, null, null, false).get(0);
               quizNameStr = quizNameStr.replaceAll(":", "");

               if(Util.isStringValid(quizNameStr)){

                  if(!quizName.equals(quizNameStr)){
                     quizObj = new Quiz();
                     quizName = quizNameStr;
                     quizObj.title = quizName;
                     quizObjs.add(quizObj);
                  }

               }
            }else if(trimmedLine.startsWith("<p>")){

               String questionText = trimmedLine;//Util.parse(trimmedLine, "<p>", "</p>", null, null, null, null, false).get(0);
               //               questionText.replaceAll("<p>", "");
               //               questionText.replaceAll("</p>", "");
               if(Util.isStringValid(questionText)){
                  question = new Question();

                  questionText = unescapeTxtDataFormat(questionText);
                  question.text = questionText.substring(0, questionText.length()-1).trim();
                  quizObj.questions.add(question);

               }
            }else if(trimmedLine.startsWith("<li>")){
               trimmedLine = trimmedLine.replaceFirst("\t", "");

               String answerText = Util.parse(trimmedLine, "<li>", "</li>", null, null, null, null, false).get(0);
               if(Util.isStringValid(answerText)){
                  Answer answer = new Answer();

                  answerText = unescapeTxtDataFormat(answerText);
                  answer.text = answerText.trim();
                  question.answers.add(answer);

               }

            }

         }  
         writeQuizzesIntoFile(quizObjs);

      } catch (IOException e) {
         Logger.error(e, e.getMessage());
      }
   }

   public static void main(String[] args) throws IOException {
      QUIZ_PATH = ".." + FS +"quizzes" + FS;
      File file = new File(QUIZ_PATH);
      if (!file.exists()) {
         file.mkdirs();
      }
      loadQuizzesFromXmlFile();
      //      loadQuizzesFromHTMLFile();
   }


   public String getQuizPath() {
      return QUIZ_PATH;
   }

}
