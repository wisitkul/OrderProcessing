import java.util.ArrayList;
import java.util.List;

/**
 * SUBJECT/PUBLISHER — ถือรายชื่อผู้สังเกตการณ์ แล้ว broadcast เมื่อ processOrder
 * รู้จักผู้รับผ่าน interface เท่านั้น เพิ่มผู้รับใหม่ได้โดยไม่แก้คลาสนี้ (OCP)
 */
public class OrderProcessor {
    private final List<OrderObserver> observers = new ArrayList<>();

    public void register(OrderObserver observer) {
        if (observer == null) throw new IllegalArgumentException("observer must not be null");
        observers.add(observer);
    }

    public void unregister(OrderObserver observer) {
        observers.remove(observer);
    }

    /** ประมวลผลคำสั่งซื้อ แล้วแจ้งผู้สังเกตการณ์ทุกตัว */
    public void processOrder(Order order) {
        if (order == null) throw new IllegalArgumentException("order must not be null");
        System.out.println("Processing order " + order.orderId() + " ...");
        // วนบนสำเนา — กันผู้รับแก้รายชื่อระหว่างประกาศ
        for (OrderObserver o : List.copyOf(observers)) {
            o.update(order);
        }
    }
}
