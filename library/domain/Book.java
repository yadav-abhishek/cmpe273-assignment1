package edu.sjsu.cmpe.library.domain;

import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"publication-date", "language", "num-pages"})
public class Book {
    private Book book;
    
	private Integer  isbn;
	
	  	public Integer getIsbn() {
			return isbn;
		    }

		   
		public void setIsbn(Integer isbn) {
			this.isbn = isbn;
		    }
    private String title;
    
    	public String getTitle() {
    	return title;
        }

        
        public void setTitle(String title) {
    	this.title = title;
    	}
        
    @JsonProperty("publication-date")     
    private String pubDate;
    
    	public String getPublicationDate() {
    		return pubDate;
        }
        
       
        public void setPublicationDate(String pubDate) {
        	this.pubDate = pubDate;
        }
        
    @JsonProperty("language")    
    private String lang;
    
    	public String getLanguage() {
    		return this.lang;
        }
        
       
        public void setLanguage(String lang) {
        	this.lang = lang;
        }
       
    @JsonProperty("num-pages")    
    private int pages;
    
    	public int getPages() {
    		return this.pages;
        }
        
        
        public void setPages(int pages) {
        	this.pages = pages;
        }
       
    private String status="available";
    	public String getStatus() {
    		return this.status;
        }
        
       
        public void setStatus(String status) {
        	this.status = status;
        }

    private List<Review> reviews =new ArrayList<Review>();
    
    	public List<Review> getReviews() {
    		return this.reviews;
    	}
    
    
	public void setReviews(List<Review> reviews) {
			this.reviews = reviews;
	}
    
	
	public Review getAreview(int id){
		return this.reviews.get(id);
	}
    
    private Author[] authors;
    	public Author[] getAuthors() {
    		return this.authors;
    	}

       
        public void setAuthors(Author[] authors) {
    		this.authors = authors;
    	}

     public Author getAnAuthor(int id){
    	 return this.authors[id];
     }
        
        
         	
    	
   
}
