package jobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import models.Answer;
import models.Question;
import models.Quiz;

import play.Logger;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import util.Util;

@OnApplicationStart
public class Start extends Job {
   private static String QUIZ_PATH = null;
   private static final String FS = System.getProperty("file.separator");

   public void doJob() {
      createQuizFolder();

      File quizFolder= new File(getQuizPath());
      
      String[] fileNames = quizFolder.list();
      List<Quiz> quizzes = new ArrayList<Quiz>();
      
      Gson gson = new Gson();
      if(Util.isValid(fileNames)){
         for(String fileName : fileNames){
            
            try {
               Quiz quiz = gson.fromJson(Util.readFileAsString(getQuizPath()+fileName), Quiz.class);
               quiz.save();
          
            } catch (JsonSyntaxException e) {
               Logger.error(e, e.getMessage());
            } catch (IOException e) {
               Logger.error(e, e.getMessage());
            }
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

   public static void main(String[] args) {
      String quizPath = ".." + FS +"quizzes" + FS;
      File file = new File(quizPath);
      if (!file.exists()) {
         file.mkdirs();
      }
      
      try {
         List<String> lineList = Util.readFileAsLineList(quizPath+"alltext");
//         List<String> quizzes = Util.parse(lineList, null, null, null, null, "// quest", null, false);
//         
         List<Quiz> quizObjs = new ArrayList<Quiz>();
         String quizName = "_%+/(";
         Quiz quizObj = null;
         Question question = null;
         for(String line : lineList){    
            
            
            if(line.startsWith("//")){
               String quizNameStr = line.split(":")[2].split(" ")[1];      
               if(!quizName.equals(quizNameStr)){
                  quizObj = new Quiz();
                  quizName = quizNameStr;
                  quizObj.title = quizName;
                  quizObjs.add(quizObj);
               }
            }else if(line.startsWith("::")){

               String questionText = line.split("::")[2];
               question = new Question();
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
               answer.text = answerText;
               question.answers.add(answer);
            }
                       
         }
         for(Quiz quiz : quizObjs){
            Gson gson = new Gson();
    
            
            FileUtils.writeStringToFile(new File(quiz.title+".json"), gson.toJson(quiz));
         }
        
         int a = 0;
         a++;
      } catch (IOException e) {
         Logger.error(e, e.getMessage());
      }
      QUIZ_PATH = quizPath;

   }


   public String getQuizPath() {
      return QUIZ_PATH;
   }

}
