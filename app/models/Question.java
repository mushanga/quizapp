package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class Question extends Model{
   @Column(length=5000)
   public String name;
   @Column(length=5000)
   public String text;

   @OneToMany(cascade=CascadeType.ALL)
   public List<Answer> answers = new ArrayList<Answer>();

   @Column
   public String type;
   
   

}
