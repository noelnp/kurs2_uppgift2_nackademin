import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

public class CustomerTracker {
    private LocalDate currentDate;
    private Scanner scanner = new Scanner(System.in);

    public CustomerTracker(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public void checkForMembership(String fileName) {
        System.out.print("Ange namn eller personnummer: ");
        String scannerInput = scanner.nextLine();
        readCustomerData(fileName, scannerInput);
    }

    private void readCustomerData(String fileName, String scannerInput) {
        String line;
        boolean customerFound = false;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            while ((line = bufferedReader.readLine()) != null) {
                String[] customerDetails = line.split(", ");
                String socialSecurityNumber = customerDetails[0];
                String customerName = customerDetails[1];

                String dateOfPaymentLine = bufferedReader.readLine();
                LocalDate dateOfPayment = LocalDate.parse(dateOfPaymentLine);

                if (matchesInput(scannerInput, socialSecurityNumber, customerName)) {
                    checkMembershipStatus(customerName, socialSecurityNumber, dateOfPayment);
                    customerFound = true;
                    break;
                }
            }

            if (!customerFound) {
                System.out.println("Personen du angav finns inte i systemet.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   private void checkMembershipStatus(String customerName, String socialSecurityNumber, LocalDate dateOfPayment) {
        if (dateOfPayment.isAfter(currentDate.minusYears(1))) {
            System.out.println("Personen är en nuvarande medlem.");
            addToMembersHistory(customerName, socialSecurityNumber, currentDate.toString(), "members_history.txt");
        } else {
            System.out.println("Personen är en före detta kund (Medlemskapet har gått ut).");
        }
    }

    private boolean matchesInput(String scannerInput, String socialSecurityNumber, String customerName) {
        return scannerInput.equals(socialSecurityNumber) || scannerInput.equalsIgnoreCase(customerName);
    }

    private void addToMembersHistory(String customerName, String socialSecurityNumber, String date, String fileName) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true))) {
            bufferedWriter.write(customerName + ", " + socialSecurityNumber + ", " + date + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}