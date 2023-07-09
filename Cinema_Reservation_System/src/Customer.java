public class Customer {
	
    private String name;
    private String email;
    private String paymentMethod;

    public Customer(String name, String email, String paymentMethod) {
        this.name = name;
        this.email = email;
        this.paymentMethod = paymentMethod;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
