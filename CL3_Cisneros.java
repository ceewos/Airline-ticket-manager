/* Carlos Manuel Cisneros
   [CS1101] Comprehensive Lab 3
   This work is to be done individually. It is not permitted to
   share, reproduce, or alter any part of this assignment for any
   purpose. Students are not permitted from sharing code, uploading
   this assignment online in any form, or viewing/receiving/
   modifying code written from anyone else. This assignment is part
   of an academic course at The University of Texas at El Paso and
   a grade will be assigned for the work produced individually by
   the student.
*/
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.IOException;

public class CL3_Cisneros {
  public static void main(String[] args)throws IOException {
    boolean contProg=true;//continue program
    Scanner scnr = new Scanner(System.in);
    int menuChoice;
    String cityChoice = "";
    int cityIndx = -2;
    String fileName = "flights.csv";
    Flight[] flights = createFlights(fileName);
    Trip currTrip = new Trip();
    String currCityCountry = "El Paso, United States";//we set the curr loaction to el paso
    String receipt = "";
    String clearReceipt;//takes in yes or no input to clear receipt
    boolean valid = false;//used for while loops that detect if input is valid
    String frequentFlier;//user input frequent flyer ID
    double discount = 0.0;
    double subTotal = 0.0;
    double discountAmt = 0.0;
    double subTotalWithDiscount = 0.0;
    System.out.println("Welcome to Cisneros Airlines!!!");
    while(contProg){
      //at the end of every switch statement it will return to display menu
      menuChoice=displayMainMenu();//displays main menu and also returns user Input for menu choice

      switch(menuChoice){
        case 1:
          displayAvailableFlights(flights);
        break;
        case 2:
          System.out.print("\nEnter the name of the city you'd like to travel to:\n>");
          while(cityIndx<0){
            if(cityIndx==-1){
              System.out.print("We dont have flights available for "+cityChoice+" please try again\n>");
            }
            cityChoice=scnr.nextLine();
            cityIndx=getDestinationIdx(cityChoice, flights);
          }
          currTrip = new Trip(flights[cityIndx]);
          currTrip = fillTrip(currTrip);
          /* fill trip gathers users input on wether they want OW or RT and also collects the amount of seats they
          wish to purchase and adds it to currTrip*/
          receipt += updateReceipt(currTrip,currCityCountry);
          System.out.println("Note: Trip to "+currTrip.getCity()+" was saved\n");
          if(!currTrip.getIsRoundTrip()){
            currCityCountry=currTrip.getCity()+" ,"+currTrip.getCountry();
          }
          //System.out.println(currTrip.getTrip());
          subTotal+=currTrip.getPrice();
          cityIndx=-2;
        break;
        case 3:
          System.out.println("------------RECEIPT--------------");
          System.out.println(receipt);

        break;
        
        case 4:
          System.out.print("Are you sure you want to clear flights selected? [Yes/No]\n>");
        
          while(!valid){//if the user enters a valid input valid=true and loop is ended 
            clearReceipt=scnr.next();
            if(clearReceipt.equalsIgnoreCase("yes")){
              System.out.println("\nNote: Your trip was cleared.\n");
              currTrip=new Trip();
              receipt="";
              currCityCountry="El Paso, United States";
              valid=true;
              subTotal=0.0;
            }else if(clearReceipt.equalsIgnoreCase("no")){
              System.out.println("\nNote: Your trip was not cleared.\n");
              valid=true;
            }else{
              System.out.print("Please enter [yes or no]\n>");
              scnr.nextLine();//flush
            }
          }
          valid=false;//resetting variable
          break;
        
        case 5:
        System.out.print("\n-----------CHECKOUT-------------\nIf your are a frequent flyer, you are eligible to receive a discount.\n"+
        "Are you a frequent flyer?(Enter yes or no):\n>");
        
        while(!valid){
          frequentFlier=scnr.next();
          if(frequentFlier.equalsIgnoreCase("yes")){
            discount=checkFF();//returns the discount, if no valid entry is recevied returns no discount
            valid=true;
          }else if(frequentFlier.equalsIgnoreCase("no")){
            valid=true;
          }else{
            System.out.print("Please enter [yes or no]\n>");
            scnr.nextLine();
          }
          
        }
        System.out.println("------------RECEIPT--------------");
        System.out.print(receipt+"\n");
        discountAmt=calculateDiscount(discount, subTotal);
        subTotalWithDiscount=calculateSubtotalWithDiscount(subTotal, discountAmt);
        viewFinalReceipt(subTotal, discount, discountAmt, subTotalWithDiscount);
        
        System.out.println("Thank you for your purchase! Goodbye. ");
        contProg=false;
        break;
        case 6:
        System.out.println("\nThank you for using Cisneros Airlines.\nStay safe, stay hydrated, and happy coding!\n");
        contProg=false;
        break;
        default:
          System.out.println("Please enter an integer value [1-5]");
        break;

      }
      
    }
  }
  public static Trip fillTrip(Trip currTrip){
    int tripChoice=0;
    int seatChoice=0;
    Scanner scnr=new Scanner(System.in);
    boolean exception=true;
    System.out.print("\n-----------TRIP SELECTION-----------\nWe found the following prices for "+
    currTrip.getCity()+"\n1. One Way price of $"+currTrip.getOneWay()+
    "\n2. Round Trip price of $"+currTrip.getRoundTrip()+"\nWhich would you like to book (make a selection of [1 or 2])\n>");
    
    while(exception){
      try {
        tripChoice = scnr.nextInt();
        if(tripChoice==1){
          currTrip.setIsRoundTrip(false);
          exception=false;
        }else if(tripChoice==2){
          currTrip.setIsRoundTrip(true);
          exception=false;
        }else{
          System.out.print("Please enter 1 or 2\n>");
        }
      } catch (InputMismatchException e) {
        System.out.print("Please provide an Integer\n>");
        scnr.nextLine();
      }
    }
    exception=true;//re-setting variable to use for seat selection try catch

    System.out.print("\n-----------SEAT SELECTION-----------\nHow many seats would you like to book?\n>");
    while(exception){
      try {
        seatChoice = scnr.nextInt();
        if(seatChoice>0){
          exception=false;
        }else{
          System.out.print("Please enter a number greater than 0\n>");
        }  
      } catch (InputMismatchException e) {
        System.out.print("Please provide an Integer\n>");
        scnr.nextLine();
      }
    }
    currTrip.setTicketNum(seatChoice);
    return currTrip;
  }

