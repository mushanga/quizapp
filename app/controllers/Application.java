package controllers;

import java.util.List;

import models.Quiz;
import play.mvc.Controller;

public class Application extends Controller {
	public static void index() {

	   List<Quiz> quizzes = Quiz.find("order by title").fetch();
      
		render(quizzes);
	}

   public static void quiz(String id) {

      
      Quiz quiz = Quiz.findById(Long.valueOf(id));
      
      render(quiz);
      
      
   }
   public static void quizData(String id) {

      
      Quiz quiz = Quiz.findById(Long.valueOf(id));
      
      renderJSON(quiz);
      
      
   }
}