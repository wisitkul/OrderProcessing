/**
 * DECORATOR (abstract) — "เสื้อคลุม" ของ Shipment
 * "เป็น" Shipment (implements) และ "ถือ" Shipment ไว้ข้างใน (wrappedShipment)
 * ค่าเริ่มต้นคือส่งงานต่อให้ตัวที่ห่อ — คลาสลูกค่อย override เพิ่มความสามารถ
 */
public abstract class ShipmentDecorator implements Shipment {
    protected Shipment wrappedShipment;

    public ShipmentDecorator(Shipment wrappedShipment) {
        if (wrappedShipment == null)
            throw new IllegalArgumentException("wrappedShipment must not be null");
        this.wrappedShipment = wrappedShipment;
    }

    @Override public String getInfo() { return wrappedShipment.getInfo(); }
    @Override public double getCost() { return wrappedShipment.getCost(); }
}
