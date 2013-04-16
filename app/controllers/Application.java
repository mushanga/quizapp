package controllers;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import models.Category;
import models.Quiz;
import play.mvc.Controller;

public class Application extends Controller {
	public static void index() {

	   Category root = Category.find("parent is null").first();
      Collections.sort(root.children);
		render("Application/groups.html", root);
	}

   public static void quiz(String groupId, String quizId) {

      
      Quiz quiz = Quiz.findById(Long.valueOf(quizId));
      
      render(quiz);
      
      
   }

   public static void group(String id) {

      
      Category group = Category.findById(Long.valueOf(id));
      if(group.parent==null){

         Category root = group;
         render("Application/groups.html", root);
      }
//      Category parent = Category.findById(group.parent);
      
      
      render(group);
      
      
   }
   public static void quizData(String id) {

      
      Quiz quiz = Quiz.findById(Long.valueOf(id));
      
      renderJSON(quiz);
      
      
   }
   public static void options() {

      
     
      render();
      
      
   }
}