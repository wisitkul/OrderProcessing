/** STRATEGY — "วิธีคำนวณส่วนลด" คืนราคาหลังหักส่วนลดแล้ว */
public interface DiscountStrategy {
    double applyDiscount(Order order);
    
}
