package jobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Answer;
import models.Question;
import models.Quiz;

import org.apache.commons.io.FileUtils;
import org.jdom.Element;

import util.XMLUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Import {
   private static String QUIZ_PATH = null;
   private static final String FS = System.getProperty("file.separator");

  

   private static void createQuizFolder() {
      String quizPath = ".." + FS +"quizzes" + FS;
      File file = new File(quizPath);
      if (!file.exists()) {
         file.mkdirs();
      }
      QUIZ_PATH = quizPath;

   }

   private static void loadQuizzesFromXmlFile() throws IOException{
      createQuizFolder();
      
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

       writeQuizzesIntoFile(quizObjs);
   }
   
   private static void writeQuizzesIntoFile(List<Quiz> quizObjs) throws IOException{  
      Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
   
      for(Quiz quiz : quizObjs){
      
         File f = new File(QUIZ_PATH+quiz.title+".json");
       
         FileUtils.writeStringToFile(f, gson.toJson(quiz), "utf-8");
      }
   }
   
   
   public static void main(String[] args) throws IOException {
      QUIZ_PATH = ".." + FS +"quizzes" + FS;
      File file = new File(QUIZ_PATH);
      if (!file.exists()) {
         file.mkdirs();
      }
      loadQuizzesFromXmlFile();
   }



}
