package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class Quiz extends Model implements Comparable<Quiz>{
	
   
   
	public String title;
	
	@OneToMany(cascade=CascadeType.ALL)
   public List<Question> questions = new ArrayList<Question>();
	
	@Lob
	public Category parent = null;
	@Override
	public int compareTo(Quiz o) {
		return this.title.compareTo(o.title);
	}

}

