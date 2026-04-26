package PetClinic.model.billing;

public class Payment {
    private Invoice invoice;
    private String method;

    public Payment(Invoice invoice, String method) {
        this.invoice = invoice;
        this.method = method;
    }

    public void process() {
        invoice.markAsPaid();
        System.out.println("Payment of PHP " + invoice.getTotalAmount()
                + " via " + method + " processed successfully.");
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "Payment[method=" + method + ", invoice=" + invoice + "]";
    }
}