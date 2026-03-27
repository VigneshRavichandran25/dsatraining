// first Abstract class create pandren
abstract class Device {
    private String deviceName;
    private boolean powerStatus;

    public Device(String deviceName) {
        this.deviceName = deviceName;
        this.powerStatus = false;
        // default ta off la iruku
    }

    // inga Encapsulation use pandrom
    public String getDeviceName() {
        return deviceName;
    }

    public boolean isPowerOn() {
        return powerStatus;
    }

    public void turnOn() {
        powerStatus = true;
        // on
    }

    public void turnOff() {
        powerStatus = false;
        // off
    }

    // Abstract method (Abstraction)
    abstract void displayStatus();
}

// Derived Class - Light, inheritance use pandren
class Light extends Device {

    public Light(String name) {
        super(name);
    }

    @Override
    void displayStatus() {
        System.out.println("Light: " + getDeviceName() +
                " is " + (isPowerOn() ? "ON" : "OFF"));
    }
}

// Derived Class - Thermostat, inheritance
class Thermostat extends Device {
    private int temperature;

    public Thermostat(String name, int temp) {
        super(name);
        this.temperature = temp;
    }

    @Override
    void displayStatus() {
        System.out.println("Thermostat: " + getDeviceName() +
                " is " + (isPowerOn() ? "ON" : "OFF") +
                " | Temp: " + temperature + "°C");
    }
}

// Interface class create pandren
interface PaymentMethod {
    void processPayment(double amount);
}

// Credit Card class la interface class implement pandren
class CreditCardPayment implements PaymentMethod {
    public void processPayment(double amount) {
        System.out.println("Paid Rs." + amount + " using Credit Card");
    }
}

// PayPal class la interface class implement pandren
class PayPalPayment implements PaymentMethod {
    public void processPayment(double amount) {
        System.out.println("Paid Rs." + amount + " using PayPal");
    }
}

// UPI class la interface class implement pandren
class UPIPayment implements PaymentMethod {
    public void processPayment(double amount) {
        System.out.println("Paid Rs." + amount + " using UPI");
    }
}

// Processor (DIP implement pandren)
class PaymentProcessor {
    private PaymentMethod paymentMethod;

    public PaymentProcessor(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void pay(double amount) {
        paymentMethod.processPayment(amount);
    }
}

// Interface class create pandren
interface EmailSender {
    void sendEmail(String msg);
}

interface SMSSender {
    void sendSMS(String msg);
}

interface PushNotificationSender {
    void sendPushNotification(String msg);
}

// Email Notification class la Emailsender interface class implement pandren
class EmailNotification implements EmailSender {
    public void sendEmail(String msg) {
        System.out.println("Email sent: " + msg);
    }
}

// SMS Notification class la SMSSender interface class implement pandren
class SMSNotification implements SMSSender {
    public void sendSMS(String msg) {
        System.out.println("SMS sent: " + msg);
    }
}

// Mobile App Notification class la PushNotificationSender interface class implement pandren
class MobileAppNotification implements PushNotificationSender {
    public void sendPushNotification(String msg) {
        System.out.println("Push Notification: " + msg);
    }
}

// Main Class create pandren
public class week3solution {
    public static void main(String[] args) {

        // rendu objects create pandren
        Device light = new Light("Living Room Light");
        Device thermo = new Thermostat("Hall Thermostat", 24);

        light.turnOn();
        thermo.turnOn();

        light.displayStatus();
        thermo.displayStatus();

        light.turnOff();
        light.displayStatus();

        // objects create pandren
        PaymentMethod cc = new CreditCardPayment();
        PaymentProcessor processor1 = new PaymentProcessor(cc);
        processor1.pay(5000);

        PaymentMethod upi = new UPIPayment();
        PaymentProcessor processor2 = new PaymentProcessor(upi);
        processor2.pay(1500);

        // objects create pandren
        EmailSender email = new EmailNotification();
        email.sendEmail("Welcome!");

        SMSSender sms = new SMSNotification();
        sms.sendSMS("OTP: 1234");

        PushNotificationSender push = new MobileAppNotification();
        push.sendPushNotification("New message received");
    }
}