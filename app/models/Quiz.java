package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class Quiz extends Model{
	
   
   
	public String title;
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<Question> questions = new ArrayList<Question>();

   public Quiz copy(){
      
      
      
      return null;
      
   }


}

