/** Observer (Part 5): ตัดสต๊อกเมื่อมีคำสั่งซื้อใหม่ */
public class InventoryService implements OrderObserver {
    @Override public void update(Order order) {
        // TODO (5a): พิมพ์ "Inventory updated for order <orderId>"
        //   hint: System.out.println("Inventory updated for order " + order.orderId());
        /* ====== fill in 1 line here ====== */
        System.out.println("Inventory updated for order " + order.orderId());
    }
}
