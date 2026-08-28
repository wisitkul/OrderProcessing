import java.util.List;

/** ส่วนที่ 6 — ประกอบร่างทุก pattern แล้วพิมพ์สรุป (แคปหน้าจอส่งงานได้เลย) */
public class Main {
    public static void main(String[] args) {
        // 1) ตั้งค่า
        Product p1 = new Product("P001", "Keyboard", 800);
        Product p2 = new Product("P002", "Mouse", 400);
        Order order = new Order("ORD-1001", List.of(p1, p2), "customer@example.com");

        OrderCalculator calculator = new OrderCalculator();
        ShipmentFactory factory = new ShipmentFactory();
        OrderProcessor processor = new OrderProcessor();

        System.out.println("=== Order " + order.orderId() + " ===");
        System.out.printf("Items total: %.2f%n", order.getTotalPrice());

        // 2) คำนวณราคา — ทดลองทั้งสอง strategy
        double byPercent = calculator.calculateFinalPrice(order, new PercentageDiscount(10));
        double byFixed   = calculator.calculateFinalPrice(order, new FixedDiscount(100));
        System.out.printf("After 10%% discount : %.2f%n", byPercent);
        System.out.printf("After 100 discount : %.2f%n", byFixed);

        // 3) สร้างการจัดส่งด้วย Factory
        Shipment shipment = factory.createShipment("STANDARD");

        // 4) เพิ่มบริการเสริมด้วย Decorator (ห่อซ้อนกัน)
        shipment = new GiftWrapDecorator(shipment);
        shipment = new InsuranceDecorator(shipment, order);

        // 5) พิมพ์สรุป
        double finalItemPrice = byPercent;                 // เลือกใช้ส่วนลด 10%
        double grandTotal = finalItemPrice + shipment.getCost();
        System.out.println("\n--- Shipment ---");
        System.out.println("Info : " + shipment.getInfo());
        System.out.printf("Shipping cost : %.2f%n", shipment.getCost());
        System.out.println("\n--- Grand Total ---");
        System.out.printf("Items (after discount) + Shipping = %.2f + %.2f = %.2f%n",
                finalItemPrice, shipment.getCost(), grandTotal);

        // 6) ยืนยันคำสั่งซื้อ -> Observer ทำงาน
        System.out.println("\n--- Confirm Order ---");
        processor.register(new InventoryService());
        processor.register(new EmailService());
        processor.processOrder(order);
    }
}
