package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class Category extends Model{

   

   @Lob
   public Category parent;
   
   @OneToMany(cascade=CascadeType.ALL)
   public List<Category> children = new ArrayList<Category>();
   
	public String name;
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<Quiz> quizzes = new ArrayList<Quiz>();

}