  //Method Flight calls CSVReader to create an array of Flights in the file
  public static Flight[] createFlights(String fileName) throws IOException{
    CSVReader fileReader=new CSVReader();
    Flight[] flight =fileReader.getFlight(fileName);
    return flight;
  }


  /*
  /* Displays the menu options to the user MODIFIED: to return int of menu choice*/
  public static int displayMainMenu()throws IOException {
    int menuChoice=0;
    boolean exception=true;  
    Scanner scnr = new Scanner(System.in);
    System.out.println("Please select a menu choice below to get started. [Enter a number between 1-6]");  
    System.out.print("1. View Flight\n2. Add flight\n"+
      "3. View trip\n4. Managge trip\n5. checkout\n6. Exit Airlines\n>");
      
      /*In order to make code look cleaner I made the display menu return an int
      this Int is the users input of what menu choice they picked, with a try catch statement
      to insure correct input*/
      while(exception){
        try {
          menuChoice = scnr.nextInt();
          return menuChoice;
        } catch (InputMismatchException e) {
          System.out.print("Please provide an Integer\n>");
          scnr.nextLine();

        }
      }
      return menuChoice;
  }

    /*
      Displays the available flights to the user
      Receives a 1D array where all the Flight objects are stored
    */
  public static void displayAvailableFlights(Flight[] list)throws IOException {
    System.out.format("+---------------------------------------------------------------------------+%n");
		System.out.format("|                             CISNEROS AIRLINES                             |%n");
		System.out.format("+---------------------------------------------------------------------------+%n");
		System.out.format("|          CITY           |         COUNTRY         |  ONEWAY   | ROUNDTRIP |%n");
		System.out.format("+-------------------------+-------------------------+-----------+-----------+%n");
		
		for( int row = 0; row < list.length; row++ ){
			System.out.printf("| %23s | %23s | $%8s | $%8s |%n", list[row].getCity(), list[row].getCountry(), list[row].getOneWay(), list[row].getRoundTrip());
			System.out.format("+-------------------------+-------------------------+-----------+-----------+%n");
		}
    System.out.println();
  }
  
