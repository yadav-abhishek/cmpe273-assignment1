package edu.sjsu.cmpe.library.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import edu.sjsu.cmpe.library.domain.Author;

@JsonPropertyOrder(alphabetic = true)
public class AuthorsDto extends LinksDto {
    private Author[] author;

    
    public AuthorsDto(Author[] author) {
    	super();
    	this.author = author;
        }

	
    public Author[] getAuthor() {
	return author;
    }

    
    public void setAuthor(Author[] author) {
	this.author = author;
    }

}