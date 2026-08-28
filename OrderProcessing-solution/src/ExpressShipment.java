/** จัดส่งแบบด่วน ค่าส่ง 150 บาท */
public class ExpressShipment implements Shipment {
    @Override public String getInfo() { return "Express Delivery"; }
    @Override public double getCost() { return 150.0; }
}