    /*
      Returns the flight position if the user inputted a valid destination based on available flights
      Returns -1 if the city was not found
      Receives a 1D array where all the Flight objects are stored
    */
  public static int getDestinationIdx(String city, Flight[] flights)throws IOException {
    for(int i=0; i < flights.length; i++){
      if(flights[i].getCity().equalsIgnoreCase(city)){
       return i;
      }
    }
    return -1;

  }
  
    /*
      Returns a formatted string that will be used to update the receipt
      Receives a Trip object, where you can use the flight object to obtain the one way / round trip price
      and the current location
    */
  public static String updateReceipt(Trip trip, String currLocation)throws IOException {
    String tripType="";
    if(trip.getIsRoundTrip()){
      tripType="Round trip";
    }else{
      tripType="One Way";
    }
    String receipt = currLocation+"-->"+trip.getCity()+", "+trip.getCountry()+"\nNumber of seats : "+trip.getTicketNum()+
    "\nTrip type : "+tripType+" | Flight total : "+getTripPrice(trip)+"\n---------------------------------\n";
    return receipt;
  
  }
  
    /*
      Returns the total price based on the type of trip (one way / round trip) and the number of seats
      Receives a Trip object
    */
    public static double getTripPrice(Trip trip)throws IOException{
      return trip.getPrice();
    }
  
    /* Returns the total amount discounted */
  public static double calculateDiscount(double discount, double subTotal)throws IOException {
    discount *= subTotal;
    return discount;
  }
  
    /* Returns the subtotal considering the discount */
  public static double calculateSubtotalWithDiscount(double subtotal, double totalDiscount) {
    subtotal -= totalDiscount;
    return subtotal;
  }
  
    /* Returns the total amount of tax charged based on subtotal */
  public static double calculateTotalTax(double subtotalWithDiscount, double tax)throws IOException {
    tax *= subtotalWithDiscount;
    return tax;
  }
  
    /* Returns the total amount (total + tax) */
  public static double calculateTotal(double subtotalWithDiscount, double totalTax)throws IOException {
    subtotalWithDiscount += totalTax;
    return subtotalWithDiscount;
  }
  
    /* Displays the last part of the "receipt", includes subtotal, discount, discount amount, tax, etc. */
  public static void viewFinalReceipt(double subTotal, double discount, double discountAmt, double subTotalWithDiscount) throws IOException{
    double tax = .0675;
    double totalTax=calculateTotalTax(subTotalWithDiscount, tax);
    System.out.println("Subtotal: "+subTotal);
    System.out.println("Discount percentage: "+discount*100+"%%");
    System.out.printf("Discount amount: %.2f\n",discountAmt);
    System.out.printf("Total after Discount: %.2f\n",subTotalWithDiscount);
    System.out.printf("Tax: %.2f\n\n",totalTax);
    System.out.printf("Total: %.2f\n",calculateTotal(subTotalWithDiscount, totalTax));
  }
  public static double checkFF() throws IOException{//checks if is a valid frequent flyer and returns the discount
    Scanner scnr=new Scanner(System.in);
    String fileName="frequent-flier.csv";
    String input="";
    int miles=-1;
    double discount=0.0;
    boolean valid=false;
    CSVReader fileReader=new CSVReader();
    FrequentFlyer[] frequentFlyers =fileReader.getFrequentFlyer(fileName);
    System.out.print("Please enter your frequent flyer number: \n>");
    
    while(!valid){
      input=scnr.next();
        
      if(input.equalsIgnoreCase("Exit")){//an option to exit frequent flier menu
        valid=true;        
      }
        
      for(int i=0; i<frequentFlyers.length;i++){
        if(input.equals(frequentFlyers[i].getUser())){
          miles=frequentFlyers[i].getMiles();
          valid=true;
        }
      }

      if(!valid){
        System.out.print("Flyer number not found, please try again or enter [EXIT]\n>");
      }
    }

    if(miles<0){
      discount = 0.0;
    }else if(miles<5000){
      discount = .05;
    }else if(miles<10000){
      discount = .10;
    }else if(miles<15000){
      discount = .15;
    }else if(miles<20000){
      discount = .30;
    }else if(miles<40000){
      discount = .40;
    }else if(miles>=40000){
      discount = .50;
    }

    return discount;
  }
}