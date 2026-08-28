/** ประเภทการจัดส่ง — เป็น interface เพื่อให้ห่อด้วย Decorator ได้ */
public interface Shipment {
    String getInfo();
    double getCost();
}
