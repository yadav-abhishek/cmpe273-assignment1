package edu.sjsu.cmpe.library.api.resources;


import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;  
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.validation.Valid;

import com.yammer.dropwizard.jersey.params.IntParam;
import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.dto.AuthorDto;
import edu.sjsu.cmpe.library.dto.AuthorsDto;
import edu.sjsu.cmpe.library.dto.BookDto;
import edu.sjsu.cmpe.library.dto.LinkDto;
import edu.sjsu.cmpe.library.dto.LinksDto;
import edu.sjsu.cmpe.library.domain.Review;
import edu.sjsu.cmpe.library.dto.ReviewDto;
import edu.sjsu.cmpe.library.dto.ReviewsDto;



@Path("/v1/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class BookResource {
	 private static Integer bookId=1;
	 private static Integer authorId=1;
	 private static long reviewId=1;
	 static final HashMap<Integer,Book> bookDb =new HashMap<Integer,Book>();
	 static final  AtomicInteger idCounter = new AtomicInteger(1);
	
	  public BookResource() {
	// do nothing
    }

	  //Create Book resource for the library
	  
	  @POST
	  @Timed(name="create-book")
	  public Response createBook(@Valid Book book){
		  book.setIsbn(bookId);
		  bookDb.put(bookId,book);
		  bookId++;
		  for (int author=0;author<book.getAuthors().length;author++)
		  {
			 book.getAuthors()[author].id=authorId; 
		  }
		  
		  	BookDto bookResponse = new BookDto(book);

	        bookResponse.addLink(new LinkDto("view-book", "/books/" + book.getIsbn(),"GET"));
	        bookResponse.addLink(new LinkDto("update-book","/books/" + book.getIsbn(), "PUT"));
	        bookResponse.addLink(new LinkDto("delete-book","/books/" + book.getIsbn(), "DELETE"));
	        bookResponse.addLink(new LinkDto("create-review","/books/"+ book.getIsbn() +"/reviews", "POST"));
	        //System.out.println("Hashmap----"+bookDb.get(0) + "xyz"+bookDb.get(1));
	        return Response.status(201).entity(bookResponse.getLinks()).build();
	  
	  }
	
	// View book using isbn  
	  
    @GET
    @Path("/{isbn}")
    @Timed(name = "view-book")
    public BookDto getBookByIsbn(@PathParam("isbn") IntParam isbn) {
	   
    	
    	Book getBook = bookDb.get(isbn.get());
    	//IntParam temp=isbn;
    	//System.out.println("bookDb.get(0)"+bookDb.get(0)+"bookDb.get(1)"+bookDb.get(1));
    	System.out.println("isbn.get()---------------------"+isbn.get());
    	System.out.println("--- bookDb.get(isbn)---"+ bookDb.get(isbn)+"getbook-------"+getBook);
	   	BookDto bookResponse = new BookDto(getBook);
    	bookResponse.addLink(new LinkDto( "view-book","/books/" + getBook.getIsbn(),"GET"));
    	bookResponse.addLink(new LinkDto("update-book","/books/" + getBook.getIsbn(), "PUT"));
		bookResponse.addLink(new LinkDto("delete-book","/books/" + getBook.getIsbn(), "DELETE"));
		bookResponse.addLink(new LinkDto("create-review","/books/" + getBook.getIsbn() + "/reviews", "POST"));
	// add more links
        if(getBook.getReviews().size() > 0){
        	bookResponse.addLink(new LinkDto("view-all-resources","/books/" + getBook.getIsbn() + "/reviews", "GET"));
        }
	return bookResponse;
    }

 
    @DELETE
    @Path("/{isbn}")
    @Timed(name = "delete-book")
    public Response deleteBook(@PathParam("isbn") Integer isbn) {

		bookDb.remove(isbn);

		LinksDto links = new LinksDto();
		links.addLink(new LinkDto("create-book", "/books", "POST"));

	return Response.ok(links).build();
    }
    
    
    //Update book status from available to lost
    
    @PUT
    @Path("/{isbn}")
    @Timed(name = "update-book")
    public Response updateBook(@PathParam("isbn") IntParam isbn, @QueryParam("status") String status)  {

    	Book getBook=bookDb.get(isbn.get());
		getBook.setStatus(status);

		BookDto bookResponse = new BookDto(getBook);
		bookResponse.addLink(new LinkDto("view-book", "/books/" + getBook.getIsbn(), "GET"));
		bookResponse.addLink(new LinkDto("update-book","/books/" + getBook.getIsbn(), "PUT"));
		bookResponse.addLink(new LinkDto("delete-book","/books/" + getBook.getIsbn(), "DELETE"));
		bookResponse.addLink(new LinkDto("create-review","/books/" + getBook.getIsbn() + "/reviews", "POST"));
		if (getBook.getReviews().size() !=0 ){
			bookResponse.addLink(new LinkDto("view-all-reviews","/books/" + getBook.getIsbn() + "/reviews", "GET"));
			}

	return Response.ok().entity(bookResponse.getLinks()).build();  
    }
    

    
    //Add reviews to book 
    
    
    @POST
    @Path("/{isbn}/reviews")
    @Timed(name = "create-review")
    public Response createReview(@Valid Review reviews, @PathParam("isbn") int isbn) {

		Book getBook = bookDb.get(isbn);
		
		System.out.println("isbn.get()="+isbn+"bookDb.get(1)"+bookDb.get(1));
		System.out.println("getBook"+getBook);

		reviews.setIsbn(reviewId);
		getBook.getReviews().add(reviews);
		reviewId++;

		ReviewDto reviewResponse = new ReviewDto();
		reviewResponse.addLink(new LinkDto("view-review", "/books/" + getBook.getIsbn() + "/reviews/" + reviews.getIsbn(), "GET"));

	return Response.status(201).entity(reviewResponse.getLinks()).build(); //this sends the response with 201
    }
    
    
    // Method to get review using reviewId.
    
    @GET
    @Path("/{isbn}/reviews/{id}")
    @Timed(name = "view-review")
    public ReviewDto viewReview(@PathParam("isbn") int isbn, @PathParam("id") long id) {
		int i=0;
		Book getBook = bookDb.get(isbn);

		while (getBook.getAreview(i).getIsbn() != id)
		{
			i++;
		}

		ReviewDto reviewResponse = new ReviewDto(getBook.getAreview(i));
		reviewResponse.addLink(new LinkDto("view-review", "/books/" + getBook.getIsbn() + "/reviews/" + getBook.getAreview(i).getIsbn(), "GET"));

		return reviewResponse;
    }
    
    
    //Get all reviews at a time for a given isbn number
	
    @GET
    @Path("/{isbn}/reviews")
    @Timed(name = "view-all-reviews")
    public ReviewsDto viewAllReviews(@PathParam("isbn") int isbn) {

		Book getBook = bookDb.get(isbn);
		ReviewsDto reviewResponse = new ReviewsDto(getBook.getReviews());

	return reviewResponse;
    }
    
    //view book author using authorid
    @GET
    @Path("/{isbn}/authors/{id}")
    @Timed(name = "view-author")
    public Response viewAuthor(@PathParam("isbn") int isbn, @PathParam("id") int id) {
		int i=0;
		Book getBook = bookDb.get(isbn);
		while (getBook.getAnAuthor(i).id!=id)
		{
			i++;
		}
		AuthorDto authorResponse = new AuthorDto(getBook.getAnAuthor(i));
		authorResponse.addLink(new LinkDto("view-author", "/books/" + getBook.getIsbn() + "/authors/" + getBook.getAnAuthor(i).id, "GET"));

	return Response.ok(authorResponse).build();
    }

	//View all authors at a time for a book
    
	@GET
    @Path("/{isbn}/authors")
    @Timed(name = "view-all-authors")
    public AuthorsDto viewAllAuthors(@PathParam("isbn") long isbn) {

		Book retrieveBook = bookDb.get(isbn);
		AuthorsDto authorResponse = new AuthorsDto(retrieveBook.getAuthors());

	return authorResponse;
    
	}
    
   
}
