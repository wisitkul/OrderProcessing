/** Strategy (Part 2): ลดราคาเป็นจำนวนเงินคงที่ เช่น new FixedDiscount(100) = ลด 100 บาท */
public class FixedDiscount implements DiscountStrategy {
    private final double amount;

    public FixedDiscount(double amount) {
        if (amount < 0) throw new IllegalArgumentException("amount must be >= 0");
        this.amount = amount;
    }

    @Override public double applyDiscount(Order order) {
        
        return  Math.max(order.getTotalPrice() - amount , 0);
    }
}
