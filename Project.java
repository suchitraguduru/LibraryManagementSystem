import java.util.*;
@SuppressWarnings("unchecked")

class Library{
	//For storing book data
	static HashMap<Integer,Book> bhash;
	//For storing patrons data
	static HashMap<Integer,Patron> pathash;
	public Library(){
		bhash = new HashMap<>();
		pathash = new HashMap<>();
	}
	//Method for adding books
	public static void addBook(String title,String author,int ISBN,int quant){
	     try{
		if(bhash.containsKey(ISBN)){
			throw new MyException("Book is already added");
		}else{
			Book nbook = new Book(title,author,ISBN,quant);
			bhash.put(ISBN,nbook);
		}
	     }catch (MyException ex) {
            		System.out.println(ex.getMessage());
             }
	}
	//Method for removing books
	public static void removeBook(int ISBN){
	     try{
		if(bhash.containsKey(ISBN)){
			bhash.remove(ISBN);
		}else{
			throw new MyException("book is not available");
		}
	      }catch (MyException ex) {
            		System.out.println(ex.getMessage());
              }
	}
	//Method to display books
	public static void displayBooks(){
		for(Integer key:bhash.keySet()){
			System.out.println();
			System.out.println("Book id: "+key);
			System.out.println("Book title: "+bhash.get(key).getTitle());
			System.out.println("Book author: "+bhash.get(key).getAuthor());
			System.out.println("Book quantity: "+bhash.get(key).getQuantity());
			
		}
	}
	//Method to add patrons
	public static void addPatron(String name, int id,Set<Integer> borrowBooks){
		if(pathash.containsKey(id)){
			System.out.println("Patron is already added");
			return;
		}else{
			Patron pt = new Patron(name, id,borrowBooks);
			pathash.put(id,pt);
		}
	}
	//Method to remove Patrons
	public static void removePatron(int id){
		if(pathash.containsKey(id)){
			pathash.remove(id);
		}else{
			System.out.println("patron is not available");
			return;
		}
	}
	//Method to display patrons
	public static void displayPatrons(){
		for(Integer key:pathash.keySet()){
			System.out.println("Patron id: "+key);
			System.out.println("Patron name: "+pathash.get(key).getName());
			System.out.println("Borrowed books ");
			Set<Integer> arr = pathash.get(key).getBorrowBooks();
			for(Integer k:arr){
				System.out.print(k+" ");
			}
			System.out.println();
		}
	}
}
//Book Structure
class Book{
	private String title;
	private String author;
	private int ISBN;
	private int quantity;
	Book(String title, String author, int ISBN, int q){
		this.title = title;
		this.author = author;
		this.ISBN = ISBN;
		this.quantity = q;
	}
	//Encapsulating book details using getters and setters
	public String getTitle(){
		return this.title;
	}
	public String getAuthor(){
		return this.author;
	}
	public int getISBN(){
		return this.ISBN;
	}
	public int getQuantity(){
		return this.quantity;
	}
	public void setTitle(String t){
		this.title = t;
	}
	public void setAuthor(String a){
		this.title = a;
	}
	public void setISBN(int id){
		this.ISBN = id;
	}
	public void setQuantity(int q){
		this.quantity = q;
	}

}
//Patron Structure
class Patron{
	private String name;
	private int id;
	private Set<Integer> borrowBooks;
	Patron(String n,int id,Set<Integer> bb){
		this.name = n;
		this.id = id;
		borrowBooks = bb;
	}
	//Encapsulating Patron data using getters and setters
	public String getName(){
		return this.name;
	}
	public int getId(){
		return this.id;
	}
	public Set getBorrowBooks(){
		return this.borrowBooks;
	}
	public void setName(String n){
		this.name = n;
	}
	public void setID(int id){
		this.id = id;
	}
	public void setBorrowBooks(Set<Integer> b){
		this.borrowBooks = b;
	}
}
//abstract interface of Borrowable
interface Borrowable {
    void borrow(int bid,int pid,HashMap<Integer,Book> bhash,HashMap<Integer,Patron> pathash);
    void returnBook(int bid,int pid,HashMap<Integer,Book> bhash,HashMap<Integer,Patron> pathash);
}
class Borrows implements Borrowable{
	//function to borrow books by patrons
	public void borrow(int bid,int pid,HashMap<Integer,Book> bhash,HashMap<Integer,Patron> pathash){
	  try{
		
		if(!bhash.containsKey(bid)){
			throw new MyException("Book is not available");
		}
		if(bhash.get(bid).getQuantity()==0){
			throw new MyException("Book Stock is not available");
		}
		if(!pathash.containsKey(pid)){
			throw new MyException("Invalid Patron Id");
		}
		Book bk = bhash.get(bid);
		bk.setQuantity(bk.getQuantity()-1);
		bhash.replace(bid,bk);
		Patron pr = pathash.get(pid);
		Set<Integer> bb = pr.getBorrowBooks();
		bb.add(bid);
		pr.setBorrowBooks(bb);
		pathash.replace(pid,pr);
		System.out.println("Books Borrowed Successfully");
	   }catch (MyException ex) {
            		System.out.println(ex.getMessage());
           }
	}	
	//function to return books to library
	public void returnBook(int bid,int pid,HashMap<Integer,Book> bhash,HashMap<Integer,Patron> pathash){
		try{
			if(!bhash.containsKey(bid)){
				throw new MyException("Book is not available");
			}
			if(!pathash.containsKey(pid)){
				throw new MyException("Invalid Patron Id");
			}
			Patron pr = pathash.get(pid);
			if(!pr.getBorrowBooks().contains(bid)){	
				throw new MyException("Patron does not borrowed the book");
			}
			Set<Integer> bb = pr.getBorrowBooks();
			bb.remove(bid);
			pr.setBorrowBooks(bb);	
			Book bk = bhash.get(bid);
			bk.setQuantity(bk.getQuantity()+1);
			bhash.replace(bid,bk);
			System.out.println("Book Returned Successfully");
		}catch (MyException ex) {
            		System.out.println(ex.getMessage());
        	}
		
	}
}
//Exceptional Handling for errors
class MyException extends Exception {
    public MyException(String s)
    {
        super(s);
    }
}
class Project{
	public static void main(String[] args) throws Exception{
		Scanner sc = new Scanner(System.in);
		System.out.println("LIBRARY MANAGEMENT SYSTEM");
		Borrows brs =  new Borrows();
		Library lib = new Library();
		System.out.println("Choose your choice:");
		System.out.println("1.Add a new Book\n2.Remove a Book\n3.Display Available Books\n4.Add a new Patron\n5.Remove a Patron\n6.Display Patrons\n7.Borrow a Book\n8.Return a Book\n9.Exit from System.");
		int ch = sc.nextInt();
		while(ch!=9){
		switch(ch){
			case 1:
				System.out.println("Enter Book Details");
				System.out.println("Enter Book Title");
				String t = sc.next();
				System.out.println("Enter Book ISBN");
				int id = sc.nextInt();
				System.out.println("Enter Book Author");
				String auth = sc.next();
				System.out.println("Enter Book quantity");
				int q = sc.nextInt();
				lib.addBook(t,auth,id,q);
				break;
			case 2:
				System.out.println("Enter Book ISBN");
				int bid = sc.nextInt();
				lib.removeBook(bid);
				break;
			case 3:
				System.out.println("Available Books are");
				lib.displayBooks();
				break;
			case 4:
				System.out.println("Enter Patron Details");
				System.out.println("Enter Patron name");
				String name = sc.next();
				System.out.println("Enter Patron id");
				int pid = sc.nextInt();	
				Set<Integer> arr = new HashSet<>();
				lib.addPatron(name,pid,arr);
				break;
			case 5:
				System.out.println("Enter Patron ID");
				int ppid = sc.nextInt();
				lib.removePatron(ppid);
				break;
			case 6:
				System.out.println("Patron Details");
				lib.displayPatrons();
				break;
			case 7:
				//pid bid 
				System.out.println("Enter patron id to borrow the book: ");
				int p = sc.nextInt();
				System.out.println("Enter book id: ");
				int b = sc.nextInt();
				brs.borrow(b,p,lib.bhash,lib.pathash);
				break;
			case 8:
				System.out.println("Enter patron id to return the book: ");
				int p1 = sc.nextInt();
				System.out.println("Enter book id: ");
				int b1 = sc.nextInt();
				brs.returnBook(b1,p1,lib.bhash,lib.pathash);
				break;
			
		}
		System.out.println();
		System.out.println("Enter the choice");
		System.out.println("1.Add a new Book\n2.Remove a Book\n3.Display Available Books\n4.Add a new Patron\n5.Remove a Patron\n6.Display Patrons\n7.Borrow a Book\n8.Return a Book\n9.Exit from System.");
		ch = sc.nextInt();
		}
	}
}
