
//project OODP with burger
//menu, select, add-vegetables-meat-cheese(custom), money-pay-change, random time(for wait)
//supplement ---> queue, membership-num phone-discount

package practice;
import java.io.*;
import java.util.*;

public class BurgerShop {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OrderManager orderManager = new OrderManager();
        System.out.println("===Welcome to Burger Shop!===");

        boolean canProceed = false; // Flag to determine if user can proceed to checkout

        try {
            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Cheese Burger\n2. Veggie Burger\n3. Customise Burger\n4. Print Order\n5. Save Order to File\n6. Check Out");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (!canProceed && (choice == 4 || choice == 5 || choice == 6)) {
                    System.out.println("Please select a burger first.");
                    continue; // Skip processing further
                }

                switch (choice) {
                    case 1:
                        orderManager.addItem(new CheeseBurger());
                        canProceed = true; // User can proceed to checkout after selecting a burger
                        break;
                    case 2:
                        orderManager.addItem(new VeggieBurger());
                        canProceed = true;
                        break;
                    case 3:
                        List<String> customIngredients = new ArrayList<>();
                        System.out.println("Available ingredients: ");
                        System.out.println("1. Beef\n2. Pork\n3. Cheese\n4. Pickles\n5. Tomatoes\n6. Lettuce");
                        System.out.println("Enter ingredients for custom burger (enter 0 to finish): ");
                        while (true) {
                            int ingredientChoice = scanner.nextInt();
                            scanner.nextLine();
                            switch (ingredientChoice) {
                                case 0:
                                    break;
                                case 1:
                                    customIngredients.add("Beef");
                                    break;
                                case 2:
                                    customIngredients.add("Pork");
                                    break;
                                case 3:
                                    customIngredients.add("Cheese");
                                    break;
                                case 4:
                                    customIngredients.add("Pickles");
                                    break;
                                case 5:
                                    customIngredients.add("Tomatoes");
                                    break;
                                case 6:
                                    customIngredients.add("Lettuce");
                                    break;
                                default:
                                    System.out.println("Invalid Ingredients!");
                                    break;
                            }
                            if (ingredientChoice == 0) {
                                break;
                            }
                        }
                        orderManager.addItem(new CustomBurger(customIngredients.toArray(new String[0])));
                        canProceed = true;
                        break;
                    case 4:
                        System.out.println();
                        orderManager.printOrder();
                        break;
                    case 5:
                        System.out.print("Enter file name to save order: ");
                        String fileName = scanner.nextLine();
                        orderManager.saveOrderToFile(fileName);
                        System.out.println("Order saved to " + fileName);
                        break;
                    case 6:
                        if (!canProceed) {
                            System.out.println("Please select a burger first.");
                            break; // Skip processing further
                        }

                        System.out.print("Do you have membership? (y/n): ");
                        String member = scanner.nextLine();
                        if (member.equalsIgnoreCase("y")) {
                            String phoneNumber;
                            boolean validPhoneNumber = false;
                            do {
                                System.out.print("Enter your phone number for membership: ");
                                phoneNumber = scanner.nextLine();
                                if (isValidPhoneNumber(phoneNumber)) {
                                    validPhoneNumber = true;
                                } else {
                                    System.out.println("Invalid phone number format! Please enter a 10-digit number starting with 0.");
                                }
                            } while (!validPhoneNumber);
                            System.out.println("\n----------Receipt----------");
                            orderManager.printOrder();
                            System.out.println("Order NO.: " + orderManager.generateRandomNumber());
                            System.out.println("Phone Number: " + phoneNumber);
                            System.out.println("Discounted Total: " + getTotalPrice(orderManager.getOrderItems(), phoneNumber) + " THB");
                            double cash;
                            do {
                                System.out.print("Cash: ");
                                cash = scanner.nextDouble();
                                if (cash < getTotalPrice(orderManager.getOrderItems(), phoneNumber)) {
                                    System.out.println("Insufficient cash. Please enter again.");
                                }
                            } while (cash < getTotalPrice(orderManager.getOrderItems(), phoneNumber));
                            System.out.println("Change: " + (cash - getTotalPrice(orderManager.getOrderItems(), phoneNumber)) + " THB");
                            System.out.println("Your estimated waiting time: " + orderManager.generateRandomNumber() + " minutes");
                            System.out.println("----------------------------\nThank you. Have a good day!");
                            return;
                        } else if (member.equalsIgnoreCase("n")) {
                            System.out.println("\n----------Receipt----------");
                            System.out.println("Order NO.: " + orderManager.generateRandomNumber());
                            orderManager.printOrder();
                            System.out.println("Total: " + getTotalPrice(orderManager.getOrderItems(), null) + " THB");
                            double cash;
                            do {
                                System.out.print("Cash: ");
                                cash = scanner.nextDouble();
                                if (cash < getTotalPrice(orderManager.getOrderItems(), null)) {
                                    System.out.println("Insufficient cash. Please enter again.");
                                }
                            } while (cash < getTotalPrice(orderManager.getOrderItems(), null));
                            System.out.println("Change: " + (cash - getTotalPrice(orderManager.getOrderItems(), null)) + " THB");
                            System.out.println("Your estimated waiting time: " + orderManager.generateRandomNumber() + " minutes");
                            System.out.println("----------------------------\nThank you. Have a good day!");
                            return;
                        } else {
                            System.out.println("Invalid input. Please enter only 'y' or 'n'.");
                        }
                        break;
                    default:
                        throw new InvalidMenuItemException("Invalid menu item!");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a valid choice.");
        } catch (InvalidMenuItemException | IOException e) {
            System.out.println(e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static double getTotalPrice(List<FoodItem> orderItems, String phoneNumber) {
        double totalPrice = 0;
        for (FoodItem item : orderItems) {
            if (phoneNumber != null) {
                item.setMembershipNumber(phoneNumber);
            }
            totalPrice += item.applyDiscount();
        }
        return totalPrice;
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^0\\d{9}$");
    }
}