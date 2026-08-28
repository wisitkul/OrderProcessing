/** Decorator (Part 4): ห่อของขวัญ +50 บาท */
public class GiftWrapDecorator extends ShipmentDecorator {
    private static final double GIFT_WRAP_FEE = 50.0;

    public GiftWrapDecorator(Shipment wrappedShipment) {
        super(wrappedShipment);
    }

    @Override public String getInfo() {
        // TODO (4a): คืน info ของตัวที่ห่อ แล้วต่อท้ายด้วย " + Gift Wrapped"
        //   hint: wrappedShipment.getInfo() + " + Gift Wrapped"
        return  wrappedShipment.getInfo() + " + Gift Wrapped";
    }

    @Override public double getCost() {
        // TODO (4b): คืน cost ของตัวที่ห่อ บวกด้วย GIFT_WRAP_FEE
        //   hint: wrappedShipment.getCost() + GIFT_WRAP_FEE
        return wrappedShipment.getCost() + GIFT_WRAP_FEE;
    }
}
