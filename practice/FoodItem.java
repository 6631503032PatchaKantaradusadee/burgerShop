package practice;
import java.io.*;
import java.util.*;

interface FoodItem{
 String getName();
 double getPrice();
 String getDescription();
 void setMembershipNumber(String phoneNumber);
 double applyDiscount();
}

abstract class Burger implements FoodItem{
 protected String name;
 protected double price;
 protected String membershipNumber;
 
 public Burger(String name, double price) {
  this.name = name;
  this.price = price;
 }
 
 @Override
 public String getName() {
  return name;
 }
 
 @Override
 public double getPrice() {
  return price;
 }
 
 @Override
 public void setMembershipNumber(String phoneNumber) {
  this.membershipNumber = phoneNumber;
 }
 
 @Override
 public double applyDiscount() {
  return (membershipNumber != null) ? price * 0.9 : price;
 }
}

class CheeseBurger extends Burger{
 public CheeseBurger() {
  super("Cheese Burger", 60);
 }
 
 @Override
 public String getDescription() {
  return "Juicy beef patty with melted cheese.";
 }
}

class VeggieBurger extends Burger{
 public VeggieBurger() {
  super("Veggie Burger", 50);
 }
 
 @Override
 public String getDescription() {
  return "Delicuious veggie patty with fresh vegetables.";
 }
}

class CustomBurger extends Burger{
 private Map<String, Double> ingredientPrices = new HashMap<>();
    private List<String> ingredients = new ArrayList<>();

    public CustomBurger(String[] ingredients) {
        super("Custom Burger", 50); // Base price for custom burger

        // Initialize ingredient prices
        ingredientPrices.put("Beef", 30.0);
        ingredientPrices.put("Pork", 25.0);
        ingredientPrices.put("Cheese", 15.0);
        ingredientPrices.put("Pickles", 10.0);
        ingredientPrices.put("Tomatoes", 10.0);
        ingredientPrices.put("Lettuce", 10.0);

        // Add selected ingredients and calculate price
        for (String ingredient : ingredients) {
            if (ingredientPrices.containsKey(ingredient)) {
                this.ingredients.add(ingredient);
                this.price += ingredientPrices.get(ingredient);
            } else {
                System.out.println("Invalid ingredient: " + ingredient);
            }
        }
    }
 
 @Override
 public String getDescription() {
  return "Custom burger with " + ingredients;
 }
}

class InvalidMenuItemException extends Exception{
 public InvalidMenuItemException(String message) {
  super(message);
 }
}

class OrderManager{
 private List<FoodItem> orderItems = new ArrayList<>();
 private Random random = new Random();
 
 public void addItem(FoodItem item) {
  orderItems.add(item);
 }
 
 public void printOrder() {
  for(FoodItem item : orderItems) {
   double discountedPrice = item.applyDiscount();
   System.out.println(item.getName() + ": " + item.getDescription() + " - THB " + discountedPrice);
  }
 }
 
 public void saveOrderToFile(String fileName) throws IOException{
  try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))){
   for(FoodItem item : orderItems) {
    double discountedPrice = item.applyDiscount();
    writer.println(item.getName() + "," + item.getDescription() + "," + discountedPrice);
   }
  }
 }
 
 public List<FoodItem> getOrderItems(){
  return orderItems;
 }
 
 public int generateRandomNumber() {
  return random.nextInt(20) + 10;
 }
}