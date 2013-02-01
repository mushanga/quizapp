package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class Answer extends Model{

@Column(length=5000)
   public String text;
   public Double fraction;
}
