/** OBSERVER — "ผู้สนใจเหตุการณ์คำสั่งซื้อใหม่" */
public interface OrderObserver {
    void update(Order order);
}
