/** Decorator (Part 4): ประกัน คิด 10% ของราคาสินค้าใน Order */
public class InsuranceDecorator extends ShipmentDecorator {
    private final Order order;

    public InsuranceDecorator(Shipment wrappedShipment, Order order) {
        super(wrappedShipment);
        if (order == null) throw new IllegalArgumentException("order must not be null");
        this.order = order;
    }

    @Override public String getInfo() {
        // TODO (4c): คืน info ของตัวที่ห่อ แล้วต่อท้ายด้วย " + Insurance"
        return  wrappedShipment.getInfo() + " + Insurance";
    }

    @Override public double getCost() {
        // TODO (4d): คืน cost ของตัวที่ห่อ บวกด้วย 10% ของราคาสินค้าใน order
        //   hint: wrappedShipment.getCost() + order.getTotalPrice() * 0.10
        return  wrappedShipment.getCost() + order.getTotalPrice() * 0.10 ;
    }
}
